package com.pharmacy.supplychain.controller;

import com.pharmacy.supplychain.dto.ApiResponse;
import com.pharmacy.supplychain.dto.DispenseRequest;
import com.pharmacy.supplychain.dto.DispenseResponse;
import com.pharmacy.supplychain.entity.DispenseRecord;
import com.pharmacy.supplychain.service.DispenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispense")
@CrossOrigin(origins = "*")
public class DispenseController {

    private final DispenseService dispenseService;

    public DispenseController(DispenseService dispenseService) {
        this.dispenseService = dispenseService;
    }

    /**
     * Module 4 - FEFO Dispensing
     * Strictly fulfills order using earliest expiring verified batches.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DispenseResponse>> dispenseMedicine(
            @Valid @RequestBody DispenseRequest request) {
        DispenseResponse response = dispenseService.dispenseMedicine(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(response, "Medicine successfully dispensed via FEFO protocol"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DispenseRecord>>> getAllDispenseRecords() {
        List<DispenseRecord> records = dispenseService.getAllDispenseRecords();
        return ResponseEntity.ok(ApiResponse.success(records, "Dispense audit records fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DispenseRecord>> getDispenseRecordById(@PathVariable Long id) {
        DispenseRecord record = dispenseService.getDispenseRecordById(id);
        return ResponseEntity.ok(ApiResponse.success(record, "Dispense record retrieved successfully"));
    }
}
