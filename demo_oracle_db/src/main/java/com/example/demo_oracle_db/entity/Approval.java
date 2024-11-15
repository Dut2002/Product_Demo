package com.example.demo_oracle_db.entity;

import com.example.demo_oracle_db.util.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CMD_APPROVAL", schema = "DEMO")
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVAL_TYPE", nullable = false)
    private Constants.ApprovalType approvalType;

    @Column(name = "REQUESTER_ID", nullable = false)
    private Long requesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Constants.ApprovalStatus status;

    @Column(name = "DATA", nullable = false, columnDefinition = "json")
    private String data; // JSON data

    @Column(name = "NOTE")
    private String note;

    @Column(name = "UPDATE_BY")
    private Long updateBy;
    @Column(name = "CREATE_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "UPDATE_AT")
    private LocalDateTime updatedAt;


    @OneToOne
    @JoinColumn(name = "REQUESTER_ID", insertable = false, updatable = false)
    private Account requester;

    @OneToOne
    @JoinColumn(name = "UPDATE_BY", insertable = false, updatable = false)
    private Account approver;
}
