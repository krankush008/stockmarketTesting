package com.backend.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Bonds1 {

    @Id
    private String isin;
    private String maturityDate;
    private String creditScore;

    // Constructors, getters, and setters

    public Bonds1() {
        // Default constructor
    }

    public Bonds1(String isin, String maturityDate, String creditScore) {
        this.isin = isin;
        this.maturityDate = maturityDate;
        this.creditScore = creditScore;
    }

    public static Bonds1 parseHtmlToBond(String html) {
        Document doc = Jsoup.parse(html);

        // Extract ISIN from the URL
        String isin = extractIsin(doc.select("a[href]").attr("href"));

        // Extract Maturity Date
        String maturityDate = doc.select("h5.maturityDate").attr("data-val");

        // Extract Credit Score
        String creditScore = doc.select("p.creditScore").text();

        return new Bonds1(isin, maturityDate, creditScore);
    }

    private static String extractIsin(String url) {
        // Implement logic to extract ISIN from the URL
        // You may need to customize this based on the actual URL structure
        // Example: https://www.example.com/bonds/isin12345
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

    // Getter and Setter methods
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
