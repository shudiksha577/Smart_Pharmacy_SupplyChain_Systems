package com.pharmacy.supplychain.repository;

import com.pharmacy.supplychain.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findBySupplierNameIgnoreCase(String supplierName);
    boolean existsBySupplierNameIgnoreCase(String supplierName);
}
