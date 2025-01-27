package com.pokedex.entite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pokemons")
public class Pokemon {

    @Id
    @JsonProperty("_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @JsonProperty("pokemonId")
    private int pokemonId;

    @JsonProperty("name")
    private Map<String, String> name;

    @JsonProperty("type")
    private List<String> type;

    @JsonProperty("base")
    private Map<String, Integer> base;

    @JsonProperty("description")
    private String description;
}
