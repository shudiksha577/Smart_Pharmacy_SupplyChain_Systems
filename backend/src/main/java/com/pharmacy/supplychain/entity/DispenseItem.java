package com.pharmacy.supplychain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Line item tracking the individual batch deducted during an FEFO dispense transaction.
 */
@Entity
@Table(name = "dispense_items")
public class DispenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispense_record_id", nullable = false)
    @JsonIgnore
    private DispenseRecord dispenseRecord;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Batch batch;

    @Column(name = "batch_number", nullable = false, length = 50)
    private String batchNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "quantity_dispensed", nullable = false)
    private Integer quantityDispensed;

    @Column(name = "remaining_batch_stock", nullable = false)
    private Integer remainingBatchStock;

    public DispenseItem() {
    }

    public DispenseItem(Batch batch, String batchNumber, LocalDate expiryDate,
                        Integer quantityDispensed, Integer remainingBatchStock) {
        this.batch = batch;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.quantityDispensed = quantityDispensed;
        this.remainingBatchStock = remainingBatchStock;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DispenseRecord getDispenseRecord() {
        return dispenseRecord;
    }

    public void setDispenseRecord(DispenseRecord dispenseRecord) {
        this.dispenseRecord = dispenseRecord;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
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
