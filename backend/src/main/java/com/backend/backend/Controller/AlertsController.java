package com.backend.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.backend.backend.Entity.Alerts;
import com.backend.backend.Repository.AlertsRepository;
import com.backend.backend.Service.AlertsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AlertsController {

    @Autowired
    private AlertsRepository alertsRepository;

    @Autowired
    private AlertsService alertsService;

    ObjectMapper objectMapper = new ObjectMapper();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/getAlertsByUserId/{userId}")
    public ResponseEntity<?> getAlertsByUserId(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> result = alertsService.getAlertsByUserId(userId);
            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No alerts found for userId: " + userId);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching alerts: " + e.getMessage());
        }
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

        // Remove alerts that are not in the updated list
        List<Alerts> existingAlerts = alertsRepository.findByUserId(savedAlerts.get(0).getUserId());
        existingAlerts.stream()
                .filter(existingAlert -> !savedAlerts.contains(existingAlert))
                .forEach(alertsRepository::delete);

        return savedAlerts;
    }
}
