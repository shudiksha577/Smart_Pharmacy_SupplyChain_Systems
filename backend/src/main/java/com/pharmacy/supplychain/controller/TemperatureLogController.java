package com.pharmacy.supplychain.controller;

import com.pharmacy.supplychain.dto.ApiResponse;
import com.pharmacy.supplychain.dto.TemperatureLogRequest;
import com.pharmacy.supplychain.entity.TemperatureLog;
import com.pharmacy.supplychain.service.TemperatureLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temperature-logs")
@CrossOrigin(origins = "*")
public class TemperatureLogController {

    private final TemperatureLogService temperatureLogService;

    public TemperatureLogController(TemperatureLogService temperatureLogService) {
        this.temperatureLogService = temperatureLogService;
    }

    /**
     * Module 7 - Temperature Compliance Recording
     * Evaluates temperature compliance against the medicine's allowed range.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TemperatureLog>> recordTemperature(
            @Valid @RequestBody TemperatureLogRequest request) {
        TemperatureLog logged = temperatureLogService.recordTemperature(request);
        String msg = logged.getComplianceStatus().name() + ": " + logged.getNotes();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(logged, msg));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TemperatureLog>>> getAllLogs() {
        List<TemperatureLog> logs = temperatureLogService.getAllLogs();
        return ResponseEntity.ok(ApiResponse.success(logs, "Temperature logs fetched successfully"));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<ApiResponse<List<TemperatureLog>>> getLogsForBatch(@PathVariable Long batchId) {
        List<TemperatureLog> logs = temperatureLogService.getLogsForBatch(batchId);
        return ResponseEntity.ok(ApiResponse.success(logs, "Temperature history for batch fetched successfully"));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<TemperatureLog>>> getRecentAlerts() {
        List<TemperatureLog> breaches = temperatureLogService.getRecentAlerts();
        return ResponseEntity.ok(ApiResponse.success(breaches, "Cold-chain violation alerts fetched successfully"));
    }
}
