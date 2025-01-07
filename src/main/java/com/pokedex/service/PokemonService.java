package com.pokedex.service;

import com.pokedex.entite.Pokemon;
import com.pokedex.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class PokemonService {

    @Autowired
    private  PokemonRepository pokemonRepository;

    public List<Pokemon> getAll() {
        return this.pokemonRepository.findAll();
    }


    public Optional<Pokemon> getById(String id) {
        return this.pokemonRepository.findById(id);
    }

    public Pokemon create(Pokemon pokemon) {
        return this.pokemonRepository.save(pokemon);
    }

    public void deleteById(String id) {
        this.pokemonRepository.deleteById(id);
    }

    public Optional<Pokemon> getByName(String name) {
        List<Pokemon> pokemons = this.pokemonRepository.findAll();
        return pokemons.stream()
                .filter(pokemon -> pokemon.getName().containsValue(name))  // Rechercher par nom dans la map
                .findFirst();
    }
}
