package com.pokedex.entite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "validation") // Annotation pour indiquer une collection MongoDB
public class Validation {

    @Id
    private String id; // Pour MongoDB, l'ID est souvent de type String (ObjectId dans MongoDB).

    private Instant creation;
    private Instant expiration;
    private Instant activation;

    private String code; // Les champs uniques peuvent être gérés via des index (voir ci-dessous).

    @DBRef // Annotation pour référencer une autre collection (relation avec `Utilisateur`)
    private User user;

}
