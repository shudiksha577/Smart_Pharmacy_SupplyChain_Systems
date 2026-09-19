package com.pharmacy.supplychain.dto;

import jakarta.validation.constraints.NotBlank;

public class BatchVerificationRequest {

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    /**
     * Action to execute on the batch: "VERIFY" or "REJECT".
     * If blank/null, this request is simply a status query / check.
     */
    private String action; // e.g., "VERIFY", "REJECT"

    private String notes;

    public BatchVerificationRequest() {
    }

    public BatchVerificationRequest(String batchNumber, String action, String notes) {
        this.batchNumber = batchNumber;
        this.action = action;
        this.notes = notes;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
