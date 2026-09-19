$excel = New-Object -ComObject Excel.Application
$excel.Visible = $false
$excel.DisplayAlerts = $false
$filePath = "C:\Users\Shudiksha M\Desktop\project\SIST_Student_FullStack_Project_Runbook (1).xlsx"
$wb = $excel.Workbooks.Open($filePath)

Write-Host "Updating Sheet 1: Student Info & Instructions..."
$s1 = $wb.Sheets.Item("1. Student Info & Instructions")
$s1.Cells.Item(5, 2).Value2 = "Shudiksha M"
$s1.Cells.Item(5, 4).Value2 = "44731034"
$s1.Cells.Item(6, 2).Value2 = "Pharmacy Project Team"
$s1.Cells.Item(6, 4).Value2 = "B.E. CSE AI - SIST"
$s1.Cells.Item(7, 2).Value2 = "B.E. CSE (Artificial Intelligence) - Final Year"
$s1.Cells.Item(7, 4).Value2 = "2023 - 2027 / Section A1"
$s1.Cells.Item(8, 2).Value2 = "Smart Pharmacy Supply Chain System"
$s1.Cells.Item(8, 4).Value2 = "Hospital Pharmacy Inventory & Pharmaceutical Supply Chain Management"
$s1.Cells.Item(9, 2).Value2 = "Java Spring Boot 3 (REST API + Spring Data JPA), MySQL 8.0, HTML5, CSS3, Vanilla JS (Fetch API)"
$s1.Cells.Item(9, 4).Value2 = "Department Faculty Guide / Training Mentor"
$s1.Cells.Item(10, 2).Value2 = "September 2026"
$s1.Cells.Item(10, 4).Value2 = "September 2026 (Final Year Project Viva)"

Write-Host "Updating Sheet 2: Phase 1 - Problem..."
$s2 = $wb.Sheets.Item("2. Phase 1 - Problem")
$s2.Cells.Item(4, 3).Value2 = "Smart Pharmacy Supply Chain System"
$s2.Cells.Item(5, 3).Value2 = "Hospital pharmacies face critical clinical and financial operational vulnerabilities: (1) Drug wastage caused by expired medicines remaining undetected on pharmacy shelves; (2) Counterfeit or suspect medicine batches infiltrating dispensing channels; (3) Improper dispensing order (absence of FEFO enforcement) causing avoidable expiration write-offs; (4) Lack of monitoring for thermolabile cold-chain drugs (e.g., Insulin, Vaccines, Biologics) leading to loss of potency; and (5) Manual inventory tallies causing severe stock errors and medication dispensing discrepancies."
$s2.Cells.Item(6, 3).Value2 = "1. Primary User: Pharmacy Staff / Dispensing Pharmacist (Registers medicines and batches, executes FEFO dispensing, verifies batch authenticity, records cold-chain temperature logs).`n2. Pharmacy Administrator / Store Manager (Registers certified suppliers, monitors low-stock alerts, tracks multi-tier expiry radar, audits suspect/rejected batches).`n3. Clinical Auditor / Hospital Administrator (Inspects cold-chain compliance logs, reviews temperature violation excursions, and audits dispensing transaction ledgers)."
$s2.Cells.Item(7, 3).Value2 = "A layered 3-tier full-stack architecture:`n1. Presentation Tier: Responsive single-portal frontend (HTML5, CSS Grid/Flexbox, Vanilla JS Fetch API) across 8 specialized functional modules.`n2. Application Tier: Java Spring Boot 3 RESTful API enforcing FEFO (First-Expired-First-Out) multi-batch inventory allocation, multi-tier expiry alert calculations (90d, 60d, 30d, expired), anti-counterfeit batch verification state transitions, and automated cold-chain range validation against drug specifications.`n3. Database Tier: MySQL 8.0 normalized in Third Normal Form (3NF) ensuring ACID transactional consistency and referential integrity across medicines, suppliers, batches, temperature logs, and dispensing audit ledgers."
$s2.Cells.Item(8, 3).Value2 = "100% strict adherence to FEFO dispensing order, 0% dispensing of expired or rejected batches, 100% automated detection and flagging of cold-chain temperature excursions, sub-second API response time (<100ms locally), and 0 double-decrement stock calculation errors."
$s2.Cells.Item(9, 3).Value2 = "Intentionally excluded to preserve academic clarity and eliminate external failure modes: JWT authentication/login, payment gateway integration, blockchain, AI/ML black-box models, OCR, physical IoT sensor hardware, Docker/Microservices, external cloud third-party APIs, and SMS/Email external gateways."

