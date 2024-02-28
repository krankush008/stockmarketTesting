package com.backend.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.backend.Entity.Bonds;
import com.backend.backend.Service.BondsService;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BondsController {

    @Autowired
    private BondsService bondsService;
    @GetMapping("/api/bonds")
    public List<Bonds> getAllBonds() {
        return bondsService.getAllBonds();
    }
}

