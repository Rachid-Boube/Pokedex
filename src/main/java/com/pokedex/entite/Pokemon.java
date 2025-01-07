package com.pokedex.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pokemons")
public class Pokemon {

    @Id
    @JsonIgnore
    private ObjectId id;

    @JsonProperty("id")
    private int pokemonId;

    @JsonProperty("name")
    private Map<String, String> name;

    @JsonProperty("type")
    private List<String> type;

    @JsonProperty("base")
    private Map<String, Integer> base;

    @JsonProperty("description")
    private String description;

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public Map<String, Integer> getBase() {
        return base;
    }

    public void setBase(Map<String, Integer> base) {
        this.base = base;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
