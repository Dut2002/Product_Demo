package com.example.demo_oracle_db.entity;

import com.example.demo_oracle_db.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CMD_NOTIFY", schema = "DEMO")

public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "HEADER", nullable = false)
    private String header;

    @Basic
    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Basic
    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAGE_REDIRECT")
    private Constants.PageRedirect pageRedirect;


    @Basic
    @Column(name = "DATA", columnDefinition = "json")
    private String data;

    @JsonIgnore
    @OneToMany(mappedBy = "notify", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotifyAccount> notifyAccountList;
}
