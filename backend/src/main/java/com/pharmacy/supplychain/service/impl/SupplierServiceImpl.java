package com.pharmacy.supplychain.service.impl;

import com.pharmacy.supplychain.entity.Supplier;
import com.pharmacy.supplychain.exception.DuplicateResourceException;
import com.pharmacy.supplychain.exception.ResourceNotFoundException;
import com.pharmacy.supplychain.repository.SupplierRepository;
import com.pharmacy.supplychain.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        if (supplierRepository.existsBySupplierNameIgnoreCase(supplier.getSupplierName().trim())) {
            throw new DuplicateResourceException("Supplier with name '" + supplier.getSupplierName() + "' already exists.");
        }
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier existing = getSupplierById(id);

        if (!existing.getSupplierName().equalsIgnoreCase(supplierDetails.getSupplierName().trim()) &&
                supplierRepository.existsBySupplierNameIgnoreCase(supplierDetails.getSupplierName().trim())) {
            throw new DuplicateResourceException("Another supplier with name '" + supplierDetails.getSupplierName() + "' already exists.");
        }

        existing.setSupplierName(supplierDetails.getSupplierName().trim());
        existing.setContactPerson(supplierDetails.getContactPerson().trim());
        existing.setPhone(supplierDetails.getPhone().trim());
        existing.setEmail(supplierDetails.getEmail().trim());
        existing.setAddress(supplierDetails.getAddress().trim());

        return supplierRepository.save(existing);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier existing = getSupplierById(id);
        supplierRepository.delete(existing);
    }
}
