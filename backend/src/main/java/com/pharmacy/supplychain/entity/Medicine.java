package com.pharmacy.supplychain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entity representing a pharmaceutical drug or formulation catalog item.
 * Includes cold-chain temperature thresholds for automated compliance evaluation.
 */
@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Medicine brand name is required")
    @Size(min = 2, max = 100, message = "Medicine name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Generic name is required")
    @Size(min = 2, max = 100, message = "Generic name must be between 2 and 100 characters")
    @Column(name = "generic_name", nullable = false, length = 100)
    private String genericName;

    @NotBlank(message = "Category is required")
    @Column(nullable = false, length = 50)
    private String category; // e.g. Antibiotics, Analgesic, Vaccines, Antidiabetic

    @NotBlank(message = "Dosage information is required")
    @Column(nullable = false, length = 50)
    private String dosage; // e.g. 500mg, 100 IU/ml, 250mg/5ml

    @NotNull(message = "Minimum storage temperature is required")
    @Column(name = "min_temperature", nullable = false)
    private Double minTemperature; // e.g., 2.0 °C for Insulin

    @NotNull(message = "Maximum storage temperature is required")
    @Column(name = "max_temperature", nullable = false)
    private Double maxTemperature; // e.g., 8.0 °C for Insulin

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Medicine() {
    }

    public Medicine(String name, String genericName, String category, String dosage, Double minTemperature, Double maxTemperature) {
        this.name = name;
        this.genericName = genericName;
        this.category = category;
        this.dosage = dosage;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
