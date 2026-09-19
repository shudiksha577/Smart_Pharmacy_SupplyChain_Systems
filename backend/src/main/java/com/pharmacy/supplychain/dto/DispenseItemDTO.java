package com.pharmacy.supplychain.dto;

import java.time.LocalDate;

public class DispenseItemDTO {
    private Long batchId;
    private String batchNumber;
    private LocalDate expiryDate;
    private Integer quantityDispensed;
    private Integer remainingBatchStock;

    public DispenseItemDTO() {
    }

    public DispenseItemDTO(Long batchId, String batchNumber, LocalDate expiryDate,
                           Integer quantityDispensed, Integer remainingBatchStock) {
        this.batchId = batchId;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.quantityDispensed = quantityDispensed;
        this.remainingBatchStock = remainingBatchStock;
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

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getQuantityDispensed() {
        return quantityDispensed;
    }

    public void setQuantityDispensed(Integer quantityDispensed) {
        this.quantityDispensed = quantityDispensed;
    }

    public Integer getRemainingBatchStock() {
        return remainingBatchStock;
    }

    public void setRemainingBatchStock(Integer remainingBatchStock) {
        this.remainingBatchStock = remainingBatchStock;
    }
}