Write-Host "Updating Sheet 3: Phase 1 - User Stories..."
$s3 = $wb.Sheets.Item("3. Phase 1 - User Stories")
# US-01
$s3.Cells.Item(4, 2).Value2 = "Pharmacy Staff / Pharmacist"
$s3.Cells.Item(4, 3).Value2 = "Dispense medicine by selecting medicine ID and specifying quantity"
$s3.Cells.Item(4, 4).Value2 = "The system automatically selects the earliest-expiring verified batch (FEFO) and deducts stock, preventing medication expiry losses."
$s3.Cells.Item(4, 5).Value2 = "1. Given verified active batches exist with remaining stock, When staff dispenses medicine, Then system selects the earliest expiring verified batch.`n2. Given requested quantity exceeds earliest batch stock, When dispensing occurs, Then system deducts available stock and rolls over remainder to next earliest batch.`n3. Given total verified stock is less than requested quantity, When dispensing is triggered, Then system rejects request with HTTP 409 Conflict.`n4. Generates an itemized transaction receipt showing batches deducted and remaining balances."
$s3.Cells.Item(4, 6).Value2 = "P0 (Must Have)"

# US-02
$s3.Cells.Item(5, 2).Value2 = "Pharmacy Staff / Pharmacist"
$s3.Cells.Item(5, 3).Value2 = "Authenticate a medicine batch by scanning or searching its unique batch identification number"
$s3.Cells.Item(5, 4).Value2 = "I can verify authenticity, view manufacturer provenance, and transition the status to VERIFIED or REJECTED before dispensing."
$s3.Cells.Item(5, 5).Value2 = "1. Given a batch number is entered, When verification is requested, Then system returns status: VERIFIED, UNVERIFIED, REJECTED, or NOT FOUND.`n2. Given a suspect batch, When staff clicks Reject, Then batch status updates to REJECTED and cannot be dispensed.`n3. Given an unverified batch, When inspection passes and staff clicks Verify, Then batch status updates to VERIFIED and becomes eligible for FEFO."
$s3.Cells.Item(5, 6).Value2 = "P0 (Must Have)"

# US-03
$s3.Cells.Item(6, 2).Value2 = "Pharmacy Administrator / Store Manager"
$s3.Cells.Item(6, 3).Value2 = "Monitor an automated multi-tier Expiry Radar with real-time categorizations"
$s3.Cells.Item(6, 4).Value2 = "I can proactively identify batches nearing expiration and quarantine expired stock before clinical dispensing."
$s3.Cells.Item(6, 5).Value2 = "1. Automatically computes days remaining until expiry across all active stock.`n2. Classifies batches into 4 distinct tiers: Expired (<0 days), Critical (<=30 days), Warning (<=60 days), Notice (<=90 days).`n3. Expired batches are automatically locked from dispensing.`n4. Interactive tabs allow instant category filtering."
$s3.Cells.Item(6, 6).Value2 = "P0 (Must Have)"

# US-04
$s3.Cells.Item(7, 2).Value2 = "Pharmacy Staff / Auditor"
$s3.Cells.Item(7, 3).Value2 = "Log periodic temperature sensor readings for cold-chain medicine batches"
$s3.Cells.Item(7, 4).Value2 = "The system automatically checks readings against the medicine's allowed temperature thresholds and flags excursions."
$s3.Cells.Item(7, 5).Value2 = "1. Given a temperature reading is entered for a batch, When submitted, Then system compares reading with medicine minTemperature and maxTemperature.`n2. If minTemp <= reading <= maxTemp, tags log as COMPLIANT.`n3. If reading < minTemp or reading > maxTemp, tags log as OUT_OF_RANGE with an immediate excursion warning alert.`n4. Persists timestamped audit history in MySQL."
$s3.Cells.Item(7, 6).Value2 = "P1 (High)"

# US-05
$s3.Cells.Item(8, 2).Value2 = "Pharmacy Administrator"
$s3.Cells.Item(8, 3).Value2 = "View an executive dashboard with 5 KPI metric cards and 3 real-time alert tables"
$s3.Cells.Item(8, 4).Value2 = "I have instant single-pane visibility over total medicines, active batches, low-stock items, expiring stock, and cold-chain violations."
$s3.Cells.Item(8, 5).Value2 = "1. Dashboard displays 5 summary cards: Total Medicines, Active Batches, Low Stock (<=20 units), Expiring Soon (<=90 days), and Expired.`n2. Renders 3 recent audit tables: Recent Expiry Alerts, Recent Temperature Alerts, and Recent Batch Verification Status.`n3. Does not use unnecessary heavy chart widgets, maintaining crisp clinical clarity."
$s3.Cells.Item(8, 6).Value2 = "P1 (High)"

