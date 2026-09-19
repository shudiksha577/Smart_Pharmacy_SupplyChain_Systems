package com.pharmacy.supplychain.dto;

import jakarta.validation.constraints.NotNull;

public class TemperatureLogRequest {

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Recorded temperature is required")
    private Double recordedTemperature;

    private String notes;

    public TemperatureLogRequest() {
    }

    public TemperatureLogRequest(Long batchId, Double recordedTemperature, String notes) {
        this.batchId = batchId;
        this.recordedTemperature = recordedTemperature;
        this.notes = notes;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Double getRecordedTemperature() {
        return recordedTemperature;
    }

    public void setRecordedTemperature(Double recordedTemperature) {
        this.recordedTemperature = recordedTemperature;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
