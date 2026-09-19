-- =====================================================================
-- Smart Pharmacy Supply Chain System - Sample Seed Data SQL Script
-- =====================================================================

USE pharmacy_db;

-- 1. Insert Suppliers
INSERT INTO suppliers (id, supplier_name, contact_person, phone, email, address, created_at)
VALUES
(1, 'Apex Pharma Distributors', 'Rajesh Sharma', '+91 9876543210', 'contact@apexpharma.in', 'Plot 12, Industrial Area, Hyderabad', NOW()),
(2, 'Biogen Healthcare Ltd.', 'Dr. Sunita Sen', '+91 9811223344', 'supply@biogen.com', '45 Technology Park, Bengaluru', NOW()),
(3, 'Global Meds Supply Co.', 'Vikram Mehta', '+91 9845012345', 'support@globalmeds.in', '88 Ring Road, Mumbai', NOW())
ON DUPLICATE KEY UPDATE supplier_name = VALUES(supplier_name);

-- 2. Insert Medicines
INSERT INTO medicines (id, name, generic_name, category, dosage, min_temperature, max_temperature, created_at)
VALUES
(1, 'Paracetamol', 'Acetaminophen', 'Analgesic', '500mg', 15.0, 25.0, NOW()),
(2, 'Insulin Glargine', 'Insulin Glargine Recombinant', 'Antidiabetic', '100 IU/ml', 2.0, 8.0, NOW()),
(3, 'Amoxicillin', 'Amoxicillin Trihydrate', 'Antibiotics', '250mg', 15.0, 25.0, NOW()),
(4, 'Ceftriaxone Injection', 'Ceftriaxone Sodium', 'Antibiotics', '1g Vial', 2.0, 8.0, NOW()),
(5, 'Atorvastatin', 'Atorvastatin Calcium', 'Cardiovascular', '20mg', 15.0, 30.0, NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 3. Insert Batches
-- Example 1: Paracetamol Batch A (expiring sooner) vs Batch B (expiring later)
INSERT INTO batches (id, batch_number, medicine_id, supplier_id, manufacturer, manufacturing_date, expiry_date, quantity, storage_requirement, verification_status, created_at)
VALUES
(1, 'BATCH-PCM-001', 1, 1, 'Apex Laboratories', '2026-03-01', '2026-10-10', 20, 'Room Temperature (15-25°C)', 'VERIFIED', NOW()),
(2, 'BATCH-PCM-002', 1, 1, 'Apex Laboratories', '2026-06-01', '2026-12-15', 40, 'Room Temperature (15-25°C)', 'VERIFIED', NOW()),
(3, 'BATCH001', 3, 2, 'Biogen Labs', '2026-05-15', '2027-05-15', 50, 'Controlled Room Temp', 'VERIFIED', NOW()),
(4, 'BATCH-AMX-999', 3, 3, 'Global Meds Corp', '2026-08-01', '2027-03-01', 35, 'Controlled Room Temp', 'UNVERIFIED', NOW()),
(5, 'BATCH-SUSPECT-04', 1, 3, 'Unknown Subcontractor', '2026-07-01', '2027-02-01', 10, 'Room Temperature', 'REJECTED', NOW()),
(6, 'BATCH-INS-101', 2, 2, 'Biogen Biotech', '2026-04-01', '2026-11-05', 65, 'Cold Storage (2-8°C)', 'VERIFIED', NOW()),
(7, 'BATCH-INS-102', 2, 2, 'Biogen Biotech', '2026-07-15', '2027-04-15', 120, 'Cold Storage (2-8°C)', 'VERIFIED', NOW()),
(8, 'BATCH-EXP-001', 4, 1, 'Apex Laboratories', '2025-07-01', '2026-08-15', 15, 'Cold Storage (2-8°C)', 'VERIFIED', NOW()),
(9, 'BATCH-CRT-030', 5, 3, 'Global Meds Corp', '2025-10-01', '2026-10-05', 18, 'Room Temperature', 'VERIFIED', NOW()),
(10, 'BATCH-WRN-060', 3, 1, 'Apex Laboratories', '2026-01-10', '2026-11-02', 25, 'Room Temperature', 'VERIFIED', NOW())
ON DUPLICATE KEY UPDATE batch_number = VALUES(batch_number);

-- 4. Insert Temperature Logs
INSERT INTO temperature_logs (id, batch_id, recorded_temperature, recorded_at, compliance_status, notes)
VALUES
(1, 6, 5.0, NOW() - INTERVAL 4 HOUR, 'COMPLIANT', 'Routine audit: 5.0°C within 2.0°C - 8.0°C.'),
(2, 6, 12.0, NOW() - INTERVAL 1 HOUR, 'OUT_OF_RANGE', 'ALERT: Cold-chain violation! Reading 12.0°C is outside range [2.0°C - 8.0°C].'),
(3, 8, 4.2, NOW() - INTERVAL 1 DAY, 'COMPLIANT', 'Chiller chamber 4.2°C compliant.'),
(4, 8, 1.0, NOW() - INTERVAL 30 MINUTE, 'OUT_OF_RANGE', 'ALERT: Temperature dropped to 1.0°C, risking freezing damage!')
ON DUPLICATE KEY UPDATE recorded_temperature = VALUES(recorded_temperature);