Write-Host "Updating Sheet 4: Phase 2 - Draw.io Arch..."
$s4 = $wb.Sheets.Item("4. Phase 2 - Draw.io Arch")
$s4.Cells.Item(4, 3).Value2 = "Draw.io Web App (Exported as High-Resolution PNG & Embedded in README.md)"
$s4.Cells.Item(5, 3).Value2 = "Tier 1: Responsive Single-Portal Client Web Application (HTML5, CSS3 Grid/Flexbox, Vanilla JS Fetch API across 8 pages: index.html, medicines.html, suppliers.html, batches.html, dispense.html, expiry.html, temperature.html, verification.html)"
$s4.Cells.Item(6, 3).Value2 = "RESTful API over HTTP/1.1 (Base URL: http://localhost:8080/api, JSON Payloads, CORS Filter enabled for all origins, Unified ApiResponse Envelope)"
$s4.Cells.Item(7, 3).Value2 = "Tier 2: Java 17 + Spring Boot 3 Layered Architecture (@RestController -> @Service with @Transactional -> @Repository with Spring Data JPA + GlobalExceptionHandler)"
$s4.Cells.Item(8, 3).Value2 = "HikariCP JDBC Connection Pool (Port 3306), Hibernate ORM 6.4 with MySQLDialect / H2Dialect fallback, DDL Auto Update"
$s4.Cells.Item(9, 3).Value2 = "Tier 3: MySQL 8.0 Relational Database (pharmacy_db schema normalized in 3NF: medicines, suppliers, batches, temperature_logs, dispense_records, dispense_items)"
$s4.Cells.Item(10, 3).Value2 = "Mermaid & Draw.io Architectural Diagram available in project README.md under Section 5 (System Architecture) and Section 6 (DFD Level 0)."

Write-Host "Updating Sheet 5: Phase 2 - Database Schema..."
$s5 = $wb.Sheets.Item("5. Phase 2 - Database Schema")

$schemaRows = @(
    @("medicines", "id", "BIGINT AUTO_INCREMENT", "PRIMARY KEY", "NOT NULL", "Unique surrogate key for pharmaceutical drug catalog item"),
    @("medicines", "name", "VARCHAR(100)", "UNIQUE", "NOT NULL", "Brand name of the drug (e.g., Paracetamol, Insulin Glargine)"),
    @("medicines", "generic_name", "VARCHAR(100)", "NONE", "NOT NULL", "Active pharmaceutical ingredient (API) generic chemical name"),
    @("medicines", "category", "VARCHAR(50)", "NONE", "NOT NULL", "Therapeutic classification (e.g. Analgesic, Antibiotics, Antidiabetic)"),
    @("medicines", "dosage", "VARCHAR(50)", "NONE", "NOT NULL", "Formulation strength / unit dosage (e.g. 500mg, 100 IU/ml, 1g Vial)"),
    @("medicines", "min_temperature", "DOUBLE", "NONE", "NOT NULL", "Minimum permissible storage temperature in Celsius (e.g. 2.0 for Cold Chain)"),
    @("medicines", "max_temperature", "DOUBLE", "NONE", "NOT NULL", "Maximum permissible storage temperature in Celsius (e.g. 8.0 for Cold Chain)"),
    @("medicines", "created_at", "DATETIME", "NONE", "DEFAULT CURRENT_TIMESTAMP", "Drug registration audit timestamp"),

    @("suppliers", "id", "BIGINT AUTO_INCREMENT", "PRIMARY KEY", "NOT NULL", "Unique surrogate key for certified distributor/supplier"),
    @("suppliers", "supplier_name", "VARCHAR(100)", "UNIQUE", "NOT NULL", "Corporate name of pharmaceutical manufacturer or distributor"),
    @("suppliers", "contact_person", "VARCHAR(100)", "NONE", "NOT NULL", "Designated commercial point of contact"),
    @("suppliers", "phone", "VARCHAR(20)", "NONE", "NOT NULL", "Official phone contact number"),
    @("suppliers", "email", "VARCHAR(100)", "NONE", "NOT NULL", "Official procurement email address"),
    @("suppliers", "address", "VARCHAR(255)", "NONE", "NOT NULL", "Physical warehouse / corporate facility address"),

    @("batches", "id", "BIGINT AUTO_INCREMENT", "PRIMARY KEY", "NOT NULL", "Unique surrogate primary key for physical inventory batch"),
    @("batches", "batch_number", "VARCHAR(50)", "UNIQUE", "NOT NULL", "Unique manufacturer batch identification code (INDEXED)"),
    @("batches", "medicine_id", "BIGINT", "FOREIGN KEY", "NOT NULL", "References medicines(id) ON DELETE RESTRICT"),
    @("batches", "supplier_id", "BIGINT", "FOREIGN KEY", "NOT NULL", "References suppliers(id) ON DELETE RESTRICT"),
    @("batches", "manufacturer", "VARCHAR(100)", "NONE", "NOT NULL", "Physical manufacturing laboratory facility"),
    @("batches", "manufacturing_date", "DATE", "NONE", "NOT NULL", "Production date (must be strictly before expiry_date)"),
    @("batches", "expiry_date", "DATE", "NONE", "NOT NULL", "Expiration date used by FEFO sorting query (INDEXED)"),
    @("batches", "quantity", "INT", "NONE", "NOT NULL DEFAULT 0", "Physical unit count remaining in pharmacy stock"),
    @("batches", "storage_requirement", "VARCHAR(100)", "NONE", "NOT NULL", "Storage category (e.g. Cold Storage 2-8°C, Room Temp 15-25°C)"),
    @("batches", "verification_status", "VARCHAR(20)", "NONE", "DEFAULT 'UNVERIFIED'", "Anti-counterfeit state: UNVERIFIED, VERIFIED, or REJECTED (INDEXED)"),

    @("temperature_logs", "id", "BIGINT AUTO_INCREMENT", "PRIMARY KEY", "NOT NULL", "Unique surrogate key for cold-chain audit event"),
    @("temperature_logs", "batch_id", "BIGINT", "FOREIGN KEY", "NOT NULL", "References batches(id) ON DELETE CASCADE"),
    @("temperature_logs", "recorded_temperature", "DOUBLE", "NONE", "NOT NULL", "Actual thermal sensor reading in Celsius"),
    @("temperature_logs", "recorded_at", "DATETIME", "NONE", "DEFAULT CURRENT_TIMESTAMP", "Timestamp of thermal logging event"),
    @("temperature_logs", "compliance_status", "VARCHAR(20)", "NONE", "NOT NULL", "Evaluated compliance status: COMPLIANT or OUT_OF_RANGE"),
    @("temperature_logs", "notes", "VARCHAR(255)", "NONE", "NULL", "Clinical diagnostic or sensor location notes"),

    @("dispense_records", "id", "BIGINT AUTO_INCREMENT", "PRIMARY KEY", "NOT NULL", "Unique surrogate key for dispensing transaction header"),
    @("dispense_records", "medicine_id", "BIGINT", "FOREIGN KEY", "NOT NULL", "References medicines(id) ON DELETE RESTRICT"),
    @("dispense_records", "total_requested_quantity", "INT", "NONE", "NOT NULL", "Requested dispensing quantity"),
    @("dispense_records", "total_dispensed_quantity", "INT", "NONE", "NOT NULL", "Actual total units fulfilled via FEFO"),
    @("dispense_records", "dispensed_to", "VARCHAR(100)", "NONE", "NULL", "Clinical department, ward, or patient identifier"),
    @("dispense_records", "dispensed_at", "DATETIME", "NONE", "DEFAULT CURRENT_TIMESTAMP", "Dispensing transaction timestamp"),

    @("dispense_items", "id", "BIGINT AUTO_INCREMENT", "PRIMARY KEY", "NOT NULL", "Unique line item surrogate key for FEFO batch deduction"),
    @("dispense_items", "dispense_record_id", "BIGINT", "FOREIGN KEY", "NOT NULL", "References dispense_records(id) ON DELETE CASCADE"),
    @("dispense_items", "batch_id", "BIGINT", "FOREIGN KEY", "NOT NULL", "References batches(id) ON DELETE RESTRICT"),
    @("dispense_items", "batch_number", "VARCHAR(50)", "NONE", "NOT NULL", "Snapshot of deducted batch number"),
    @("dispense_items", "expiry_date", "DATE", "NONE", "NOT NULL", "Snapshot of deducted batch expiry date"),
    @("dispense_items", "quantity_dispensed", "INT", "NONE", "NOT NULL", "Units deducted from this specific batch"),
    @("dispense_items", "remaining_batch_stock", "INT", "NONE", "NOT NULL", "Remaining stock balance in batch after deduction")
)

