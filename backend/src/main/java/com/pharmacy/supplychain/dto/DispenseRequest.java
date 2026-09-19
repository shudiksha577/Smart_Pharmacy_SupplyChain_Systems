package com.pharmacy.supplychain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DispenseRequest {

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Dispensing quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    private String dispensedTo; // e.g., "Ward 3", "OPD Patient 104"
    private String notes;

    public DispenseRequest() {
    }

    public DispenseRequest(Long medicineId, Integer quantity, String dispensedTo, String notes) {
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.dispensedTo = dispensedTo;
        this.notes = notes;
    }

    // Getters and Setters

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDispensedTo() {
        return dispensedTo;
    }

    public void setDispensedTo(String dispensedTo) {
        this.dispensedTo = dispensedTo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
