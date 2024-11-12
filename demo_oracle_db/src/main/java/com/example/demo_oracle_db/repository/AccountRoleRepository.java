package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
