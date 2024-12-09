package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.NotifyAccount;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotifyAccountRepository extends CrudRepository<NotifyAccount, Long>, JpaSpecificationExecutor<NotifyAccount> {
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    @Query(value = "select id, is_read from cmd_notify_account " +
            " where notify_id = ?1 and account_id = ?2", nativeQuery = true)
    List<Tuple> findIdByNotifyIdAndAccountId(Long notifyId, Long accountId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update cmd_notify_account " +
            " set is_read = ?2 where id = ?1 ")
    void setRead(Long notifyAccountId, Integer isRead);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update cmd_notify_account " +
            " set is_read = 1 where account_id = ?1 and is_read = 0 ")
    void setAllRead(Long accountId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO cmd_notify_account (notify_id, account_id, is_read)" +
            "values (?1, ?2, 0)")
    void addNotifyAccount(Long notifyId, Long accountId);

    boolean existsByAccountIdAndIsRead(Long accountId, Integer isRead);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from cmd_notify_account " +
            " where notify_id = ?1 ")
    void deleteByNotifyId(Long notifyId);
}