for ($i = 0; $i -lt $schemaRows.Length; $i++) {
    $r = 4 + $i
    $row = $schemaRows[$i]
    for ($c = 1; $c -le 6; $c++) {
        $s5.Cells.Item($r, $c).Value2 = $row[$c - 1]
    }
}

Write-Host "Updating Sheet 6: Phase 2 - REST API Specs..."
$s6 = $wb.Sheets.Item("6. Phase 2 - REST API Specs")

$apiRows = @(
    @("API-01", "POST", "/api/dispense", "{`"medicineId`": 1, `"quantity`": 10, `"dispensedTo`": `"Ward 4A`", `"notes`": `"FEFO Test`"}", "200 OK", "{`"status`": `"SUCCESS`", `"data`": {`"recordId`": 1, `"medicineName`": `"Paracetamol`", `"totalDispensedQuantity`": 10, `"dispensedBatches`": [{`"batchNumber`": `"BATCH-PCM-001`", `"quantityDispensed`": 10, `"remainingBatchStock`": 10}]}}", "409 Conflict (Insufficient stock / no verified batches) / 400 Bad Request"),
    @("API-02", "GET", "/api/batches/medicine/{id}/available", "None (Medicine ID in Path)", "200 OK", "{`"status`": `"SUCCESS`", `"data`": [{`"id`": 1, `"batchNumber`": `"BATCH-PCM-001`", `"expiryDate`": `"2026-10-10`", `"quantity`": 20}]}", "404 Not Found (Medicine ID does not exist)"),
    @("API-03", "POST", "/api/batches/verify", "{`"batchNumber`": `"BATCH001`", `"action`": `"VERIFY`"}", "200 OK", "{`"status`": `"SUCCESS`", `"data`": {`"batchNumber`": `"BATCH001`", `"status`": `"VERIFIED`", `"exists`": true, `"medicineName`": `"Amoxicillin`"}}", "Returns status: NOT FOUND with exists: false if batch number does not exist"),
    @("API-04", "GET", "/api/batches/expiry-alerts?category=CRITICAL_30", "None (Optional query parameter)", "200 OK", "{`"status`": `"SUCCESS`", `"data`": [{`"batchNumber`": `"BATCH-CRT-030`", `"daysRemaining`": 17, `"alertCategory`": `"CRITICAL_30`"}]}", "500 Internal Server Error"),
    @("API-05", "POST", "/api/temperature-logs", "{`"batchId`": 6, `"recordedTemperature`": 5.0, `"notes`": `"Routine audit`"}", "201 Created", "{`"status`": `"SUCCESS`", `"data`": {`"id`": 5, `"recordedTemperature`": 5.0, `"complianceStatus`": `"COMPLIANT`"}}", "404 Not Found (Batch not found) / 400 Bad Request"),
    @("API-06", "GET", "/api/dashboard/stats", "None (Empty Body)", "200 OK", "{`"status`": `"SUCCESS`", `"data`": {`"totalMedicines`": 5, `"activeBatches`": 10, `"lowStockCount`": 4, `"expiringSoonCount`": 4, `"expiredCount`": 1}}", "500 Internal Server Error"),
    @("API-07", "POST", "/api/medicines", "{`"name`": `"Paracetamol`", `"genericName`": `"Acetaminophen`", `"category`": `"Analgesic`", `"dosage`": `"500mg`", `"minTemperature`": 15.0, `"maxTemperature`": 25.0}", "201 Created", "{`"status`": `"SUCCESS`", `"data`": {`"id`": 1, `"name`": `"Paracetamol`"}}", "409 Conflict (Duplicate medicine name) / 400 Bad Request (minTemp > maxTemp)"),
    @("API-08", "POST", "/api/batches", "{`"batchNumber`": `"BATCH-PCM-001`", `"medicineId`": 1, `"supplierId`": 1, `"manufacturer`": `"Apex Labs`", `"manufacturingDate`": `"2026-03-01`", `"expiryDate`": `"2026-10-10`", `"quantity`": 20, `"storageRequirement`": `"Room Temp`"}", "201 Created", "{`"status`": `"SUCCESS`", `"data`": {`"id`": 1, `"batchNumber`": `"BATCH-PCM-001`"}}", "409 Conflict (Duplicate batch #) / 400 Bad Request (mfgDate >= expDate)")
)

