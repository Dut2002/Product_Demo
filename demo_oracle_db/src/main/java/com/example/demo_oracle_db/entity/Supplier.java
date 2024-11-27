package com.example.demo_oracle_db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CMD_SUPPLIER", schema = "DEMO")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "NAME", nullable = false)
    private String name;
    @Basic
    @Column(name = "CONTACT")
    private String contact;
    @Basic
    @Column(name = "ADDRESS")
    private String address;
    @Basic
    @Column(name = "PHONE", length = 20)
    private String phone;
    @Basic
    @Column(name = "EMAIL")
    private String email;
    @Basic
    @Column(name = "WEBSITE")
    private String website;

    @Basic
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @JsonIgnore
    @OneToOne(optional = false)
    @JoinColumn(name = "ACCOUNT_ID", updatable = false, insertable = false)
    private Account account;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
