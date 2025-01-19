package com.pokedex.entite;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jwt") // Nom de la collection MongoDB
public class Jwt {

    @Id
    private String id; // MongoDB utilise un identifiant de type String par défaut

    @Field("valeur") // Champ de la collection
    private String valeur;

    @Field("desactive") // Champ de la collection
    private boolean desactive;

    @Field("expire") // Champ de la collection
    private boolean expire;

    @DBRef // Référence à un document lié
    private RefreshToken refreshToken;

    @DBRef // Référence à un autre document
    private User user;

}
