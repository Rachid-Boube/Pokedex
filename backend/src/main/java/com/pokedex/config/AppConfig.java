package com.pokedex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${app.environment}")
    private String environment;

    public String getEnvironment() {
        return environment;
    }
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    
}