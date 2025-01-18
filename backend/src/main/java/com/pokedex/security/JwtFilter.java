package com.pokedex.security;


import com.pokedex.entite.Jwt;
import com.pokedex.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Marquer cette classe comme un service Spring
@Service
public class JwtFilter extends OncePerRequestFilter {

    // Déclarer les dépendances
    private final UserService userService;
    private final JwtService jwtService;

    // Constructeur pour initialiser les dépendances
    public JwtFilter(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // Surcharger la méthode doFilterInternal pour ajouter une logique de filtrage personnalisée
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        String username = null;
        String token ;
        Jwt tokenDansLaDB = null;
        boolean isTokenExpired = true; // Initialiser le statut d'expiration du token


        // Obtenir l'en-tête Authorization de la requête
        final String authorization = request.getHeader("Authorization");
        // Vérifier si l'en-tête Authorization n'est pas null et commence par "Bearer "
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7); // Extraire le token de l'en-tête
            tokenDansLaDB = this.jwtService.tokenByValue(token);
            username = jwtService.extractUsername(token); // Extraire le nom d'utilisateur du token
            isTokenExpired = jwtService.isTokenExpired(token); // Vérifier si le token est expiré

        }

        if (
                !isTokenExpired
                        && tokenDansLaDB.getUser().getEmail().equals(username)
                        && SecurityContextHolder.getContext().getAuthentication() == null

        ) {
            UserDetails userDetails = userService.loadUserByUsername(username); // Charger les détails de l'utilisateur par nom d'utilisateur
            // Créer un token d'authentification avec les détails de l'utilisateur
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // Définir le token d'authentification dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
