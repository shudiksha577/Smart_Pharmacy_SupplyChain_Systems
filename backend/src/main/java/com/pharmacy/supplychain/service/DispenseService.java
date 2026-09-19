package com.pharmacy.supplychain.service;

import com.pharmacy.supplychain.dto.DispenseRequest;
import com.pharmacy.supplychain.dto.DispenseResponse;
import com.pharmacy.supplychain.entity.DispenseRecord;

import java.util.List;

public interface DispenseService {
    DispenseResponse dispenseMedicine(DispenseRequest request);
    List<DispenseRecord> getAllDispenseRecords();
    DispenseRecord getDispenseRecordById(Long id);
}
