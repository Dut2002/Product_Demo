package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends CrudRepository<AccountRole,Long>, JpaSpecificationExecutor<Long> {
    boolean existsByAccountIdAndRoleId(Long accountId, Long roleId);

    void deleteByAccountIdAndRoleId(Long accountId, Long roleId);
}
