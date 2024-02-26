package com.backend.backend.Entity;

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

    public Alerts() {
    }

    public Alerts(String bondsId, Long userId, Double xirr) {
        this.bondsId = bondsId;
        this.userId = userId;
        this.xirr = xirr;
    }

    public Long getAlertId() {
        return alertId;
    }

    public String getBondsId() {
        return bondsId;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getXirr() {
        return xirr;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public void setBondsId(String bondsId) {
        this.bondsId = bondsId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setXirr(Double xirr) {
        this.xirr = xirr;
    }
}

