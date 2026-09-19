package com.pharmacy.supplychain.controller;

import com.pharmacy.supplychain.dto.ApiResponse;
import com.pharmacy.supplychain.entity.Medicine;
import com.pharmacy.supplychain.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "*")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Medicine>>> getAllMedicines() {
        List<Medicine> list = medicineService.getAllMedicines();
        return ResponseEntity.ok(ApiResponse.success(list, "Medicines fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Medicine>> getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(ApiResponse.success(medicine, "Medicine fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Medicine>> createMedicine(@Valid @RequestBody Medicine medicine) {
        Medicine created = medicineService.createMedicine(medicine);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Medicine registered successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Medicine>> updateMedicine(@PathVariable Long id,
                                                                @Valid @RequestBody Medicine medicine) {
        Medicine updated = medicineService.updateMedicine(id, medicine);
        return ResponseEntity.ok(ApiResponse.success(updated, "Medicine updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Medicine deleted successfully"));
    }
}
