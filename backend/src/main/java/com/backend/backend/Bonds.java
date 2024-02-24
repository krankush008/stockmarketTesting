package com.backend.backend;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Bonds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String bond_id;

    private String issuer;
    private String creditScore;
    private int maturity;
    private BigDecimal xirr;

    // getters and setters
}

