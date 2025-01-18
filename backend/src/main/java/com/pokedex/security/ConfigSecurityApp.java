package com.pokedex.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
@Configuration
public class ConfigSecurityApp {

    private final BCryptPasswordEncoder bcryptPasswordEncoder;
    private final JwtFilter jwtFilter;

    public ConfigSecurityApp(JwtFilter jwtFilter, BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.jwtFilter = jwtFilter;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return
                httpSecurity
                        // Désactive la protection CSRF (Cross-Site Request Forgery)
                        .csrf(AbstractHttpConfigurer::disable)
                        // Configure les autorisations des requêtes HTTP
                        .authorizeHttpRequests(
                                authorize ->
                                        // Permet les requêtes POST vers /inscription sans authentification
                                        authorize.requestMatchers(POST, "/auth/register").permitAll()
                                                // Permet les requêtes POST vers /activation sans authentification
                                                .requestMatchers(POST, "/auth/activation").permitAll()
                                                // Permet les requêtes POST vers /connexion sans authentification
                                                .requestMatchers(POST, "/auth/login").permitAll()
                                                .requestMatchers(POST, "/resetPassWord").permitAll()
                                                .requestMatchers(POST, "/newPassWord").permitAll()
                                                .requestMatchers(POST, "/refresh-token").permitAll()
                                                // Permet les requêtes GET vers /api/utilisateurs.requestMatchers(GET, "/avis").hasRole("ADMINISTRATEUR")

                                                // Exige une authentification pour toutes les autres requêtes
                                                .anyRequest().authenticated()
                        )
                        .sessionManagement(httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }



    @Bean  // Déclare un bean pour le gestionnaire d'authentification
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Retourne le gestionnaire d'authentification à partir de la configuration d'authentification
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean // Déclare un bean pour le fournisseur d'authentification
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        // Crée une instance de DaoAuthenticationProvider
        DaoAuthenticationProvider daoauthenticationProvider = new DaoAuthenticationProvider();
        // Configure le fournisseur d'authentification avec le service de détails utilisateur
        daoauthenticationProvider.setUserDetailsService(userDetailsService);
        // Configure le fournisseur d'authentification avec l'encodeur de mot de passe BCrypt
        daoauthenticationProvider.setPasswordEncoder(bcryptPasswordEncoder);
        // Retourne l'instance configurée de DaoAuthenticationProvider
        return daoauthenticationProvider;
    }


}
