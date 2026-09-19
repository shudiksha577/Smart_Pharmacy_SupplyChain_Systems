package com.pharmacy.supplychain.service.impl;

import com.pharmacy.supplychain.entity.Medicine;
import com.pharmacy.supplychain.exception.DuplicateResourceException;
import com.pharmacy.supplychain.exception.ResourceNotFoundException;
import com.pharmacy.supplychain.repository.MedicineRepository;
import com.pharmacy.supplychain.service.MedicineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + id));
    }

    @Override
    public Medicine createMedicine(Medicine medicine) {
        if (medicineRepository.existsByNameIgnoreCase(medicine.getName().trim())) {
            throw new DuplicateResourceException("Medicine with name '" + medicine.getName() + "' already exists.");
        }
        if (medicine.getMinTemperature() > medicine.getMaxTemperature()) {
            throw new IllegalArgumentException("Minimum temperature cannot exceed maximum temperature.");
        }
        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine updateMedicine(Long id, Medicine medicineDetails) {
        Medicine existing = getMedicineById(id);

        // Check if new name collides with another existing medicine
        if (!existing.getName().equalsIgnoreCase(medicineDetails.getName().trim()) &&
                medicineRepository.existsByNameIgnoreCase(medicineDetails.getName().trim())) {
            throw new DuplicateResourceException("Another medicine with name '" + medicineDetails.getName() + "' already exists.");
        }

        if (medicineDetails.getMinTemperature() > medicineDetails.getMaxTemperature()) {
            throw new IllegalArgumentException("Minimum temperature cannot exceed maximum temperature.");
        }

        existing.setName(medicineDetails.getName().trim());
        existing.setGenericName(medicineDetails.getGenericName().trim());
        existing.setCategory(medicineDetails.getCategory().trim());
        existing.setDosage(medicineDetails.getDosage().trim());
        existing.setMinTemperature(medicineDetails.getMinTemperature());
        existing.setMaxTemperature(medicineDetails.getMaxTemperature());

        return medicineRepository.save(existing);
    }

    @Override
    public void deleteMedicine(Long id) {
        Medicine existing = getMedicineById(id);
        medicineRepository.delete(existing);
    }
}
