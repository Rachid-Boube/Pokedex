package com.pokedex.entite;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refresh_token") // Nom de la collection MongoDB
public class RefreshToken {

    @Id
    private String id; // MongoDB utilise un identifiant de type String par défaut

    @Field("valeur") // Champ dans la collection MongoDB
    private String valeur;

    @Field("creation") // Champ dans la collection MongoDB
    private Instant creation;

    @Field("expire") // Champ dans la collection MongoDB
    private boolean expire;

    @Field("expiration") // Champ dans la collection MongoDB
    private Instant expiration;
}
