package com.backend.backend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class HelloAppController {
    
    @RequestMapping("/hello")
    public String hello() {
        return "hello ankii";
    }
    
    
}
