package com.backend.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BondController {

    @Autowired
    private Bonds1Repository bonds1Repository;

    @Autowired
    private AlertsRepository alertsRepository;

    
    /*
    @GetMapping("/api/bonds")
    public List<Bonds> getFilteredBonds(
            @RequestParam(required = false) String issuer,
            @RequestParam(required = false) String creditScore,
            @RequestParam(required = false) Integer maturity) {
        
        System.out.println("ankushhusha");
        // Call the repository method to fetch bonds based on filters
        if (issuer != null && creditScore != null && maturity != null) {
            List<Bonds> ans1 = bondRepository.findByIssuerAndCreditScoreAndMaturityGreaterThanEqual(issuer, creditScore, maturity);
            System.out.println("Choton");
            return ans1;
        } else {
            // Return all bonds if no filters are provided
            return bondRepository.findAll();
        }
    }
    */

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/bonds")
    public List<Bonds1> getAllBonds() {
        //System.out.println("MYname"+bonds1Repository.findAll());
        List<Bonds1> allBonds = bonds1Repository.findAll();
        for (Bonds1 bond : allBonds) {
            System.out.println("ID: " + bond.getIsin() +
                               ", maturity: " + bond.getMaturityDate() +
                               ", Credit Score: " + bond.getCreditScore());
        }
        return allBonds;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("api/createAlerts")
    public List<Alerts> createAlert(@RequestBody List<Alerts> alerts) {
        List<Alerts> savedAlerts = new ArrayList<>(); 

        // Save each alert in the list

        // for (Alerts alert : alerts) {
        //     System.out.println("ankush backend alert id: " + alert.getBonds_id());
        //     alerts.add(alertsRepository.save(alert));
        // }
        for (Alerts alert : alerts) {
            Optional<Alerts> existingAlert = alertsRepository.findByBondsIdAndUserId(alert.getBonds_id(), alert.getUser_id());
    
            if (existingAlert.isPresent()) {
                // Update the existing record
                Alerts existing = existingAlert.get();
                existing.setXirr(alert.getXirr());
                savedAlerts.add(alertsRepository.save(existing));
            } else {
                // Insert a new record
                savedAlerts.add(alertsRepository.save(alert));
            }
        }
        return savedAlerts;
    }
}
