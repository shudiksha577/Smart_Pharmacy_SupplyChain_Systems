package com.pharmacy.supplychain.service.impl;

import com.pharmacy.supplychain.dto.DashboardStatsDTO;
import com.pharmacy.supplychain.dto.ExpiryAlertDTO;
import com.pharmacy.supplychain.entity.Batch;
import com.pharmacy.supplychain.entity.ComplianceStatus;
import com.pharmacy.supplychain.entity.TemperatureLog;
import com.pharmacy.supplychain.repository.BatchRepository;
import com.pharmacy.supplychain.repository.MedicineRepository;
import com.pharmacy.supplychain.repository.TemperatureLogRepository;
import com.pharmacy.supplychain.service.BatchService;
import com.pharmacy.supplychain.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final MedicineRepository medicineRepository;
    private final BatchRepository batchRepository;
    private final TemperatureLogRepository temperatureLogRepository;
    private final BatchService batchService;

    public DashboardServiceImpl(MedicineRepository medicineRepository,
                                BatchRepository batchRepository,
                                TemperatureLogRepository temperatureLogRepository,
                                BatchService batchService) {
        this.medicineRepository = medicineRepository;
        this.batchRepository = batchRepository;
        this.temperatureLogRepository = temperatureLogRepository;
        this.batchService = batchService;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        LocalDate today = LocalDate.now();

        // 1. Total Medicines
        stats.setTotalMedicines(medicineRepository.count());

        // 2. Active Batches (quantity > 0)
        List<Batch> allBatches = batchRepository.findAll();
        long activeCount = 0;
        long lowStockCount = 0;
        long expiringSoonCount = 0;
        long expiredCount = 0;

        for (Batch b : allBatches) {
            if (b.getQuantity() != null && b.getQuantity() > 0) {
                activeCount++;

                // Low stock threshold <= 20 units
                if (b.getQuantity() <= 20) {
                    lowStockCount++;
                }

                long days = ChronoUnit.DAYS.between(today, b.getExpiryDate());
                if (days < 0) {
                    expiredCount++;
                } else if (days <= 90) {
                    expiringSoonCount++;
                }
            } else if (b.getExpiryDate() != null && b.getExpiryDate().isBefore(today)) {
                expiredCount++;
            }
        }

        stats.setActiveBatches(activeCount);
        stats.setLowStockCount(lowStockCount);
        stats.setExpiringSoonCount(expiringSoonCount);
        stats.setExpiredCount(expiredCount);

        // Recent Expiry Alerts (Top 6 critical / near expiry)
        List<ExpiryAlertDTO> allAlerts = batchService.getExpiryAlerts(null);
        stats.setRecentExpiryAlerts(allAlerts.stream().limit(6).collect(Collectors.toList()));

        // Recent Temperature Alerts (Out-of-range logs)
        List<TemperatureLog> outOfRangeLogs = temperatureLogRepository
                .findByComplianceStatusOrderByRecordedAtDesc(ComplianceStatus.OUT_OF_RANGE);
        stats.setRecentTemperatureAlerts(outOfRangeLogs.stream().limit(6).collect(Collectors.toList()));

        // Recent Batches Verification Status (Top 6)
        List<Batch> recentBatches = batchRepository.findTop10ByOrderByCreatedAtDesc();
        stats.setRecentVerificationStatus(recentBatches.stream().limit(6).collect(Collectors.toList()));

        return stats;
    }
}
