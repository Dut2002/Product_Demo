package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Voucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VoucherRepository extends CrudRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {

    boolean existsByCode(String code);

    Optional<Voucher> findByCode(String voucherCode);
}
