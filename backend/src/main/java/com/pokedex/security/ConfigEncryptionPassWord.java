package com.pokedex.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ConfigEncryptionPassWord {

    // Déclare un bean pour l'encodeur de mot de passe BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        // Retourne une nouvelle instance de BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }
}
