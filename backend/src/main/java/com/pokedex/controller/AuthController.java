package com.pokedex.controller;


import com.pokedex.dto.AuthentificationDTO;
import com.pokedex.entite.User;
import com.pokedex.security.JwtService;
import com.pokedex.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path ="/auth",consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private  UserService userService;
    private AuthenticationManager authenticationManager;// Déclare une dépendance vers le gestionnaire d'authentification, injectée via le constructeur'
    private JwtService jwtService;


    @PostMapping("/register")
    public void register(@RequestBody User user) {
        this.userService.register(user);
    }

    @PostMapping(path = "activation") // Mappe les requêtes HTTP POST à l'URL /activation à cette méthode
    public void activation(@RequestBody Map<String, String> activation) { // Déclare la méthode activation qui prend une map de chaînes en entrée
        this.userService.activation(activation); // Appelle la méthode activation du service UtilisateurService
    }

    @PostMapping(path = "login") // Mappe les requêtes HTTP POST à l'URL /connexion à cette méthode
    public Map<String, String> login(@RequestBody AuthentificationDTO authentificationDTO) { // Déclare la méthode connexion qui prend un objet AuthentificationDTO en entrée et retourne une map de chaînes
        final Authentication authenticate = authenticationManager.authenticate( // Authentifie l'utilisateur avec le gestionnaire d'authentification
                new UsernamePasswordAuthenticationToken( // Crée un nouveau token d'authentification avec le nom d'utilisateur et le mot de passe
                        authentificationDTO.username(), // Récupère le nom d'utilisateur du DTO
                        authentificationDTO.password() // Récupère le mot de passe du DTO
                )
        );
        if (authenticate.isAuthenticated()){
            return this.jwtService.generate(authentificationDTO.username());
        };
        return null; // Retourne null si l'authentification échoue

    }


    @PostMapping(path = "/logout") // Mappe les requêtes HTTP POST à l'URL /activation à cette méthode
    public void logout() { // Déclare la méthode activation qui prend une map de chaînes en entrée
        this.jwtService.logout(); // Appelle la méthode activation du service UtilisateurService
    }



    @PostMapping(path = "/resetPassWord") // Mappe les requêtes HTTP POST à l'URL /activation à cette méthode
    public void resetPassWord(@RequestBody Map<String, String> activation) { // Déclare la méthode activation qui prend une map de chaînes en entrée
        this.userService.resetPassWord(activation); // Appelle la méthode activation du service UtilisateurService
    }

    @PostMapping(path = "/newPassWord") // Mappe les requêtes HTTP POST à l'URL /activation à cette méthode
    public void newPassWord(@RequestBody Map<String, String> activation) { // Déclare la méthode activation qui prend une map de chaînes en entrée
        this.userService.newPassWord(activation); // Appelle la méthode activation du service UtilisateurService
    }

    @PostMapping(path = "/refresh-token") // Mappe les requêtes HTTP POST à l'URL /activation à cette méthode
    public @ResponseBody Map<String, String>  refreshToken(@RequestBody Map<String, String> refreshTokenRequest) { // Déclare la méthode activation qui prend une map de chaînes en entrée
        return this.jwtService.refreshToken(refreshTokenRequest); // Appelle la méthode activation du service UtilisateurService
    }




}
