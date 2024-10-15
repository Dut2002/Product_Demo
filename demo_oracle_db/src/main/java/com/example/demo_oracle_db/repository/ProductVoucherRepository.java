package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.ProductVoucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ProductVoucherRepository extends CrudRepository<ProductVoucher, Long>, JpaSpecificationExecutor<ProductVoucher> {

    boolean existsByProductIdAndVoucherId(Long productId, Long voucherId);

    Optional<ProductVoucher> findByProductIdAndVoucherId(Long productId, Long voucherId);
}
