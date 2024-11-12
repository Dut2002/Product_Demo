package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Supplier;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface SupplierRepository extends CrudRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {

    boolean existsByName(String supplierName);
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();
    @Query(nativeQuery = true, value = "Select ID FROM cmd_supplier where NAME = ?1")
    Optional<Long> getIdByName(String supplierName);
}
