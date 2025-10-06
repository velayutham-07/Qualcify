package com.qualcify.model;

import java.util.List;

public class AiPredictionResult {
    private List<Double> predictions;
    private List<String> report;

    public List<Double> getPredictions() { return predictions; }
    public void setPredictions(List<Double> predictions) { this.predictions = predictions; }

    public List<String> getReport() { return report; }
    public void setReport(List<String> report) { this.report = report; }
}
