package com.pokedex.repository;

import com.pokedex.entite.Role;
import com.pokedex.entite.TypeRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByLibelle(TypeRole libelle);
}
