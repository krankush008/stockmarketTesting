package com.backend.backend.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.backend.backend.Entity.Alerts;
import com.backend.backend.Repository.AlertsRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertsService {

    @Autowired
    private AlertsRepository alertsRepository;

    public List<Map<String, Object>> getAlertsByUserId(Long userId) {
        List<Alerts> alerts = alertsRepository.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Alerts alert : alerts) {
            Map<String, Object> alertInfo = new HashMap<>();
            alertInfo.put("bondId", alert.getBondsId());
            alertInfo.put("xirr", alert.getXirr());
            result.add(alertInfo);
        }
        return result;
    }
}
