package com.pharmacy.supplychain.repository;

import com.pharmacy.supplychain.entity.Batch;
import com.pharmacy.supplychain.entity.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    Optional<Batch> findByBatchNumber(String batchNumber);

    Optional<Batch> findByBatchNumberIgnoreCase(String batchNumber);

    boolean existsByBatchNumber(String batchNumber);

    List<Batch> findByMedicineId(Long medicineId);

    List<Batch> findBySupplierId(Long supplierId);

    /**
     * Primary FEFO Query:
     * Selects all active batches for a given medicine that are:
     * 1. Strictly VERIFIED (authentic)
     * 2. Have remaining quantity > 0
     * 3. Have not expired (expiryDate >= today)
     * 4. Ordered strictly by expiryDate ASC (First Expired First Out)
     */
    @Query("SELECT b FROM Batch b WHERE b.medicine.id = :medicineId " +
           "AND b.verificationStatus = :status " +
           "AND b.quantity > 0 " +
           "AND b.expiryDate >= :currentDate " +
           "ORDER BY b.expiryDate ASC, b.id ASC")
    List<Batch> findAvailableBatchesForFefo(@Param("medicineId") Long medicineId,
                                           @Param("status") VerificationStatus status,
                                           @Param("currentDate") LocalDate currentDate);

    /**
     * Query to find all batches expiring within a given date window.
     */
    List<Batch> findByQuantityGreaterThanAndExpiryDateBetweenOrderByExpiryDateAsc(
            Integer quantity, LocalDate startDate, LocalDate endDate);

    /**
     * Query to find already expired batches that still hold inventory.
     */
    List<Batch> findByQuantityGreaterThanAndExpiryDateLessThanOrderByExpiryDateAsc(
            Integer quantity, LocalDate currentDate);

    /**
     * Recent batches for audit and ledger.
     */
    List<Batch> findTop10ByOrderByCreatedAtDesc();

    long countByQuantityGreaterThanAndExpiryDateLessThan(Integer quantity, LocalDate currentDate);

    long countByQuantityGreaterThan(Integer quantity);

    long countByQuantityLessThanEqual(Integer threshold);
}
