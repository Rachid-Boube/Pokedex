package com.pokedex.entite;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "pokemons")
public class Pokemon {
    @Id
    private String id;
    private int pokemonId;
    private Map<String, String> name;
    private List<String> type;
    private Map<String, Integer> base;




}
