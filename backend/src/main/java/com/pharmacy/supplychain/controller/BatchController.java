package com.pharmacy.supplychain.controller;

import com.pharmacy.supplychain.dto.ApiResponse;
import com.pharmacy.supplychain.dto.BatchVerificationRequest;
import com.pharmacy.supplychain.dto.BatchVerificationResponse;
import com.pharmacy.supplychain.dto.ExpiryAlertDTO;
import com.pharmacy.supplychain.entity.Batch;
import com.pharmacy.supplychain.entity.VerificationStatus;
import com.pharmacy.supplychain.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@CrossOrigin(origins = "*")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Batch>>> getAllBatches() {
        List<Batch> batches = batchService.getAllBatches();
        return ResponseEntity.ok(ApiResponse.success(batches, "Batches fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Batch>> getBatchById(@PathVariable Long id) {
        Batch batch = batchService.getBatchById(id);
        return ResponseEntity.ok(ApiResponse.success(batch, "Batch fetched successfully"));
    }

    @GetMapping("/number/{batchNumber}")
    public ResponseEntity<ApiResponse<Batch>> getBatchByNumber(@PathVariable String batchNumber) {
        Batch batch = batchService.getBatchByNumber(batchNumber);
        return ResponseEntity.ok(ApiResponse.success(batch, "Batch fetched successfully"));
    }

    @GetMapping("/medicine/{medicineId}/available")
    public ResponseEntity<ApiResponse<List<Batch>>> getAvailableBatchesForMedicine(@PathVariable Long medicineId) {
        List<Batch> batches = batchService.getAvailableBatchesForMedicine(medicineId);
        return ResponseEntity.ok(ApiResponse.success(batches, "Available FEFO batches fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Batch>> createBatch(@Valid @RequestBody com.pharmacy.supplychain.dto.BatchRequestDTO request) {
        Batch created = batchService.createBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Medicine batch registered successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Batch>> updateBatch(@PathVariable Long id,
                                                          @Valid @RequestBody com.pharmacy.supplychain.dto.BatchRequestDTO request) {
        Batch updated = batchService.updateBatch(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Medicine batch updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Medicine batch deleted successfully"));
    }

    /**
     * Module 6 - Batch Verification & Authenticity Check
     * Accepts batch number and optional action (VERIFY/REJECT).
     * Returns: VERIFIED, UNVERIFIED, REJECTED, or NOT FOUND.
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<BatchVerificationResponse>> verifyBatch(
            @Valid @RequestBody BatchVerificationRequest request) {
        BatchVerificationResponse response = batchService.verifyBatch(request);
        return ResponseEntity.ok(ApiResponse.success(response, response.getMessage()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Batch>> updateStatus(@PathVariable Long id,
                                                           @RequestParam VerificationStatus status) {
        Batch updated = batchService.updateVerificationStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Batch verification status updated to " + status));
    }

    /**
     * Module 5 - Expiry Alerts
     * Automatically calculates: 90 days, 60 days, 30 days, Expired.
     */
    @GetMapping("/expiry-alerts")
    public ResponseEntity<ApiResponse<List<ExpiryAlertDTO>>> getExpiryAlerts(
            @RequestParam(required = false) String category) {
        List<ExpiryAlertDTO> alerts = batchService.getExpiryAlerts(category);
        return ResponseEntity.ok(ApiResponse.success(alerts, "Expiry alerts fetched successfully"));
    }
}
