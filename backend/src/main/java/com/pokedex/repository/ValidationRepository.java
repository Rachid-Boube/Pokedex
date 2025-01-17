package com.pokedex.repository;

import com.pokedex.entite.User;
import com.pokedex.entite.Validation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ValidationRepository extends MongoRepository<Validation,String> {
    Optional<Validation> findByUser(User user);
}
