package com.pharmacy.supplychain.repository;

import com.pharmacy.supplychain.entity.DispenseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DispenseRecordRepository extends JpaRepository<DispenseRecord, Long> {
    List<DispenseRecord> findAllByOrderByDispensedAtDesc();
    List<DispenseRecord> findTop10ByOrderByDispensedAtDesc();
}
