package com.pharmacy.supplychain.controller;

import com.pharmacy.supplychain.dto.ApiResponse;
import com.pharmacy.supplychain.entity.Supplier;
import com.pharmacy.supplychain.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Supplier>>> getAllSuppliers() {
        List<Supplier> list = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success(list, "Suppliers fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(@Valid @RequestBody Supplier supplier) {
        Supplier created = supplierService.createSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Supplier registered successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> updateSupplier(@PathVariable Long id,
                                                                @Valid @RequestBody Supplier supplier) {
        Supplier updated = supplierService.updateSupplier(id, supplier);
        return ResponseEntity.ok(ApiResponse.success(updated, "Supplier updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier deleted successfully"));
    }
}
