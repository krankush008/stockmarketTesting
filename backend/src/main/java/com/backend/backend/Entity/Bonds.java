package com.backend.backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Bonds {

    @Id
    private String isin;
    private String maturityDate;
    private String creditScore;

    public Bonds() {
        // Default constructor
    }

    public Bonds(String isin, String maturityDate, String creditScore) {
        this.isin = isin;
        this.maturityDate = maturityDate;
        this.creditScore = creditScore;
    }

    // Getter for isin
    public String getIsin() {
        return isin;
    }

    // Setter for isin
    public void setIsin(String isin) {
        this.isin = isin;
    }

    // Getter for maturityDate
    public String getMaturityDate() {
        return maturityDate;
    }

    // Setter for maturityDate
    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    // Getter for creditScore
    public String getCreditScore() {
        return creditScore;
    }

    // Setter for creditScore
    public void setCreditScore(String creditScore) {
        this.creditScore = creditScore;
    }
}
