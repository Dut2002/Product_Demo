package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Notify;
import com.example.demo_oracle_db.util.Constants;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface NotifyRepository extends CrudRepository<Notify, Long>, JpaSpecificationExecutor<Notify> {
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    @Query(value = "SELECT count(1) from cmd_notify " +
            " inner join demo.cmd_notify_account cna on account_id = ?1 && cmd_notify.id = cna.notify_id", nativeQuery = true)
    Long getCountUnRead(Long accountId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO cmd_notify (header, message, timestamp, page_redirect, data)" +
            "values (?1, ?2, ?3, ?4, ?5)")
    void addNotify(String header, String message, LocalDateTime timestamp, String page_redirect, String data);

    Optional<Notify> findByPageRedirect(Constants.PageRedirect pageRedirect);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update cmd_notify " +
            " set message = ?2, timestamp = ?3 " +
            " where id = ?1 and page_redirect = ?4 ")
    void updateSupplierRegisterNotify(Long notifyId, String message, LocalDateTime now, String page_redirect);
}
