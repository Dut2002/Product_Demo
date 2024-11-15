package com.example.demo_oracle_db.repository;

import com.example.demo_oracle_db.entity.Approval;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            " set status = ?2, note = ?3, update_by = ?4, update_at = ?5 " +
            " where id = ?1 ")
    void processRequest(Long id, String status, String note, Long accountId, LocalDateTime updateAt);
}
