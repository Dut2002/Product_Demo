package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.FunctionRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FunctionRoleRepository extends CrudRepository<FunctionRole, Long>, JpaSpecificationExecutor<FunctionRole> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cmd_function_role (function_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
    void addFunctionRole(Long functionId, Long roleId);
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    boolean existsByFunctionIdAndRoleId(Long functionId, Long roleId);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "DELETE from cmd_function_role " +
            " Where function_id = ?1 and role_id = ?2",nativeQuery = true)
    void deleteByFunctionIdAndRoleId(Long functionId, Long roleId);


}
