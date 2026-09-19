package com.pharmacy.supplychain.service;

import com.pharmacy.supplychain.dto.TemperatureLogRequest;
import com.pharmacy.supplychain.entity.ComplianceStatus;
import com.pharmacy.supplychain.entity.TemperatureLog;

import java.util.List;

public interface TemperatureLogService {
    TemperatureLog recordTemperature(TemperatureLogRequest request);
    List<TemperatureLog> getAllLogs();
    List<TemperatureLog> getLogsForBatch(Long batchId);
    List<TemperatureLog> getRecentAlerts();
    long countBreaches();
}
