package com.pharmacy.supplychain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class BatchRequestDTO {

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotNull(message = "Manufacturing date is required")
    private LocalDate manufacturingDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotBlank(message = "Storage requirement is required")
    private String storageRequirement;

    public BatchRequestDTO() {
    }

    // Getters and Setters

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStorageRequirement() {
        return storageRequirement;
    }

    public void setStorageRequirement(String storageRequirement) {
        this.storageRequirement = storageRequirement;
    }
}
