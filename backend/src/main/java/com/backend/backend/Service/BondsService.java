package com.backend.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.backend.Entity.Bonds;
import com.backend.backend.Repository.BondsRepository;
import java.util.List;

@Service
public class BondsService {

    @Autowired
    private BondsRepository bondsRepository;

    public List<Bonds> getAllBonds() {
        return bondsRepository.findAll();
    }
}
