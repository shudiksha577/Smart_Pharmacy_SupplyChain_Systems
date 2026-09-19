package com.pharmacy.supplychain.config;

import com.pharmacy.supplychain.entity.*;
import com.pharmacy.supplychain.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Initializes rich, realistic pharmaceutical sample data on startup.
 * Provides immediate out-of-the-box demonstration of:
 * - FEFO dispensing (Paracetamol Batch A vs Batch B)
 * - Cold-chain temperature compliance (Insulin 2-8°C)
 * - Expiry alerts (Expired, 30 days, 60 days, 90 days)
 * - Anti-counterfeit verification (Verified, Unverified, Rejected)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;
    private final BatchRepository batchRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    public DataInitializer(MedicineRepository medicineRepository,
                           SupplierRepository supplierRepository,
                           BatchRepository batchRepository,
                           TemperatureLogRepository temperatureLogRepository) {
        this.medicineRepository = medicineRepository;
        this.supplierRepository = supplierRepository;
        this.batchRepository = batchRepository;
        this.temperatureLogRepository = temperatureLogRepository;
    }

    @Override
    public void run(String... args) {
        if (medicineRepository.count() > 0) {
            return; // Data already seeded
        }

        System.out.println("--> Seeding Initial Pharmaceutical Supply Chain Data...");

        // 1. Seed Authorized Suppliers
        Supplier s1 = supplierRepository.save(new Supplier(
                "Apex Pharma Distributors", "Rajesh Sharma", "+91 9876543210",
                "contact@apexpharma.in", "Plot 12, Industrial Area, Hyderabad"
        ));
        Supplier s2 = supplierRepository.save(new Supplier(
                "Biogen Healthcare Ltd.", "Dr. Sunita Sen", "+91 9811223344",
                "supply@biogen.com", "45 Technology Park, Bengaluru"
        ));
        Supplier s3 = supplierRepository.save(new Supplier(
                "Global Meds Supply Co.", "Vikram Mehta", "+91 9845012345",
                "support@globalmeds.in", "88 Ring Road, Mumbai"
        ));

        // 2. Seed Medicines
        Medicine paracetamol = medicineRepository.save(new Medicine(
                "Paracetamol", "Acetaminophen", "Analgesic", "500mg", 15.0, 25.0
        ));
        Medicine insulin = medicineRepository.save(new Medicine(
                "Insulin Glargine", "Insulin Glargine Recombinant", "Antidiabetic", "100 IU/ml", 2.0, 8.0
        ));
        Medicine amoxicillin = medicineRepository.save(new Medicine(
                "Amoxicillin", "Amoxicillin Trihydrate", "Antibiotics", "250mg", 15.0, 25.0
        ));
        Medicine ceftriaxone = medicineRepository.save(new Medicine(
                "Ceftriaxone Injection", "Ceftriaxone Sodium", "Antibiotics", "1g Vial", 2.0, 8.0
        ));
        Medicine atorvastatin = medicineRepository.save(new Medicine(
                "Atorvastatin", "Atorvastatin Calcium", "Cardiovascular", "20mg", 15.0, 30.0
        ));

        // 3. Seed Batches (Referencing exact scenarios from requirements)
        LocalDate today = LocalDate.now();

        // Scenario 1: FEFO Dispensing Test Batches (Paracetamol)
        // Batch A: Expires sooner (qty 20) -> should be picked first by FEFO
        Batch pcmA = batchRepository.save(new Batch(
                "BATCH-PCM-001", paracetamol, s1, "Apex Laboratories",
                today.minusMonths(6), today.plusDays(22), 20,
                "Room Temperature (15-25°C)", VerificationStatus.VERIFIED
        ));
        // Batch B: Expires later (qty 40) -> picked after Batch A is exhausted
        Batch pcmB = batchRepository.save(new Batch(
                "BATCH-PCM-002", paracetamol, s1, "Apex Laboratories",
                today.minusMonths(3), today.plusMonths(4), 40,
                "Room Temperature (15-25°C)", VerificationStatus.VERIFIED
        ));

        // Scenario 2: Batch Verification Sample (BATCH001 from prompt example)
        Batch batch001 = batchRepository.save(new Batch(
                "BATCH001", amoxicillin, s2, "Biogen Labs",
                today.minusMonths(4), today.plusMonths(8), 50,
                "Controlled Room Temp (15-25°C)", VerificationStatus.VERIFIED
        ));

        // Unverified Batch (Requires Staff Inspection)
        Batch unverifiedBatch = batchRepository.save(new Batch(
                "BATCH-AMX-999", amoxicillin, s3, "Global Meds Corp",
                today.minusMonths(1), today.plusMonths(6), 35,
                "Controlled Room Temp (15-25°C)", VerificationStatus.UNVERIFIED
        ));

        // Rejected / Counterfeit Batch (Dispensing Strictly Prohibited)
        Batch rejectedBatch = batchRepository.save(new Batch(
                "BATCH-SUSPECT-04", paracetamol, s3, "Unknown Subcontractor",
                today.minusMonths(2), today.plusMonths(5), 10,
                "Room Temperature (15-25°C)", VerificationStatus.REJECTED
        ));

        // Cold-Chain Batches (Insulin Glargine: 2°C - 8°C)
        Batch insBatch1 = batchRepository.save(new Batch(
                "BATCH-INS-101", insulin, s2, "Biogen Biotech",
                today.minusMonths(5), today.plusDays(48), 65,
                "Cold Storage (2-8°C)", VerificationStatus.VERIFIED
        ));
        Batch insBatch2 = batchRepository.save(new Batch(
                "BATCH-INS-102", insulin, s2, "Biogen Biotech",
                today.minusMonths(2), today.plusMonths(7), 120,
                "Cold Storage (2-8°C)", VerificationStatus.VERIFIED
        ));

        // Expiry Radar Scenario Batches:
        // Already Expired
        Batch expiredBatch = batchRepository.save(new Batch(
                "BATCH-EXP-001", ceftriaxone, s1, "Apex Laboratories",
                today.minusMonths(14), today.minusDays(12), 15,
                "Cold Storage (2-8°C)", VerificationStatus.VERIFIED
        ));
        // Critical <= 30 Days (e.g. 15 days left)
        Batch critBatch = batchRepository.save(new Batch(
                "BATCH-CRT-030", atorvastatin, s3, "Global Meds Corp",
                today.minusMonths(11), today.plusDays(15), 18,
                "Room Temperature (15-30°C)", VerificationStatus.VERIFIED
        ));
        // Warning <= 60 Days (e.g. 45 days left)
        Batch warnBatch = batchRepository.save(new Batch(
                "BATCH-WRN-060", amoxicillin, s1, "Apex Laboratories",
                today.minusMonths(8), today.plusDays(45), 25,
                "Controlled Room Temp", VerificationStatus.VERIFIED
        ));
        // Notice <= 90 Days (e.g. 75 days left)
        Batch notBatch = batchRepository.save(new Batch(
                "BATCH-NOT-090", atorvastatin, s2, "Biogen Healthcare",
                today.minusMonths(7), today.plusDays(75), 80,
                "Room Temperature", VerificationStatus.VERIFIED
        ));

        // 4. Seed Cold-Chain Temperature Logs (Demonstrates Compliance & Out of Range Alerts)
        // Compliant insulin log (5.0°C within 2-8°C)
        temperatureLogRepository.save(new TemperatureLog(
                insBatch1, 5.0, LocalDateTime.now().minusHours(4),
                ComplianceStatus.COMPLIANT, "Routine refrigerator audit: 5.0°C within 2.0°C - 8.0°C."
        ));
        // Out of range breach (12.0°C > 8.0°C as in prompt Example 3)
        temperatureLogRepository.save(new TemperatureLog(
                insBatch1, 12.0, LocalDateTime.now().minusHours(1),
                ComplianceStatus.OUT_OF_RANGE, "ALERT: Cold-chain violation! Reading 12.0°C is outside range [2.0°C - 8.0°C]."
        ));
        // Ceftriaxone compliant
        temperatureLogRepository.save(new TemperatureLog(
                expiredBatch, 4.2, LocalDateTime.now().minusDays(1),
                ComplianceStatus.COMPLIANT, "Stored in chiller chamber #2: 4.2°C compliant."
        ));
        // Ceftriaxone freeze breach (1.0°C < 2.0°C)
        temperatureLogRepository.save(new TemperatureLog(
                expiredBatch, 1.0, LocalDateTime.now().minusMinutes(30),
                ComplianceStatus.OUT_OF_RANGE, "ALERT: Temperature dropped to 1.0°C, risking freezing damage!"
        ));

        System.out.println("--> Pharmaceutical Supply Chain Sample Data Seeded Successfully!");
    }
}
