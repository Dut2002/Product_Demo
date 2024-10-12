package com.example.demo_oracle_db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CMD_ACCOUNT", schema = "DEMO")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 0)
    private Long id;
    @Basic
    @Column(name = "ACCESS_TOKEN", nullable = true)
    private String accessToken;
    @Basic
    @Column(name = "FULL_NAME", nullable = false, length = 255)
    @Size(max = 255)
    private String fullName;
    @Basic
    @Column(name = "PASSWORD", nullable = true, length = 255)
    @Size(max = 255)
    private String password;
    @Basic
    @Column(name = "OTP", nullable = true, length = 10)
    @Size(max = 10)
    private String otp;
    @Basic
    @Column(name = "ROLE_ID", nullable = false, precision = 0)
    private Long roleId;
    @Basic
    @Column(name = "USERNAME", nullable = false, length = 255)
    private String username;
    @Column(name = "OTP_EXPIRE")
    private LocalDateTime otpExpire;
    @Basic
    @Column(name = "EMAIL", nullable = false, length = 100)
    @Size(max = 100)
    @Email
    private String email;
    @Basic
    @Column(name = "STATUS", nullable = true, length = 10)
    @Size(max = 10)
    private String status;
    @Basic
    @Column(name = "REFRESH_TOKEN", nullable = true)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", insertable=false, updatable=false)
    private Role role;
}