package com.backend.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BondRepository extends JpaRepository<Bonds, String> {

    // Add a method for fetching bonds based on filters
    List<Bonds> findByIssuerAndCreditScoreAndMaturityGreaterThanEqual(String issuer, String creditScore, int maturity);
}

