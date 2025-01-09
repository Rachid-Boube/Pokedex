package com.pokedex.controller;

import com.pokedex.entite.Pokemon;
import com.pokedex.service.PokemonService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonController {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pokemon>> getAllPokemons() {
        return new ResponseEntity<>(this.pokemonService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public Pokemon addPokemon(@RequestBody Pokemon pokemon) {
        return this.pokemonService.create(pokemon);
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<Pokemon> getPokemonById(@PathVariable int pokemonId) {
        return this.pokemonService.getByPokemonId(pokemonId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
