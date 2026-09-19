package com.pharmacy.supplychain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DispenseResponse {

    private Long recordId;
    private Long medicineId;
    private String medicineName;
    private Integer totalRequestedQuantity;
    private Integer totalDispensedQuantity;
    private String dispensedTo;
    private LocalDateTime dispensedAt;
    private String notes;
    private List<DispenseItemDTO> dispensedBatches = new ArrayList<>();

    public DispenseResponse() {
    }

    // Getters and Setters

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getTotalRequestedQuantity() {
        return totalRequestedQuantity;
    }

    public void setTotalRequestedQuantity(Integer totalRequestedQuantity) {
        this.totalRequestedQuantity = totalRequestedQuantity;
    }

    public Integer getTotalDispensedQuantity() {
        return totalDispensedQuantity;
    }

    public void setTotalDispensedQuantity(Integer totalDispensedQuantity) {
        this.totalDispensedQuantity = totalDispensedQuantity;
    }

    public String getDispensedTo() {
        return dispensedTo;
    }

    public void setDispensedTo(String dispensedTo) {
        this.dispensedTo = dispensedTo;
    }

    public LocalDateTime getDispensedAt() {
        return dispensedAt;
    }

    public void setDispensedAt(LocalDateTime dispensedAt) {
        this.dispensedAt = dispensedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<DispenseItemDTO> getDispensedBatches() {
        return dispensedBatches;
    }

    public void setDispensedBatches(List<DispenseItemDTO> dispensedBatches) {
        this.dispensedBatches = dispensedBatches;
    }
}
