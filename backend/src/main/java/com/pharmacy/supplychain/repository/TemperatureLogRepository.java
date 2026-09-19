package com.pharmacy.supplychain.repository;

import com.pharmacy.supplychain.entity.ComplianceStatus;
import com.pharmacy.supplychain.entity.TemperatureLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, Long> {

    List<TemperatureLog> findByBatchIdOrderByRecordedAtDesc(Long batchId);

    List<TemperatureLog> findTop15ByOrderByRecordedAtDesc();

    List<TemperatureLog> findByComplianceStatusOrderByRecordedAtDesc(ComplianceStatus complianceStatus);

    long countByComplianceStatus(ComplianceStatus complianceStatus);
}
