package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Function;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FunctionRepository extends CrudRepository<Function, Long>, JpaSpecificationExecutor<Function> {


    boolean existsByFunctionName(String functionName);

    boolean existsByFunctionNameAndIdNot(String name, Long id);

    boolean existsByFeRoute(String feRoute);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CMD_FUNCTION (FUNCTION_NAME, FE_ROUTE) VALUES (?1, ?2)", nativeQuery = true)
    Integer addFunction(String name, String feRoute);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cmd_function SET FUNCTION_NAME = ?2, FE_ROUTE = ?3 " +
            " WHERE ID = ?1 ", nativeQuery = true)
    void updateFunction(Long id, String name, String feRoute);
}
