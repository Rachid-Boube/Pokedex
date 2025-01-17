package com.pokedex.repository;

import com.pokedex.entite.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email); // Trouver un utilisateur par son email
}
