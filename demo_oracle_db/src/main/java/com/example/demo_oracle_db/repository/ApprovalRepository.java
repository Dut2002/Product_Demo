package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Approval;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
@Transactional
public interface ApprovalRepository extends CrudRepository<Approval, Long>, JpaSpecificationExecutor<Approval> {
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = " INSERT INTO cmd_approval (approval_type, requester_id, status, data, create_at) " +
            " values (?1,?2,?3,?4,?5) ")
    void addRequest(String approvalType, Long accountId, String approvalStatus, String jsonRequest, LocalDateTime now);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE cmd_approval " +
            " set status = ?2, update_by = ?3, update_at = ?4 " +
            " where id = ?1 ")
    void processRequest(Long id, String status, Long accountId, LocalDateTime updateAt);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE cmd_approval " +
            " set note = ?2, update_by = ?3, update_at = ?4 " +
            " where id = ?1 ")
    void saveNote(Long id, String note, Long accountId, LocalDate now);

    @Query(nativeQuery = true, value = "select count(1) from cmd_approval" +
            " where status = ?2 and approval_type = ?1")
    Integer countRequest(String approvalType, String approvalStatus);
}

