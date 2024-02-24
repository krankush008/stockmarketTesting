package com.backend.backend;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByBondsIdAndXirrLessThan(String bondsId, BigDecimal xirr);
}
