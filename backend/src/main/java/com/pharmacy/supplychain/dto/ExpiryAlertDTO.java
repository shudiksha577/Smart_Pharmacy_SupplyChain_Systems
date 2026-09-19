package com.pharmacy.supplychain.dto;

import java.time.LocalDate;

public class ExpiryAlertDTO {

    private Long batchId;
    private String batchNumber;
    private Long medicineId;
    private String medicineName;
    private String genericName;
    private String category;
    private Integer quantity;
    private LocalDate expiryDate;
    private long daysRemaining;
    private String alertCategory; // "EXPIRED", "CRITICAL_30", "WARNING_60", "NOTICE_90", "SAFE"
    private String storageRequirement;
    private String verificationStatus;

    public ExpiryAlertDTO() {
    }

    public ExpiryAlertDTO(Long batchId, String batchNumber, Long medicineId, String medicineName,
                          String genericName, String category, Integer quantity, LocalDate expiryDate,
                          long daysRemaining, String alertCategory, String storageRequirement,
                          String verificationStatus) {
        this.batchId = batchId;
        this.batchNumber = batchNumber;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.genericName = genericName;
        this.category = category;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.daysRemaining = daysRemaining;
        this.alertCategory = alertCategory;
        this.storageRequirement = storageRequirement;
        this.verificationStatus = verificationStatus;
    }

    // Getters and Setters

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

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

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public String getAlertCategory() {
        return alertCategory;
    }

    public void setAlertCategory(String alertCategory) {
        this.alertCategory = alertCategory;
    }

    public String getStorageRequirement() {
        return storageRequirement;
    }

    public void setStorageRequirement(String storageRequirement) {
        this.storageRequirement = storageRequirement;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
