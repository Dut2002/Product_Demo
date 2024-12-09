package com.example.demo_oracle_db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "CMD_NOTIFY_ACCOUNT", schema = "DEMO")

public class NotifyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long accountId;

    @Basic
    @Column(name = "NOTIFY_ID", nullable = false)
    private Long notifyId;

    @Basic
    @Column(name = "IS_READ", nullable = false)
    private Integer isRead;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", insertable=false, updatable=false)
    private Account account;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "NOTIFY_ID", insertable=false, updatable=false)
    private Notify notify;
}
