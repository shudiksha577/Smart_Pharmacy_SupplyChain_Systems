package com.pharmacy.supplychain.dto;

import java.time.LocalDate;

public class BatchVerificationResponse {

    private String batchNumber;
    private String status; // VERIFIED, UNVERIFIED, REJECTED, NOT FOUND
    private boolean exists;
    private Long batchId;
    private String medicineName;
    private String manufacturer;
    private String supplierName;
    private LocalDate expiryDate;
    private Integer currentQuantity;
    private String message;

    public BatchVerificationResponse() {
    }

    public static BatchVerificationResponse notFound(String batchNumber) {
        BatchVerificationResponse res = new BatchVerificationResponse();
        res.setBatchNumber(batchNumber);
        res.setStatus("NOT FOUND");
        res.setExists(false);
        res.setMessage("Batch number '" + batchNumber + "' does not exist in the hospital pharmacy database.");
        return res;
    }

    // Getters and Setters

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
