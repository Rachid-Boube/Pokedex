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

    @Autowired
    private PokemonService pokemonService;

    @GetMapping // 修改路径
    public ResponseEntity<List<Pokemon>> getAllPokemons() {
        return new ResponseEntity<>(this.pokemonService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public Pokemon addPokemon(@RequestBody Pokemon pokemon) {
        return this.pokemonService.create(pokemon);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Pokemon>> getPokemonById(@PathVariable String id) {
        return new ResponseEntity<>(this.pokemonService.getById(id), HttpStatus.OK);
    }
}
