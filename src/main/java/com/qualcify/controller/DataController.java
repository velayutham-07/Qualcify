package com.qualcify.controller;

import com.qualcify.model.AiPredictionResult;
import com.qualcify.model.BatchDefect;
import com.qualcify.service.AiPredictionService;
import com.qualcify.utils.ExcelReader;
import com.qualcify.utils.WekaClassifier;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/data")
public class DataController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
    private static final String CHART_DIR = UPLOAD_DIR + "charts" + File.separator;

    @Autowired
    private AiPredictionService aiService;

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) throws Exception {
        Path filePath = Paths.get(UPLOAD_DIR, filename);
        byte[] fileBytes = Files.readAllBytes(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok().headers(headers).body(fileBytes);
    }

    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file, Model model) {
        try {
            new File(UPLOAD_DIR).mkdirs();
            new File(CHART_DIR).mkdirs();

            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
            file.transferTo(filePath.toFile());

            List<BatchDefect> defects = ExcelReader.readDefects(new FileInputStream(filePath.toFile()));
            model.addAttribute("fileName", file.getOriginalFilename());
            model.addAttribute("defects", defects);

            // Predict defect type using Weka
            for (BatchDefect d : defects) {
                d.setDefectType(WekaClassifier.classifyBatch(d.getDefect()));
            }

            // ===== Current Defects Chart =====
            List<String> batchNames = defects.stream().map(BatchDefect::getBatch).collect(Collectors.toList());
            List<Integer> defectCounts = defects.stream().map(BatchDefect::getDefect).collect(Collectors.toList());
            CategoryChart currentChart = new CategoryChartBuilder()
                    .width(800).height(400)
                    .title("Current Defects")
                    .xAxisTitle("Batch")
                    .yAxisTitle("Defect Count")
                    .build();
            currentChart.getStyler().setLegendVisible(false);
            currentChart.getStyler().setHasAnnotations(true);
            currentChart.getStyler().setXAxisLabelRotation(90);
            currentChart.addSeries("Defects", batchNames, defectCounts).setLineColor(java.awt.Color.RED);

            String currentChartPath = CHART_DIR + "defects_chart.png";
            BitmapEncoder.saveBitmap(currentChart, currentChartPath, BitmapEncoder.BitmapFormat.PNG);
            model.addAttribute("currentChartPath", "/uploads/charts/defects_chart.png");

            // ===== AI Forecast Chart =====
            AiPredictionResult aiResult = aiService.getFutureDefectPredictions(defects, 5);
            List<Double> forecastValues = aiResult.getPredictions();
            model.addAttribute("aiReport", aiResult.getReport());

            List<String> futureBatchNames = new ArrayList<>();
            int lastIndex = defects.size();
            for (int i = 1; i <= forecastValues.size(); i++) {
                futureBatchNames.add("Batch " + (lastIndex + i));
            }

            CategoryChart forecastChart = new CategoryChartBuilder()
                    .width(800).height(400)
                    .title("AI Forecast Defects")
                    .xAxisTitle("Batch")
                    .yAxisTitle("Defect Count")
                    .build();
            forecastChart.getStyler().setLegendVisible(false);
            forecastChart.getStyler().setHasAnnotations(true);
            forecastChart.getStyler().setXAxisLabelRotation(90);

            List<Double> allValues = new ArrayList<>();
            defects.forEach(d -> allValues.add((double) d.getDefect()));
            allValues.addAll(forecastValues);

            List<String> allBatchNames = new ArrayList<>(batchNames);
            allBatchNames.addAll(futureBatchNames);

            forecastChart.addSeries("Forecast", allBatchNames, allValues).setLineColor(java.awt.Color.BLUE);

            String forecastChartPath = CHART_DIR + "defects_forecast_chart.png";
            BitmapEncoder.saveBitmap(forecastChart, forecastChartPath, BitmapEncoder.BitmapFormat.PNG);
            model.addAttribute("forecastChartPath", "/uploads/charts/defects_forecast_chart.png");

            return "result";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to process Excel file: " + e.getMessage());
            return "upload";
        }
    }
}