package com.backend.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Alerts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertId;
    private String bondsId;
    private Long userId;
    private Double xirr;

    // Constructors, getters, setters, etc.
    
    // Example constructor without arguments
    public Alerts() {
    }

    // Example constructor with arguments
    public Alerts(String bondsId, Long userId, Double xirr) {
        this.bondsId = bondsId;
        this.userId = userId;
        this.xirr = xirr;
    }

    public Long getAlertId() {
        return alertId;
    }

    public String getBonds_id() {
        return bondsId;
    }

    public Long getUser_id() {
        return userId;
    }

    public Double getXirr() {
        return xirr;
    }

    // Setters
    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public void setBonds_id(String bonds_id) {
        this.bondsId = bonds_id;
    }

    public void setUser_id(Long user_id) {
        this.userId = user_id;
    }

    public void setXirr(Double xirr) {
        this.xirr = xirr;
    }
}

