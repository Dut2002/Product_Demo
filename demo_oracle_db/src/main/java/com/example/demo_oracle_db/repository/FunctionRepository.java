package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Function;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FunctionRepository extends CrudRepository<Function, Long>, JpaSpecificationExecutor<Function> {


    boolean existsByName(String functionName);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByFeRoute(String feRoute);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CMD_FUNCTION (FUNCTION_NAME, FE_ROUTE, priority) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void addFunction(String name, String feRoute, Integer priority);
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    @Modifying
    @Transactional
    @Query(value = "UPDATE cmd_function SET FUNCTION_NAME = ?2, FE_ROUTE = ?3 " +
            " WHERE ID = ?1 ", nativeQuery = true)
    void updateFunction(Long id, String name, String feRoute);

    @Query(value = "select f.ID, f.FUNCTION_NAME from cmd_function f" +
            " where priority >= ?2 and NOT EXISTS(" +
            " select 1 from cmd_function_role" +
            " where function_id = f.ID and role_id = ?1)" , nativeQuery = true)
    List<Object[]> getFunctionRoleNot(Long id, Integer priority);

    @Query(value = "select priority from cmd_function WHERE ID = ?1", nativeQuery = true)
    Optional<Integer> findPriorityById(Long functionId);
}
