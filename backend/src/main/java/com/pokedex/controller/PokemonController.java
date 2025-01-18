package com.pokedex.controller;

import com.pokedex.entite.Pokemon;
import com.pokedex.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pokemons")
public class PokemonController {

    private final PokemonService pokemonService;


    @GetMapping("/all")
    public ResponseEntity<List<Pokemon>> getAllPokemons() {
        return new ResponseEntity<List<Pokemon>>(this.pokemonService.getAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public Pokemon addPokemon(@RequestBody Pokemon pokemon) {
        return this.pokemonService.create(pokemon);
    }


    @GetMapping("/{pokemonId}")
    public ResponseEntity<Pokemon> getPokemonById(@PathVariable int pokemonId) {
        return this.pokemonService.getByPokemonId(pokemonId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




    @GetMapping("/name/{name}")
    public ResponseEntity<Pokemon> getPokemonByName(@PathVariable String name) {
        return this.pokemonService.getByName(name)
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Pokemon>> getPokemonsByType(@PathVariable String type) {
        return new ResponseEntity<>(this.pokemonService.getByType(type), HttpStatus.OK);
    }
}