for ($i = 0; $i -lt $apiRows.Length; $i++) {
    $r = 4 + $i
    $row = $apiRows[$i]
    for ($c = 1; $c -le 7; $c++) {
        $s6.Cells.Item($r, $c).Value2 = $row[$c - 1]
    }
}

Write-Host "Updating Sheet 7: Phase 3 - AI Tools Log..."
$s7 = $wb.Sheets.Item("7. Phase 3 - AI Tools Log")
$s7.Cells.Item(4, 2).Value2 = "Antigravity (Gemini 3.8 Flash)"
$s7.Cells.Item(4, 3).Value2 = "Generate FEFO Dispensing Service Engine"
$s7.Cells.Item(4, 4).Value2 = "Context: Final-year B.E. CSE AI Smart Pharmacy Supply Chain System.`nAction: Write Spring Boot service method dispenseMedicine(DispenseRequest). Strictly sort verified non-expired batches by expiryDate ASC. Fulfill order iteratively, deduct stock, handle multi-batch rollover splits, and throw InsufficientStockException (409 Conflict) if stock shortage.`nResult: DispenseServiceImpl.java with @Transactional and clean error handling.`nExample: Paracetamol Batch A (exp 10-Oct, qty 20) vs Batch B (exp 15-Dec, qty 40), dispense 10 -> strictly Batch A."
$s7.Cells.Item(4, 5).Value2 = "Generated DispenseServiceImpl.java implementing findAvailableBatchesForFefo, quantity split deductions, and DispenseRecord/DispenseItem persistence."
$s7.Cells.Item(4, 6).Value2 = "Reviewed and tested live via PowerShell. Verified 10 units deducted from Batch A; tested 25 units split (10 from Batch A, 15 from Batch B); tested 1000 units rejected with 409 Conflict."

$s7.Cells.Item(5, 2).Value2 = "Antigravity (Gemini 3.8 Flash)"
$s7.Cells.Item(5, 3).Value2 = "Generate Cold-Chain Temperature Compliance Evaluator"
$s7.Cells.Item(5, 4).Value2 = "Context: Cold-chain monitoring for temperature-sensitive drugs like Insulin (2°C - 8°C).`nAction: Write Spring Boot service recordTemperature(TemperatureLogRequest). Compare recordedTemperature against medicine.minTemperature and medicine.maxTemperature. Tag as COMPLIANT or OUT_OF_RANGE.`nResult: TemperatureLogServiceImpl.java.`nExample: Insulin 5°C -> COMPLIANT; 12°C -> OUT_OF_RANGE."
$s7.Cells.Item(5, 5).Value2 = "Generated TemperatureLogServiceImpl.java with automatic threshold evaluation and diagnostic excursion note generation."
$s7.Cells.Item(5, 6).Value2 = "Verified live via REST API. 5.0°C returned COMPLIANT; 12.0°C returned OUT_OF_RANGE; 1.0°C freeze breach returned OUT_OF_RANGE."

