package com.pharmacy.supplychain.dto;

import com.pharmacy.supplychain.entity.Batch;
import com.pharmacy.supplychain.entity.TemperatureLog;
import java.util.ArrayList;
import java.util.List;

public class DashboardStatsDTO {

    private long totalMedicines;
    private long activeBatches;
    private long lowStockCount;
    private long expiringSoonCount; // within 90 days
    private long expiredCount;

    // Recent tables required by dashboard specification
    private List<ExpiryAlertDTO> recentExpiryAlerts = new ArrayList<>();
    private List<TemperatureLog> recentTemperatureAlerts = new ArrayList<>();
    private List<Batch> recentVerificationStatus = new ArrayList<>();

    public DashboardStatsDTO() {
    }

    // Getters and Setters

    public long getTotalMedicines() {
        return totalMedicines;
    }

    public void setTotalMedicines(long totalMedicines) {
        this.totalMedicines = totalMedicines;
    }

    public long getActiveBatches() {
        return activeBatches;
    }

    public void setActiveBatches(long activeBatches) {
        this.activeBatches = activeBatches;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public long getExpiringSoonCount() {
        return expiringSoonCount;
    }

    public void setExpiringSoonCount(long expiringSoonCount) {
        this.expiringSoonCount = expiringSoonCount;
    }

    public long getExpiredCount() {
        return expiredCount;
    }

    public void setExpiredCount(long expiredCount) {
        this.expiredCount = expiredCount;
    }

    public List<ExpiryAlertDTO> getRecentExpiryAlerts() {
        return recentExpiryAlerts;
    }

    public void setRecentExpiryAlerts(List<ExpiryAlertDTO> recentExpiryAlerts) {
        this.recentExpiryAlerts = recentExpiryAlerts;
    }

    public List<TemperatureLog> getRecentTemperatureAlerts() {
        return recentTemperatureAlerts;
    }

    public void setRecentTemperatureAlerts(List<TemperatureLog> recentTemperatureAlerts) {
        this.recentTemperatureAlerts = recentTemperatureAlerts;
    }

    public List<Batch> getRecentVerificationStatus() {
        return recentVerificationStatus;
    }

    public void setRecentVerificationStatus(List<Batch> recentVerificationStatus) {
        this.recentVerificationStatus = recentVerificationStatus;
    }
}
