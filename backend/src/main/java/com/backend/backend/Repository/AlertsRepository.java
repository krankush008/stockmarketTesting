package com.backend.backend.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.backend.backend.Entity.Alerts;

public interface AlertsRepository extends CrudRepository<Alerts, Long> {
    Optional<Alerts> findByBondsIdAndUserId(String bondsId, Long userId);
    List<Alerts> findByBondsIdAndXirrLessThan(String bondsId, BigDecimal xirr);
    List<Alerts> findByUserId(Long userId);
}