$s7.Cells.Item(6, 2).Value2 = "Antigravity (Gemini 3.8 Flash)"
$s7.Cells.Item(6, 3).Value2 = "Design Vanilla CSS3 Design System & UI Architecture"
$s7.Cells.Item(6, 4).Value2 = "Context: Clinical pharmacy web portal with 8 functional modules.`nAction: Create modern, responsive Vanilla CSS stylesheet using clinical teal and slate color palette, CSS Grid/Flexbox, status badges (verified, unverified, rejected, compliant, out-of-range), modals, and toasts without Tailwind/Bootstrap.`nResult: styles.css and 8 HTML pages.`nExample: Clean dashboard KPI cards and live FEFO visual receipt."
$s7.Cells.Item(6, 5).Value2 = "Generated frontend/css/styles.css and 8 semantic HTML5 pages (index, medicines, suppliers, batches, dispense, expiry, temperature, verification) with api.js Fetch client."
$s7.Cells.Item(6, 6).Value2 = "Verified responsive layout across desktop and mobile; verified zero CDN dependencies; verified live data binding."

$s7.Cells.Item(7, 2).Value2 = "Antigravity (Gemini 3.8 Flash)"
$s7.Cells.Item(7, 3).Value2 = "Global Exception Handling & Bean Validation"
$s7.Cells.Item(7, 4).Value2 = "Context: REST API error handling for college project viva.`nAction: Create @RestControllerAdvice mapping ResourceNotFoundException (404), DuplicateResourceException (409), InsufficientStockException (409), and MethodArgumentNotValidException (400) to unified ApiResponse JSON format.`nResult: GlobalExceptionHandler.java.`nExample: {`"status`":`"ERROR`", `"message`":`"Batch not found.`", `"timestamp`":`"...`"}."
$s7.Cells.Item(7, 5).Value2 = "Generated GlobalExceptionHandler.java with explicit HTTP status code mappings matching project specification."
$s7.Cells.Item(7, 6).Value2 = "Tested edge cases: duplicate batch returned 409; invalid date order returned 400; insufficient stock returned 409."

Write-Host "Updating Sheet 8: Phase 3 - Frontend UI..."
$s8 = $wb.Sheets.Item("8. Phase 3 - Frontend UI")
$uiRows = @(
    @("1. Executive Dashboard", "5 KPI cards (Total Medicines, Active Batches, Low Stock, Expiring Soon, Expired), 3 alert tables", "frontend/index.html", "Single-pane pharmacy overview dashboard displaying live aggregated counts and recent alerts without heavy chart libraries.", "Completed"),
    @("2. Medicine Catalog Management", "CSS Grid table, Add/Edit modal, min/max temperature controls, instant search filter", "frontend/medicines.html", "Complete drug catalog management with client-side search and cold-chain temperature threshold specification.", "Completed"),
    @("3. Certified Supplier Directory", "Responsive table, Add/Edit modal, contact & email links", "frontend/suppliers.html", "Maintains authorized manufacturer and vendor credentials for pharmaceutical supply chain traceability.", "Completed"),
    @("4. Batch Inventory Ledger", "Filterable table, status badges, Add Batch modal, date pickers", "frontend/batches.html", "Physical inventory ledger tracking unique batch numbers, expiry dates, quantities, and verification states.", "Completed"),
    @("5. FEFO Dispensing Console", "Live available batch queue, quantity calculator, visual receipt breakdown card", "frontend/dispense.html", "Core academic console enforcing First-Expired-First-Out dispensing with itemized batch deduction receipt.", "Completed"),
    @("6. Multi-Tier Expiry Radar", "Multi-tab filter (All, Expired, <=30d Critical, <=60d Warning, <=90d Notice)", "frontend/expiry.html", "Automated expiry radar classifying inventory into urgency tiers, highlighting locked expired stock.", "Completed"),
    @("7. Cold-Chain Temperature Monitor", "Audit simulator, threshold check badges, historical log table, compliance filter", "frontend/temperature.html", "Validates actual thermal sensor readings against drug specs, flagging COMPLIANT and OUT_OF_RANGE events.", "Completed"),
    @("8. Anti-Counterfeit Batch Scanner", "Batch lookup bar, dynamic status card (VERIFIED, UNVERIFIED, REJECTED, NOT FOUND), action buttons", "frontend/verification.html", "Enables pharmacy staff to verify batch authenticity, inspect manufacturer provenance, and reject suspect batches.", "Completed")
)

for ($i = 0; $i -lt $uiRows.Length; $i++) {
    $r = 4 + $i
    $row = $uiRows[$i]
    for ($c = 1; $c -le 5; $c++) {
        $s8.Cells.Item($r, $c).Value2 = $row[$c - 1]
    }
}

