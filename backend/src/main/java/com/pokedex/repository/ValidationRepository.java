package com.pokedex.repository;

import com.pokedex.entite.User;
import com.pokedex.entite.Validation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.Optional;

public interface ValidationRepository extends MongoRepository<Validation,String> {
    // Ajouter cette méthode pour trouver une validation par utilisateur
    Optional<Validation> findByUser(User user);

    Optional<Validation> findByCode(String code);

    void deleteAllByExpirationBefore(Instant now);
}
