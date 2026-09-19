package com.pharmacy.supplychain.service.impl;

import com.pharmacy.supplychain.dto.TemperatureLogRequest;
import com.pharmacy.supplychain.entity.Batch;
import com.pharmacy.supplychain.entity.ComplianceStatus;
import com.pharmacy.supplychain.entity.Medicine;
import com.pharmacy.supplychain.entity.TemperatureLog;
import com.pharmacy.supplychain.exception.ResourceNotFoundException;
import com.pharmacy.supplychain.repository.BatchRepository;
import com.pharmacy.supplychain.repository.TemperatureLogRepository;
import com.pharmacy.supplychain.service.TemperatureLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for cold-chain temperature compliance monitoring.
 * Compares actual temperature sensor reading against the medicine's specification:
 * - minTemperature <= reading <= maxTemperature => COMPLIANT
 * - reading < minTemperature OR reading > maxTemperature => OUT_OF_RANGE
 */
@Service
@Transactional
public class TemperatureLogServiceImpl implements TemperatureLogService {

    private final TemperatureLogRepository temperatureLogRepository;
    private final BatchRepository batchRepository;

    public TemperatureLogServiceImpl(TemperatureLogRepository temperatureLogRepository,
                                     BatchRepository batchRepository) {
        this.temperatureLogRepository = temperatureLogRepository;
        this.batchRepository = batchRepository;
    }

    @Override
    public TemperatureLog recordTemperature(TemperatureLogRequest request) {
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with ID: " + request.getBatchId()));

        Medicine medicine = batch.getMedicine();
        Double minTemp = medicine.getMinTemperature();
        Double maxTemp = medicine.getMaxTemperature();
        Double actualTemp = request.getRecordedTemperature();

        // Evaluate cold-chain compliance
        ComplianceStatus status;
        if (actualTemp >= minTemp && actualTemp <= maxTemp) {
            status = ComplianceStatus.COMPLIANT;
        } else {
            status = ComplianceStatus.OUT_OF_RANGE;
        }

        String notes = request.getNotes();
        if (notes == null || notes.trim().isEmpty()) {
            if (status == ComplianceStatus.COMPLIANT) {
                notes = String.format("Temperature compliant within acceptable range (%.1f°C to %.1f°C).", minTemp, maxTemp);
            } else {
                notes = String.format("ALERT: Cold-chain violation! Reading %.1f°C is outside range [%.1f°C - %.1f°C].", actualTemp, minTemp, maxTemp);
            }
        }

        TemperatureLog log = new TemperatureLog(
                batch,
                actualTemp,
                LocalDateTime.now(),
                status,
                notes
        );

        return temperatureLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemperatureLog> getAllLogs() {
        return temperatureLogRepository.findTop15ByOrderByRecordedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemperatureLog> getLogsForBatch(Long batchId) {
        return temperatureLogRepository.findByBatchIdOrderByRecordedAtDesc(batchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemperatureLog> getRecentAlerts() {
        return temperatureLogRepository.findByComplianceStatusOrderByRecordedAtDesc(ComplianceStatus.OUT_OF_RANGE);
    }

    @Override
    @Transactional(readOnly = true)
    public long countBreaches() {
        return temperatureLogRepository.countByComplianceStatus(ComplianceStatus.OUT_OF_RANGE);
    }
}
