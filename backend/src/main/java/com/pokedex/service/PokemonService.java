package com.pokedex.service;

import com.pokedex.entite.Pokemon;
import com.pokedex.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    public List<Pokemon> getAll() {
        return this.pokemonRepository.findAll();
    }

    public Optional<Pokemon> getByPokemonId(int pokemonId) {
        return this.pokemonRepository.findByPokemonId(pokemonId);
    }

    public Pokemon create(Pokemon pokemon) {
        return this.pokemonRepository.save(pokemon);
    }


    public Optional<Pokemon> getByName(String name) {
        List<Pokemon> pokemons = this.pokemonRepository.findAll();
        return pokemons.stream()
                .filter(pokemon -> pokemon.getName().containsValue(name))  // Rechercher par nom dans la map
                .findFirst();
    }

    public List<Pokemon> getByType(String type) {
        List<Pokemon> pokemons = this.pokemonRepository.findAll();
        return pokemons.stream()
                .filter(pokemon -> pokemon.getType().contains(type))
                .collect(Collectors.toList());
    }
}
