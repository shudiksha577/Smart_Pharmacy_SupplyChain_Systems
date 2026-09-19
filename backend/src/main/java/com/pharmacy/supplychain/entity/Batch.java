package com.pharmacy.supplychain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a discrete physical manufacturing batch of medicines.
 * Batch number is strictly unique.
 * Tracks verification status and expiry date used by the FEFO algorithm.
 */
@Entity
@Table(name = "batches", indexes = {
        @Index(name = "idx_batch_number", columnList = "batch_number", unique = true),
        @Index(name = "idx_expiry_date", columnList = "expiry_date")
})
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Batch number is required")
    @Column(name = "batch_number", nullable = false, unique = true, length = 50)
    private String batchNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicine_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Supplier supplier;

    @NotBlank(message = "Manufacturer name is required")
    @Column(nullable = false, length = 100)
    private String manufacturer;

    @NotNull(message = "Manufacturing date is required")
    @Column(name = "manufacturing_date", nullable = false)
    private LocalDate manufacturingDate;

    @NotNull(message = "Expiry date is required")
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank(message = "Storage requirement is required")
    @Column(name = "storage_requirement", nullable = false, length = 100)
    private String storageRequirement; // e.g. "Cold Storage (2-8°C)", "Room Temperature (15-25°C)"

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 20)
    private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Batch() {
    }

    public Batch(String batchNumber, Medicine medicine, Supplier supplier, String manufacturer,
                 LocalDate manufacturingDate, LocalDate expiryDate, Integer quantity,
                 String storageRequirement, VerificationStatus verificationStatus) {
        this.batchNumber = batchNumber;
        this.medicine = medicine;
        this.supplier = supplier;
        this.manufacturer = manufacturer;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.storageRequirement = storageRequirement;
        this.verificationStatus = verificationStatus != null ? verificationStatus : VerificationStatus.UNVERIFIED;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.verificationStatus == null) {
            this.verificationStatus = VerificationStatus.UNVERIFIED;
        }
    }

    // Helper methods
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public boolean isVerified() {
        return VerificationStatus.VERIFIED.equals(this.verificationStatus);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
