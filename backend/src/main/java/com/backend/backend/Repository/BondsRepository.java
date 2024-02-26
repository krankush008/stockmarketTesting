package com.backend.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.backend.Entity.Bonds;

public interface BondsRepository extends JpaRepository<Bonds, String> {
}