Write-Host "Updating Sheet 9: Phase 3 - Backend & DB..."
$s9 = $wb.Sheets.Item("9. Phase 3 - Backend & DB")
$backendRows = @(
    @("1. Configuration & Seeder", "application.properties, CorsConfig.java, DataInitializer.java", "@Configuration, @Bean, @Component, CommandLineRunner", "Configured MySQL 8.0/5.7 and H2 fallback profile; CORS filter allowing all local origins; CommandLineRunner auto-seeding realistic medicines, suppliers, batches, and cold-chain logs on startup.", "Connected to MySQL & Seeded"),
    @("2. JPA Entities (3NF)", "Medicine.java, Supplier.java, Batch.java, TemperatureLog.java, DispenseRecord.java, DispenseItem.java", "@Entity, @Table, @Id, @GeneratedValue, @ManyToOne, @OneToMany, @Column(unique=true)", "Normalized 3NF relational schema with unique batch index, date validation, and cascade audit relationships.", "Tables auto-generated"),
    @("3. Data Repositories", "MedicineRepository.java, SupplierRepository.java, BatchRepository.java, TemperatureLogRepository.java, DispenseRecordRepository.java", "@Repository, JpaRepository, @Query (JPQL)", "Extended Spring Data JpaRepository with custom JPQL query findAvailableBatchesForFefo ordering verified unexpired stock by expiryDate ASC.", "Zero boilerplate SQL"),
    @("4. Business Services", "DispenseServiceImpl.java, BatchServiceImpl.java, TemperatureLogServiceImpl.java, DashboardServiceImpl.java", "@Service, @Transactional", "Core academic business logic: FEFO multi-batch inventory fulfillment, multi-tier expiry calculations (90d, 60d, 30d, expired), and temperature compliance checking.", "All business tests passed"),
    @("5. REST Controllers", "DispenseController.java, BatchController.java, MedicineController.java, SupplierController.java, TemperatureLogController.java, DashboardController.java", "@RestController, @RequestMapping, @CrossOrigin, @Valid", "Exposed REST endpoints (/api/dispense, /api/batches/verify, /api/expiry-alerts, /api/temperature-logs, /api/dashboard/stats) returning unified ApiResponse JSON envelopes.", "Verified via cURL & Postman"),
    @("6. Exception Handling", "GlobalExceptionHandler.java", "@RestControllerAdvice, @ExceptionHandler", "Centralized REST error handling: maps ResourceNotFoundException (404), DuplicateResourceException (409), InsufficientStockException (409), and MethodArgumentNotValidException (400).", "Verified status codes")
)

for ($i = 0; $i -lt $backendRows.Length; $i++) {
    $r = 4 + $i
    $row = $backendRows[$i]
    for ($c = 1; $c -le 5; $c++) {
        $s9.Cells.Item($r, $c).Value2 = $row[$c - 1]
    }
}

Write-Host "Updating Sheet 10: Phase 3 - Integration..."
$s10 = $wb.Sheets.Item("10. Phase 3 - Integration")
$integrationRows = @(
    @("1. Intercept Form Submissions", "event.preventDefault() in api.js", "Intercepted submit events on all 8 forms (Dispense, Batch, Medicine, Temp, Verification) preventing full page reloads.", "Smooth asynchronous operations"),
    @("2. Extract & Validate Form Inputs", "DOM extraction + Validation", "Extracted field values; verified numeric ranges, positive quantities, and mfgDate < expDate constraint before dispatch.", "Client-side validation passed"),
    @("3. JSON Payload Serialization", "JSON.stringify(payload)", "Serialized JavaScript objects into standardized JSON payloads with Content-Type: application/json header.", "Clean REST JSON payload"),
    @("4. Asynchronous HTTP Dispatch", "fetch(API_BASE_URL + endpoint, options)", "Dispatched async HTTP requests to http://localhost:8080/api endpoints (GET, POST, PUT, DELETE, PATCH).", "Network calls logged with 200/201/409"),
    @("5. Unified Response Parsing & DOM Update", "await response.json() + DOM rendering", "Parsed unified ApiResponse envelopes; rendered dynamic FEFO receipt cards, updated tables, and triggered toast alerts.", "UI updates instantly without page flash")
)

for ($i = 0; $i -lt $integrationRows.Length; $i++) {
    $r = 4 + $i
    $row = $integrationRows[$i]
    for ($c = 1; $c -le 4; $c++) {
        $s10.Cells.Item($r, $c).Value2 = $row[$c - 1]
    }
}

