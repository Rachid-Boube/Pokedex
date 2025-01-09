package com.pokedex.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokedex.config.AppConfig;

@RestController
public class EnvironmentController {

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/api/environment")
    public ResponseEntity<Map<String, String>> getEnvironment() {
        if (appConfig == null || appConfig.getEnvironment() == null) {
            // Log the error condition
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
        Map<String, String> env = new HashMap<>();
        env.put("environment", appConfig.getEnvironment());
        return ResponseEntity.ok(env);
    }
}