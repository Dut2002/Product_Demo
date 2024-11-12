package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
@Transactional
public interface ProductRepository extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cmd_product " +
            " (NAME, YEAR_MAKING, EXPIRE_DATE, QUANTITY, PRICE, CATEGORY_ID, SUPPLIER_ID) " +
            " values (?1,?2,?3,?4,?5, ?6, ?7)", nativeQuery = true)
    void addProduct(String name, Long yearMaking, LocalDate expireDate, Integer quantity, Double price, Long categoryId, Long supplierId);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    @Modifying
    @Transactional
    @Query(value = "UPDATE cmd_product " +
            " SET NAME = ?2, YEAR_MAKING = ?3, EXPIRE_DATE = ?4, QUANTITY = ?5, PRICE = ?6, CATEGORY_ID = ?7, SUPPLIER_ID = ?8 " +
            " WHERE ID = ?1 ",nativeQuery = true)
    void updateProduct(Long id, String name, Long yearMaking, LocalDate expireDate, Integer quantity, Double price, Long categoryId, Long supplierId);

    Boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
