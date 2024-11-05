package com.example.demo_oracle_db.entity;

import com.example.demo_oracle_db.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CMD_ACCOUNT", schema = "DEMO")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Lob
    @Column(name = "ACCESS_TOKEN", columnDefinition = "LONGTEXT")
    private String accessToken;
    @Basic
    @Column(name = "FULL_NAME", nullable = false)
    @Size(max = 255)
    private String fullName;
    @Basic
    @Column(name = "PASSWORD")
    @Size(max = 255)
    private String password;
    @Basic
    @Column(name = "OTP", length = 10)
    @Size(max = 10)
    private String otp;

    @Basic
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;
    @Column(name = "OTP_EXPIRE")
    private LocalDateTime otpExpire;
    @Basic
    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    @Size(max = 100)
    @Email
    private String email;
    @Basic
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Constants.Status status;
    @Basic
    @Lob
    @Column(name = "REFRESH_TOKEN", columnDefinition = "LONGTEXT")
    private String refreshToken;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountRole> accountRoles;
}