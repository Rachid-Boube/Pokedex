package com.pokedex.repository;

import com.pokedex.entite.Pokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PokemonRepository extends MongoRepository<Pokemon, String> {
    Optional<Pokemon> findByPokemonId(int pokemonId);
}