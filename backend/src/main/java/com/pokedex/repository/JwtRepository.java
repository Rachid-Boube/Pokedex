package com.pokedex.repository;

import com.pokedex.entite.Jwt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends MongoRepository<Jwt, String> {

    Optional<Jwt> findByValeurAndDesactiveAndExpire(String valeur, boolean desactive, boolean expire);

    @Query("{ 'expire': ?0, 'desactive': ?1, 'utilisateur.email': ?2 }")
    Optional<Jwt> findByUtilisateurValidToken(String email,boolean expire, boolean desactive);

    @Query("{ 'utilisateur.email': ?0, 'expire': false, 'desactive': false }")
    Stream<Jwt> findUtilisateur(String email);

    @Query("{ 'refreshToken.valeur': ?0, 'expire': false, 'desactive': false }")
    Optional<Jwt> findByRefreshToken(String valeur);

    void deleteAllByExpireAndDesactive(boolean expire, boolean desactive);
}

