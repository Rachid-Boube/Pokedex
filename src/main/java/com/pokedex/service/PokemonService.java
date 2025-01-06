package com.pokedex.service;

import com.pokedex.entite.Pokemon;
import com.pokedex.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository repository;

    public List<Pokemon> getAllPokemons() {
        return repository.findAll();
    }

    public Optional<Pokemon> getPokemonById(int id) {
        return repository.findById(id);
    }

    public Pokemon addPokemon(Pokemon pokemon) {
        return repository.save(pokemon);
    }
}
