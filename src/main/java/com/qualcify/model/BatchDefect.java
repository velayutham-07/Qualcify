package com.qualcify.model;

public class BatchDefect {
    private String batch;
    private int defect;
    private String defectType;
    private String predictedType;

    public BatchDefect(String batch, int defect, String defectType) {
        this.batch = batch;
        this.defect = defect;
        this.defectType = defectType;
    }

    public String getBatch() { return batch; }
    public int getDefect() { return defect; }
    public String getDefectType() { return defectType; }
    public String getPredictedType() { return predictedType; }

    public void setBatch(String batch) { this.batch = batch; }
    public void setDefect(int defect) { this.defect = defect; }
    public void setDefectType(String defectType) { this.defectType = defectType; }
    public void setPredictedType(String predictedType) { this.predictedType = predictedType; }
}