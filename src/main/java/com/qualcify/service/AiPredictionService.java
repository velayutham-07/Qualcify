package com.qualcify.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcify.model.AiPredictionResult;
import com.qualcify.model.BatchDefect;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiPredictionService {

    private final WebClient webClient;

    public AiPredictionService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8001") // FastAPI microservice
                .build();
    }

    public AiPredictionResult getFutureDefectPredictions(List<BatchDefect> defects, int futureCount) {
        List<Map<String, Object>> defectsForAI = defects.stream()
                                                .map(d -> {
                                                    Map<String, Object> m = new HashMap<>();
                                                    m.put("batch", d.getBatch());
                                                    m.put("defect", d.getDefect());
                                                    return m;
                                                })
                                                .collect(Collectors.toList());

        Map<String, Object> payload = new HashMap<>();
        payload.put("defects", defectsForAI);
        payload.put("future_count", futureCount);

        String response = webClient.post()
                .uri("/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        AiPredictionResult result = new AiPredictionResult();
        try {
            JsonNode root = mapper.readTree(response);

            // Parse predictions
            JsonNode preds = root.get("predictions");
            if (preds != null && preds.isArray()) {
                List<Double> forecastValues = new ArrayList<>();
                for (JsonNode p : preds) forecastValues.add(p.asDouble());
                result.setPredictions(forecastValues);
            }

            // Parse report
            JsonNode reportNode = root.get("report");
            if (reportNode != null && reportNode.isArray()) {
                List<String> report = new ArrayList<>();
                for (JsonNode r : reportNode) report.add(r.asText());
                result.setReport(report);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}