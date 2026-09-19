-- =====================================================================
-- Smart Pharmacy Supply Chain System - MySQL 8.0 / 5.7 Database Schema
-- Normalized to Third Normal Form (3NF)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS pharmacy_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pharmacy_db;

-- ---------------------------------------------------------------------
-- Table 1: suppliers
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL UNIQUE,
    contact_person VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table 2: medicines
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    generic_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    min_temperature DOUBLE NOT NULL,
    max_temperature DOUBLE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table 3: batches
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_number VARCHAR(50) NOT NULL UNIQUE,
    medicine_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    manufacturing_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    storage_requirement VARCHAR(100) NOT NULL,
    verification_status VARCHAR(20) NOT NULL DEFAULT 'UNVERIFIED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_batch_medicine FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE RESTRICT,
    CONSTRAINT fk_batch_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE RESTRICT,
    INDEX idx_batch_expiry (expiry_date),
    INDEX idx_batch_status (verification_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table 4: temperature_logs
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS temperature_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL,
    recorded_temperature DOUBLE NOT NULL,
    recorded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    compliance_status VARCHAR(20) NOT NULL,
    notes VARCHAR(255),
    CONSTRAINT fk_temp_batch FOREIGN KEY (batch_id) REFERENCES batches(id) ON DELETE CASCADE,
    INDEX idx_temp_batch (batch_id),
    INDEX idx_temp_status (compliance_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table 5: dispense_records
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dispense_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_id BIGINT NOT NULL,
    total_requested_quantity INT NOT NULL,
    total_dispensed_quantity INT NOT NULL,
    dispensed_to VARCHAR(100),
    dispensed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    notes VARCHAR(255),
    CONSTRAINT fk_dispense_medicine FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table 6: dispense_items
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dispense_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dispense_record_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    batch_number VARCHAR(50) NOT NULL,
    expiry_date DATE NOT NULL,
    quantity_dispensed INT NOT NULL,
    remaining_batch_stock INT NOT NULL,
    CONSTRAINT fk_item_record FOREIGN KEY (dispense_record_id) REFERENCES dispense_records(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_batch FOREIGN KEY (batch_id) REFERENCES batches(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