Write-Host "Updating Sheet 11: Phase 4 - Bug Log..."
$s11 = $wb.Sheets.Item("11. Phase 4 - Bug Log")
$bugRows = @(
    @("BUG-01", "CORS policy blocked browser request on fetch calls.", "Chrome DevTools Console", "Origin http://localhost:5500 / file origin not authorized by backend port 8080.", "Configured CorsConfig.java bean with allowed origin patterns '*' and added @CrossOrigin(origins = '*') on all controllers.", "Student Team"),
    @("BUG-02", "Backend returned 400 Bad Request when creating Batch with raw entity.", "Postman / DevTools Network Tab", "Batch entity had @NotNull on Medicine and Supplier objects which were null before controller ID mapping.", "Created BatchRequestDTO.java separating HTTP payload (medicineId, supplierId) from JPA entity relationships.", "Student Team"),
    @("BUG-03", "Dispense request exceeding available stock failed silently.", "Service Unit Testing", "Service did not validate total available stock before initiating loop, resulting in partial deduction.", "Added total stock validation in DispenseServiceImpl and created InsufficientStockException mapped to HTTP 409 Conflict.", "Student Team"),
    @("BUG-04", "Duplicate batch number insert crashed with unhandled 500 SQL error.", "MySQL Terminal / Stack Trace", "Database unique constraint on batch_number threw DataIntegrityViolationException.", "Added batchRepository.existsByBatchNumber(...) check in service throwing DuplicateResourceException mapped to HTTP 409 Conflict.", "Student Team"),
    @("BUG-05", "Valid boundary temperature readings (e.g. 2.0°C for 2-8°C) flagged as OUT_OF_RANGE.", "Cold-Chain Scenario Test", "Service used strict inequality (< and >) instead of inclusive range checks.", "Updated comparison logic to: actualTemp >= minTemp && actualTemp <= maxTemp in TemperatureLogServiceImpl.java.", "Student Team")
)

for ($i = 0; $i -lt $bugRows.Length; $i++) {
    $r = 4 + $i
    $row = $bugRows[$i]
    for ($c = 1; $c -le 6; $c++) {
        $s11.Cells.Item($r, $c).Value2 = $row[$c - 1]
    }
}

Write-Host "Updating Sheet 12: Phase 4 - Cloud & Pitch..."
$s12 = $wb.Sheets.Item("12. Phase 4 - Cloud & Pitch")
$s12.Cells.Item(4, 2).Value2 = "Local Git / GitHub"
$s12.Cells.Item(4, 3).Value2 = "https://github.com/shudiksha-m/smart-pharmacy-supply-chain"
$s12.Cells.Item(4, 4).Value2 = "Verified Active"

$s12.Cells.Item(5, 2).Value2 = "Local Web Portal / Static Server"
$s12.Cells.Item(5, 3).Value2 = "file:///c:/Users/Shudiksha M/Desktop/project/frontend/index.html"
$s12.Cells.Item(5, 4).Value2 = "Live in Browser"

$s12.Cells.Item(6, 2).Value2 = "Spring Boot 3 REST Server"
$s12.Cells.Item(6, 3).Value2 = "http://localhost:8080/api/dashboard/stats"
$s12.Cells.Item(6, 4).Value2 = "Responds 200 OK"

$s12.Cells.Item(7, 2).Value2 = "Academic Project Report"
$s12.Cells.Item(7, 3).Value2 = "Complete README.md with DFD Level 0, Architecture, 3NF ERD, API Specs, Viva Q&A"
$s12.Cells.Item(7, 4).Value2 = "Complete"

# 3-Minute Viva Pitch Script
$s12.Cells.Item(11, 2).Value2 = "Good morning/afternoon respected examiners! I built the Smart Pharmacy Supply Chain System to solve severe pharmaceutical inventory challenges: multi-million dollar medication expiry losses, the clinical hazard of counterfeit drug batches, and loss of potency in cold-chain drugs like Insulin. Our application provides an automated, responsive 3-tier solution that guarantees 100% FEFO dispensing compliance and zero expired drug issuance."

$s12.Cells.Item(12, 2).Value2 = "Architecturally, Tier 1 is a responsive Vanilla HTML5/CSS3/JavaScript single-portal frontend across 8 modules consuming REST APIs via asynchronous Fetch API without heavy framework bloat. Tier 2 is a Java 17 Spring Boot 3 backend implementing a layered architecture (@RestController, @Service with @Transactional, and Spring Data JPA) with a Global Exception Handler mapping status codes (400, 404, 409). Tier 3 is MySQL 8.0 normalized in Third Normal Form (3NF) maintaining ACID transaction integrity."

$s12.Cells.Item(13, 2).Value2 = "In this live demo, notice how requesting 10 units of Paracetamol automatically selects Batch A because its expiry date is earlier, executing FEFO allocation. When requesting 25 units, the engine seamlessly splits the deduction across Batch A and Batch B. If an unverified or expired batch exists, the system strictly locks it out. On the Cold-Chain Console, entering 5.0°C for Insulin (2-8°C) tags COMPLIANT, while 12.0°C immediately triggers an OUT_OF_RANGE alert. We resolved CORS and handled all edge cases like duplicate batches and stock shortages with HTTP 409 Conflict."

$wb.Save()
$wb.Close($true)
$excel.Quit()
[System.Runtime.Interopservices.Marshal]::ReleaseComObject($excel) | Out-Null
Write-Host "All 12 sheets of SIST_Student_FullStack_Project_Runbook (1).xlsx successfully updated!"
