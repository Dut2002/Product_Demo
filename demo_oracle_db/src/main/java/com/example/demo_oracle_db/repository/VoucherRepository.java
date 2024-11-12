package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Voucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VoucherRepository extends CrudRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();
    Optional<Voucher> findByCode(String voucherCode);
}
