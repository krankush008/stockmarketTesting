package com.backend.backend;

public class Bond {
    private String id;
    private String issuer;
    private int creditScore;
    private int XIRR;

    // Constructor
    public Bond(String id, String issuer, int creditScore, int XIRR) {
        this.id = id;
        this.issuer = issuer;
        this.creditScore = creditScore;
        this.XIRR = XIRR;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public int getXIRR() {
        return XIRR;
    }

    public void setXIRR(int XIRR) {
        this.XIRR = XIRR;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Bond{" +
                "id='" + id + '\'' +
                ", issuer='" + issuer + '\'' +
                ", creditScore=" + creditScore +
                ", XIRR=" + XIRR +
                '}';
    }
}
