package com.pokedex.controller;

import com.pokedex.entite.Pokemon;
import com.pokedex.service.PokemonService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/all")
    public ResponseEntity<List<Pokemon>> getAllPokemons() {
        return new ResponseEntity<List<Pokemon>>(this.pokemonService.getAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public Pokemon addPokemon(@RequestBody Pokemon pokemon) {
        return this.pokemonService.create(pokemon);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Pokemon>> getPokemonById(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);  // Conversion de String en ObjectId
            return new ResponseEntity<>(this.pokemonService.getById(objectId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // ID invalide
        }
    }


    @GetMapping(value = "/name/{name}", produces = "application/json")
    public ResponseEntity<Pokemon> getPokemonByName(@PathVariable String name) {
        Optional<Pokemon> pokemon = pokemonService.getByName(name);
        if (pokemon.isPresent()) {
            return new ResponseEntity<>(pokemon.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/type/{type}", produces = "application/json")
    public ResponseEntity<List<Pokemon>> getPokemonByType(@PathVariable String type) {
        List<Pokemon> pokemons = pokemonService.getByType(type);
        if (!pokemons.isEmpty()) {
            return new ResponseEntity<>(pokemons, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}



