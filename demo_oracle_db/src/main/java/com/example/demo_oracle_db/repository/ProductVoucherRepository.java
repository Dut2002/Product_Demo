package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.ProductVoucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductVoucherRepository extends CrudRepository<ProductVoucher, Long>, JpaSpecificationExecutor<ProductVoucher> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO cmd_product_voucher " +
            " (product_id, voucher_id) " +
            " values (?1,?2)")
    void addProductVoucher(Long productId, Long id);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    boolean existsByProductIdAndVoucherId(Long productId, Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM cmd_product_voucher where PRODUCT_ID = ?1 && VOUCHER_ID = ?2")
    void deleteByProductIdAndVoucherId(Long productId, Long voucherId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM cmd_product_voucher where PRODUCT_ID = ?1")
    void deleteByProductId(Long id);
}
