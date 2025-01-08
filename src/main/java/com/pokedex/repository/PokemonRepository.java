package com.pokedex.repository;

import com.pokedex.entite.Pokemon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PokemonRepository extends MongoRepository<Pokemon, ObjectId> {
}