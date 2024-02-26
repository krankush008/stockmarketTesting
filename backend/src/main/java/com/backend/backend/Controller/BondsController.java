package com.backend.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.backend.backend.Entity.Alerts;
import com.backend.backend.Entity.Bonds;
import com.backend.backend.Repository.AlertsRepository;
import com.backend.backend.Repository.BondsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BondsController {

    @Autowired
    private BondsRepository bondsRepository;

    @Autowired
    private AlertsRepository alertsRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/bonds")
    public List<Bonds> getAllBonds() {
        List<Bonds> allBonds = bondsRepository.findAll();
        return allBonds;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("api/createAlerts")
    public List<Alerts> createAlerts(@RequestBody List<Alerts> alerts) {
        List<Alerts> savedAlerts = new ArrayList<>(); 
        for (Alerts alert : alerts) {
            Optional<Alerts> existingAlert = alertsRepository.findByBondsIdAndUserId(alert.getBondsId(), alert.getUserId());
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
