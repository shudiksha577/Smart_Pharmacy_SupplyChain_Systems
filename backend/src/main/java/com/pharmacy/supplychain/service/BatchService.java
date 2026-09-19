package com.pharmacy.supplychain.service;

import com.pharmacy.supplychain.dto.BatchRequestDTO;
import com.pharmacy.supplychain.dto.BatchVerificationRequest;
import com.pharmacy.supplychain.dto.BatchVerificationResponse;
import com.pharmacy.supplychain.dto.ExpiryAlertDTO;
import com.pharmacy.supplychain.entity.Batch;
import com.pharmacy.supplychain.entity.VerificationStatus;

import java.util.List;

public interface BatchService {
    List<Batch> getAllBatches();
    Batch getBatchById(Long id);
    Batch getBatchByNumber(String batchNumber);
    Batch createBatch(Batch batch, Long medicineId, Long supplierId);
    Batch createBatch(BatchRequestDTO dto);
    Batch updateBatch(Long id, Batch batchDetails, Long medicineId, Long supplierId);
    Batch updateBatch(Long id, BatchRequestDTO dto);
    void deleteBatch(Long id);

    // Verification Workflow
    BatchVerificationResponse verifyBatch(BatchVerificationRequest request);
    Batch updateVerificationStatus(Long batchId, VerificationStatus status);

    // Expiry Monitoring & Alerts
    List<ExpiryAlertDTO> getExpiryAlerts(String categoryFilter);
    List<Batch> getAvailableBatchesForMedicine(Long medicineId);
}
