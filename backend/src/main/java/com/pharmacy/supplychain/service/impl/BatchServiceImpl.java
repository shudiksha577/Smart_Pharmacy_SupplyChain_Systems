package com.pharmacy.supplychain.service.impl;

import com.pharmacy.supplychain.dto.BatchVerificationRequest;
import com.pharmacy.supplychain.dto.BatchVerificationResponse;
import com.pharmacy.supplychain.dto.ExpiryAlertDTO;
import com.pharmacy.supplychain.entity.Batch;
import com.pharmacy.supplychain.entity.Medicine;
import com.pharmacy.supplychain.entity.Supplier;
import com.pharmacy.supplychain.entity.VerificationStatus;
import com.pharmacy.supplychain.exception.DuplicateResourceException;
import com.pharmacy.supplychain.exception.ResourceNotFoundException;
import com.pharmacy.supplychain.repository.BatchRepository;
import com.pharmacy.supplychain.repository.MedicineRepository;
import com.pharmacy.supplychain.repository.SupplierRepository;
import com.pharmacy.supplychain.service.BatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;

    public BatchServiceImpl(BatchRepository batchRepository,
                            MedicineRepository medicineRepository,
                            SupplierRepository supplierRepository) {
        this.batchRepository = batchRepository;
        this.medicineRepository = medicineRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Batch getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine batch not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Batch getBatchByNumber(String batchNumber) {
        return batchRepository.findByBatchNumberIgnoreCase(batchNumber.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with number: " + batchNumber));
    }

    @Override
    public Batch createBatch(Batch batch, Long medicineId, Long supplierId) {
        String cleanBatchNum = batch.getBatchNumber().trim().toUpperCase();

        if (batchRepository.existsByBatchNumber(cleanBatchNum)) {
            throw new DuplicateResourceException("Batch number '" + cleanBatchNum + "' already exists in the system.");
        }

        if (batch.getManufacturingDate().isAfter(batch.getExpiryDate()) ||
            batch.getManufacturingDate().isEqual(batch.getExpiryDate())) {
            throw new IllegalArgumentException("Manufacturing date must be strictly before expiry date.");
        }

        if (batch.getQuantity() == null || batch.getQuantity() <= 0) {
            throw new IllegalArgumentException("Initial batch quantity must be greater than zero.");
        }

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Associated medicine not found with ID: " + medicineId));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Associated supplier not found with ID: " + supplierId));

        batch.setBatchNumber(cleanBatchNum);
        batch.setMedicine(medicine);
        batch.setSupplier(supplier);
        if (batch.getVerificationStatus() == null) {
            batch.setVerificationStatus(VerificationStatus.UNVERIFIED);
        }

        return batchRepository.save(batch);
    }

    @Override
    public Batch createBatch(com.pharmacy.supplychain.dto.BatchRequestDTO dto) {
        Batch batch = new Batch();
        batch.setBatchNumber(dto.getBatchNumber());
        batch.setManufacturer(dto.getManufacturer());
        batch.setManufacturingDate(dto.getManufacturingDate());
        batch.setExpiryDate(dto.getExpiryDate());
        batch.setQuantity(dto.getQuantity());
        batch.setStorageRequirement(dto.getStorageRequirement());
        batch.setVerificationStatus(VerificationStatus.UNVERIFIED);
        return createBatch(batch, dto.getMedicineId(), dto.getSupplierId());
    }

    @Override
    public Batch updateBatch(Long id, com.pharmacy.supplychain.dto.BatchRequestDTO dto) {
        Batch batch = new Batch();
        batch.setBatchNumber(dto.getBatchNumber());
        batch.setManufacturer(dto.getManufacturer());
        batch.setManufacturingDate(dto.getManufacturingDate());
        batch.setExpiryDate(dto.getExpiryDate());
        batch.setQuantity(dto.getQuantity());
        batch.setStorageRequirement(dto.getStorageRequirement());
        return updateBatch(id, batch, dto.getMedicineId(), dto.getSupplierId());
    }

    @Override
    public Batch updateBatch(Long id, Batch batchDetails, Long medicineId, Long supplierId) {
        Batch existing = getBatchById(id);
        String cleanBatchNum = batchDetails.getBatchNumber().trim().toUpperCase();

        if (!existing.getBatchNumber().equalsIgnoreCase(cleanBatchNum) &&
                batchRepository.existsByBatchNumber(cleanBatchNum)) {
            throw new DuplicateResourceException("Another batch with number '" + cleanBatchNum + "' already exists.");
        }

        if (batchDetails.getManufacturingDate().isAfter(batchDetails.getExpiryDate()) ||
            batchDetails.getManufacturingDate().isEqual(batchDetails.getExpiryDate())) {
            throw new IllegalArgumentException("Manufacturing date must be strictly before expiry date.");
        }

        if (batchDetails.getQuantity() == null || batchDetails.getQuantity() < 0) {
            throw new IllegalArgumentException("Batch quantity cannot be negative.");
        }

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medicineId));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + supplierId));

        existing.setBatchNumber(cleanBatchNum);
        existing.setMedicine(medicine);
        existing.setSupplier(supplier);
        existing.setManufacturer(batchDetails.getManufacturer().trim());
        existing.setManufacturingDate(batchDetails.getManufacturingDate());
        existing.setExpiryDate(batchDetails.getExpiryDate());
        existing.setQuantity(batchDetails.getQuantity());
        existing.setStorageRequirement(batchDetails.getStorageRequirement().trim());
        if (batchDetails.getVerificationStatus() != null) {
            existing.setVerificationStatus(batchDetails.getVerificationStatus());
        }

        return batchRepository.save(existing);
    }

    @Override
    public void deleteBatch(Long id) {
        Batch existing = getBatchById(id);
        batchRepository.delete(existing);
    }

    @Override
    public BatchVerificationResponse verifyBatch(BatchVerificationRequest request) {
        String cleanBatchNum = request.getBatchNumber().trim();
        Optional<Batch> optionalBatch = batchRepository.findByBatchNumberIgnoreCase(cleanBatchNum);

        if (optionalBatch.isEmpty()) {
            return BatchVerificationResponse.notFound(cleanBatchNum);
        }

        Batch batch = optionalBatch.get();

        // If an explicit action is supplied (VERIFY or REJECT), perform the state transition
        if (request.getAction() != null && !request.getAction().trim().isEmpty()) {
            String action = request.getAction().trim().toUpperCase();
            if ("VERIFY".equals(action)) {
                batch.setVerificationStatus(VerificationStatus.VERIFIED);
                batchRepository.save(batch);
            } else if ("REJECT".equals(action)) {
                batch.setVerificationStatus(VerificationStatus.REJECTED);
                batchRepository.save(batch);
            } else {
                throw new IllegalArgumentException("Invalid verification action. Allowed actions: VERIFY, REJECT");
            }
        }

        BatchVerificationResponse response = new BatchVerificationResponse();
        response.setBatchNumber(batch.getBatchNumber());
        response.setStatus(batch.getVerificationStatus().name());
        response.setExists(true);
        response.setBatchId(batch.getId());
        response.setMedicineName(batch.getMedicine().getName());
        response.setManufacturer(batch.getManufacturer());
        response.setSupplierName(batch.getSupplier().getSupplierName());
        response.setExpiryDate(batch.getExpiryDate());
        response.setCurrentQuantity(batch.getQuantity());

        if (VerificationStatus.VERIFIED.equals(batch.getVerificationStatus())) {
            response.setMessage("Batch verified authentic. Approved for FEFO dispensing.");
        } else if (VerificationStatus.REJECTED.equals(batch.getVerificationStatus())) {
            response.setMessage("WARNING: Batch is marked as REJECTED/SUSPECT. Dispensing is prohibited.");
        } else {
            response.setMessage("Batch is currently UNVERIFIED. Requires inspection prior to dispensing.");
        }

        return response;
    }

    @Override
    public Batch updateVerificationStatus(Long batchId, VerificationStatus status) {
        Batch batch = getBatchById(batchId);
        batch.setVerificationStatus(status);
        return batchRepository.save(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpiryAlertDTO> getExpiryAlerts(String categoryFilter) {
        List<Batch> allBatches = batchRepository.findAll();
        LocalDate today = LocalDate.now();
        List<ExpiryAlertDTO> alerts = new ArrayList<>();

        for (Batch b : allBatches) {
            // Only report alerts for batches that have remaining physical stock
            if (b.getQuantity() == null || b.getQuantity() <= 0) {
                continue;
            }

            long daysRemaining = ChronoUnit.DAYS.between(today, b.getExpiryDate());
            String alertCat;

            if (daysRemaining < 0) {
                alertCat = "EXPIRED";
            } else if (daysRemaining <= 30) {
                alertCat = "CRITICAL_30";
            } else if (daysRemaining <= 60) {
                alertCat = "WARNING_60";
            } else if (daysRemaining <= 90) {
                alertCat = "NOTICE_90";
            } else {
                alertCat = "SAFE";
            }

            // Apply filter if specified
            if (categoryFilter != null && !categoryFilter.trim().isEmpty() && !categoryFilter.equalsIgnoreCase("ALL")) {
                if (!alertCat.equalsIgnoreCase(categoryFilter.trim())) {
                    continue;
                }
            } else {
                // If no specific filter, exclude SAFE batches from alert radar
                if ("SAFE".equals(alertCat)) {
                    continue;
                }
            }

            ExpiryAlertDTO dto = new ExpiryAlertDTO(
                    b.getId(),
                    b.getBatchNumber(),
                    b.getMedicine().getId(),
                    b.getMedicine().getName(),
                    b.getMedicine().getGenericName(),
                    b.getMedicine().getCategory(),
                    b.getQuantity(),
                    b.getExpiryDate(),
                    daysRemaining,
                    alertCat,
                    b.getStorageRequirement(),
                    b.getVerificationStatus().name()
            );
            alerts.add(dto);
        }

        // Sort by daysRemaining ascending (earliest expiry first)
        alerts.sort((a, b) -> Long.compare(a.getDaysRemaining(), b.getDaysRemaining()));
        return alerts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Batch> getAvailableBatchesForMedicine(Long medicineId) {
        return batchRepository.findAvailableBatchesForFefo(medicineId, VerificationStatus.VERIFIED, LocalDate.now());
    }
}
