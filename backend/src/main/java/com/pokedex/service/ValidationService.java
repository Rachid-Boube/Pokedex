package com.pokedex.service;

import com.pokedex.entite.User;
import com.pokedex.entite.Validation;
import com.pokedex.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    public void enregistrer(User user) {
        // Rechercher une validation existante pour l'utilisateur
        Optional<Validation> validationExistante = validationRepository.findByUser(user);

        Validation validation;
        if (validationExistante.isPresent()) {
            // Si une validation existe, on la réutilise ou la met à jour
            validation = validationExistante.get();
            validation.setCreation(Instant.now());
            validation.setExpiration(Instant.now().plus(10, ChronoUnit.MINUTES));
        } else {
            // Créer une nouvelle validation
            validation = new Validation();
            validation.setUser(user);
            validation.setCreation(Instant.now());
            validation.setExpiration(Instant.now().plus(10, ChronoUnit.MINUTES));
        }

        // Générer un nouveau code de validation
        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);
        validation.setCode(code);

        // Enregistrer ou mettre à jour la validation
        this.validationRepository.save(validation);

        // Envoie une notification à l'utilisateur
        this.notificationService.envoyerNotification(validation);
    }
}