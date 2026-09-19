package com.pharmacy.supplychain.service.impl;

import com.pharmacy.supplychain.dto.DispenseItemDTO;
import com.pharmacy.supplychain.dto.DispenseRequest;
import com.pharmacy.supplychain.dto.DispenseResponse;
import com.pharmacy.supplychain.entity.*;
import com.pharmacy.supplychain.exception.InsufficientStockException;
import com.pharmacy.supplychain.exception.ResourceNotFoundException;
import com.pharmacy.supplychain.repository.BatchRepository;
import com.pharmacy.supplychain.repository.DispenseRecordRepository;
import com.pharmacy.supplychain.repository.MedicineRepository;
import com.pharmacy.supplychain.service.DispenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements FEFO (First Expired First Out) Pharmaceutical Dispensing.
 * Core Academic Business Logic:
 * 1. Filter only verified, active batches with non-zero stock.
 * 2. Exclude expired and rejected batches.
 * 3. Sort batches in ascending order of expiry date.
 * 4. Fulfill requested order iteratively from earliest expiring batch.
 * 5. Handle batch splits across multiple batches if first batch quantity is insufficient.
 * 6. Deduct inventory and persist auditable transaction record.
 */
@Service
@Transactional
public class DispenseServiceImpl implements DispenseService {

    private final MedicineRepository medicineRepository;
    private final BatchRepository batchRepository;
    private final DispenseRecordRepository dispenseRecordRepository;

    public DispenseServiceImpl(MedicineRepository medicineRepository,
                               BatchRepository batchRepository,
                               DispenseRecordRepository dispenseRecordRepository) {
        this.medicineRepository = medicineRepository;
        this.batchRepository = batchRepository;
        this.dispenseRecordRepository = dispenseRecordRepository;
    }

    @Override
    public DispenseResponse dispenseMedicine(DispenseRequest request) {
        Long medicineId = request.getMedicineId();
        Integer requestedQty = request.getQuantity();

        if (requestedQty == null || requestedQty <= 0) {
            throw new IllegalArgumentException("Requested dispensing quantity must be strictly greater than zero.");
        }

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medicineId));

        // Step 1: Query eligible batches ordered by expiry date ASC (First Expired First Out)
        LocalDate today = LocalDate.now();
        List<Batch> eligibleBatches = batchRepository.findAvailableBatchesForFefo(
                medicineId, VerificationStatus.VERIFIED, today);

        // Step 2: Calculate total stock available across all verified, unexpired batches
        int totalAvailableStock = 0;
        for (Batch b : eligibleBatches) {
            totalAvailableStock += (b.getQuantity() != null ? b.getQuantity() : 0);
        }

        // Step 3: Validate stock sufficiency
        if (totalAvailableStock < requestedQty) {
            throw new InsufficientStockException(
                    "Insufficient verified stock for '" + medicine.getName() + "'. " +
                    "Requested: " + requestedQty + ", Available Verified Stock: " + totalAvailableStock + ". " +
                    "Dispense aborted to prevent inventory inconsistency."
            );
        }

        // Step 4: Initialize Dispense Record
        DispenseRecord record = new DispenseRecord();
        record.setMedicine(medicine);
        record.setTotalRequestedQuantity(requestedQty);
        record.setTotalDispensedQuantity(requestedQty);
        record.setDispensedTo(request.getDispensedTo() != null ? request.getDispensedTo().trim() : "General Pharmacy Outpatient");
        record.setNotes(request.getNotes());

        List<DispenseItemDTO> itemDTOs = new ArrayList<>();
        int remainingToDispense = requestedQty;

        // Step 5: FEFO Iterative Allocation
        for (Batch batch : eligibleBatches) {
            if (remainingToDispense <= 0) {
                break;
            }

            int availableInBatch = batch.getQuantity();
            int deductFromThisBatch = Math.min(availableInBatch, remainingToDispense);

            // Deduct batch quantity
            int updatedBatchStock = availableInBatch - deductFromThisBatch;
            batch.setQuantity(updatedBatchStock);
            batchRepository.save(batch);

            // Create line item record
            DispenseItem item = new DispenseItem(
                    batch,
                    batch.getBatchNumber(),
                    batch.getExpiryDate(),
                    deductFromThisBatch,
                    updatedBatchStock
            );
            record.addItem(item);

            // Add DTO summary
            itemDTOs.add(new DispenseItemDTO(
                    batch.getId(),
                    batch.getBatchNumber(),
                    batch.getExpiryDate(),
                    deductFromThisBatch,
                    updatedBatchStock
            ));

            remainingToDispense -= deductFromThisBatch;
        }

        DispenseRecord savedRecord = dispenseRecordRepository.save(record);

        // Step 6: Construct Response DTO
        DispenseResponse response = new DispenseResponse();
        response.setRecordId(savedRecord.getId());
        response.setMedicineId(medicine.getId());
        response.setMedicineName(medicine.getName());
        response.setTotalRequestedQuantity(requestedQty);
        response.setTotalDispensedQuantity(requestedQty);
        response.setDispensedTo(savedRecord.getDispensedTo());
        response.setDispensedAt(savedRecord.getDispensedAt());
        response.setNotes(savedRecord.getNotes());
        response.setDispensedBatches(itemDTOs);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DispenseRecord> getAllDispenseRecords() {
        return dispenseRecordRepository.findAllByOrderByDispensedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public DispenseRecord getDispenseRecordById(Long id) {
        return dispenseRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispense record not found with ID: " + id));
    }
}
