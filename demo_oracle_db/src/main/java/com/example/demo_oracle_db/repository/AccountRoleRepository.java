package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRoleRepository extends CrudRepository<AccountRole,Long>, JpaSpecificationExecutor<Long> {
    boolean existsByAccountIdAndRoleId(Long accountId, Long roleId);

    void deleteByAccountIdAndRoleId(Long accountId, Long roleId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cmd_account_role" +
            " (ACCOUNT_ID, ROLE_ID) " +
            " values (?1,?2)",nativeQuery = true)
    void addAccountRole(Long accountId, Long roleId);
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    boolean existsByRoleId(Long id);

    @Query(value = "select count(1) from cmd_account_role where ACCOUNT_ID = ?1",nativeQuery = true)
    Long countUserRole(Long accountId);

    @Query(value = "select min(priority) from cmd_account_role " +
            " inner join demo.cmd_role cr on ACCOUNT_ID = ?1 AND cmd_account_role.ROLE_ID = cr.ID ", nativeQuery = true)
    Optional<Integer> getAccountPriority(Long accountId);

    boolean existsByRoleIdAndRoleName(Long roleId, String user);
}
