package com.pharmacy.supplychain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entity storing periodic cold-chain sensor reading audits for a batch.
 * Evaluates whether temperature stays within the allowed thresholds of the medicine.
 */
@Entity
@Table(name = "temperature_logs")
public class TemperatureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Associated batch is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Batch batch;

    @NotNull(message = "Recorded temperature is required")
    @Column(name = "recorded_temperature", nullable = false)
    private Double recordedTemperature;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "compliance_status", nullable = false, length = 20)
    private ComplianceStatus complianceStatus;

    @Column(length = 255)
    private String notes;

    public TemperatureLog() {
    }

    public TemperatureLog(Batch batch, Double recordedTemperature, LocalDateTime recordedAt,
                          ComplianceStatus complianceStatus, String notes) {
        this.batch = batch;
        this.recordedTemperature = recordedTemperature;
        this.recordedAt = recordedAt != null ? recordedAt : LocalDateTime.now();
        this.complianceStatus = complianceStatus;
        this.notes = notes;
    }

    @PrePersist
    protected void onCreate() {
        if (this.recordedAt == null) {
            this.recordedAt = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Double getRecordedTemperature() {
        return recordedTemperature;
    }

    public void setRecordedTemperature(Double recordedTemperature) {
        this.recordedTemperature = recordedTemperature;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public ComplianceStatus getComplianceStatus() {
        return complianceStatus;
    }

    public void setComplianceStatus(ComplianceStatus complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
