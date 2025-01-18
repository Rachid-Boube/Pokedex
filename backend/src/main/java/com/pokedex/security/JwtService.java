package com.pokedex.security;


import com.pokedex.entite.Jwt;
import com.pokedex.entite.RefreshToken;
import com.pokedex.entite.User;
import com.pokedex.repository.JwtRepository;
import com.pokedex.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor // Génère un constructeur avec tous les arguments grâce à Lombok
@Service
public class JwtService {

    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";
    private UserService  userService;
    private JwtRepository jwtRepository;


    public Jwt tokenByValue(String value) {
        return this.jwtRepository.findByValeurAndDesactiveAndExpire(
                value,
                false,
                false
        ).orElseThrow(()->new RuntimeException("token not found ou invalide"));
    }

    public Map<String, String> generate(String username ){
        User user = (User) this.userService.loadUserByUsername(username); // Charge l'utilisateur par nom d'utilisateur
        this.disableTokens(user); // Désactive les tokens existants pour l'utilisateur'
        Map<String,String> jwtMap = new java.util.HashMap<>(this.generateJwt(user));

        RefreshToken refreshToken = RefreshToken.builder()
                .valeur(UUID.randomUUID().toString())
                .expire(false)
                .creation(Instant.now())
                .expiration(Instant.now().plusMillis(30*60*1000))
                .build();

        final Jwt jwt = Jwt
                .builder()
                .valeur(jwtMap.get(BEARER))
                .desactive(false)
                .expire(false)
                .user(user)
                .refreshToken(refreshToken)
                .build();

        this.jwtRepository.save(jwt); // Enregistre le JWT dans le dépôt
        jwtMap.put(REFRESH,refreshToken.getValeur());
        return jwtMap; // Génère et retourne le token JWT


    }

    private void disableTokens(User user){
        final List<Jwt> jwtList = this.jwtRepository.findUtilisateur(user.getEmail()).peek(
                jwt ->{
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());

        this.jwtRepository.saveAll(jwtList);
    }

    public String extractUsername(String token) { // Méthode pour extraire le nom d'utilisateur d'un token JWT
        return this.getClaim(token, Claims::getSubject); // Utilise la méthode getClaim pour obtenir le sujet (nom d'utilisateur)
    }


    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate != null && expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) { // Méthode pour obtenir la date d'expiration d'un token JWT
        return this.getClaim(token, Claims::getExpiration); // Utilise la méthode getClaim pour obtenir la date d'expiration
    }

    private <T> T getClaim(String token, Function<Claims, T> function) { // Méthode générique pour obtenir une réclamation (claim) d'un token JWT
        Claims claims = getAllClaims(token); // Obtient toutes les réclamations du token
        return function.apply(claims); // Applique la fonction fournie aux réclamations
    }

    private Claims getAllClaims(String token) { // Méthode pour obtenir toutes les réclamations d'un token JWT
        return Jwts.parser() // Crée un parser JWT
                .setSigningKey(this.getKey()) // Définit la clé de signature
                .setAllowedClockSkewSeconds(3600) // Tolère un décalage d'une heure
                .build() // Construit le parser
                .parseClaimsJws(token) // Parse le token JWT
                .getBody(); // Retourne le corps des réclamations
    }

    private Map<String, String> generateJwt(User user) { // Méthode pour générer un token JWT pour un utilisateur
        final long currentTime = System.currentTimeMillis(); // Obtient le temps actuel en millisecondes
        final long expirationTime = currentTime + 1000 * 60 ;


        final Map<String, Object> claims = Map.of( // Crée un map des réclamations
                "nom", user.getUsername(), // Ajoute le nom de l'utilisateur
                Claims.SUBJECT, user.getEmail(), // Ajoute l'email de l'utilisateur comme sujet
                Claims.EXPIRATION, new Date(expirationTime)
        );

        final String bearer = Jwts.builder() // Crée un builder JWT
                .setIssuedAt(new Date(currentTime)) // Définit la date d'émission
                .setExpiration(new Date(expirationTime)) // Définit la date d'expiration
                .setSubject(user.getEmail()) // Définit le sujet (email de l'utilisateur)
                .setClaims(claims) // Définit les réclamations
                .signWith(getKey(), SignatureAlgorithm.HS256) // Signe le token avec la clé et l'algorithme HS256
                .compact(); // Compacte le token en une chaîne

        return Map.of(BEARER, bearer); // Retourne le token JWT sous forme de map
    }

    private Key getKey() { // Méthode pour obtenir la clé de signature
        // Clé de chiffrement pour les tokens JWT
        String ENCRYPTION_KEY = "0cc91aaf940989e869df01f420c9ff966c2c24023c3dde00a2de33009a0e31aa";
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY); // Décode la clé de chiffrement en base64
        return Keys.hmacShaKeyFor(decoder); // Retourne la clé HMAC-SHA
    }


    public void logout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findByUtilisateurValidToken(
                user.getEmail(),
                false,
                false
        ).orElseThrow(() -> new RuntimeException("token is not valid"));

        jwt.setExpire(true);
        jwt.setDesactive(true);

        this.jwtRepository.save(jwt);
    }

    @Scheduled(cron = "@daily")
    public void removeUselessJwt(){
        log.info("Suppression des tokens a {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactive(true, true);
    }

    public Map<String, String>  refreshToken(Map<String, String> refreshTokenRequest) {
        final Jwt jwt = this.jwtRepository.findByRefreshToken(refreshTokenRequest.get(REFRESH)).orElseThrow(() -> new RuntimeException("token is not valid"));
        if(jwt.getRefreshToken().isExpire() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())) {
            throw new RuntimeException("token is not valid");
        }
        Map<String, String> tokens = this.generate(jwt.getUser().getEmail());
        this.disableTokens(jwt.getUser());
        return tokens;
    }

}
