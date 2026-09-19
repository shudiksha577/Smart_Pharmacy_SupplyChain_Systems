package com.pharmacy.supplychain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an official dispensing event in the hospital pharmacy.
 * Enforces FEFO dispensing audit history.
 */
@Entity
@Table(name = "dispense_records")
public class DispenseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicine_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Medicine medicine;

    @Column(name = "total_requested_quantity", nullable = false)
    private Integer totalRequestedQuantity;

    @Column(name = "total_dispensed_quantity", nullable = false)
    private Integer totalDispensedQuantity;

    @Column(name = "dispensed_to", length = 100)
    private String dispensedTo; // e.g. "ICU Ward", "Emergency OPD", "Patient #291"

    @Column(name = "dispensed_at", nullable = false)
    private LocalDateTime dispensedAt;

    @Column(length = 255)
    private String notes;

    @OneToMany(mappedBy = "dispenseRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DispenseItem> items = new ArrayList<>();

    public DispenseRecord() {
    }

    public DispenseRecord(Medicine medicine, Integer totalRequestedQuantity, Integer totalDispensedQuantity,
                          String dispensedTo, String notes) {
        this.medicine = medicine;
        this.totalRequestedQuantity = totalRequestedQuantity;
        this.totalDispensedQuantity = totalDispensedQuantity;
        this.dispensedTo = dispensedTo;
        this.notes = notes;
        this.dispensedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.dispensedAt == null) {
            this.dispensedAt = LocalDateTime.now();
        }
    }

    public void addItem(DispenseItem item) {
        items.add(item);
        item.setDispenseRecord(this);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
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

    public List<DispenseItem> getItems() {
        return items;
    }

    public void setItems(List<DispenseItem> items) {
        this.items = items;
    }
}
