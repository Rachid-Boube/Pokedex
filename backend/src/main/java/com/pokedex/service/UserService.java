package com.pokedex.service;


import com.pokedex.entite.Role;
import com.pokedex.entite.TypeRole;
import com.pokedex.entite.User;
import com.pokedex.entite.Validation;
import com.pokedex.repository.RoleRepository;
import com.pokedex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;


    public  void register(User user){
        // Validation de l'email
        if(!user.getEmail().contains("@")){ // Vérifie que l'email contient un '@'.
            throw new RuntimeException("Votre adresse e-mail est invalide."); // Lance une exception si l'email est invalide.
        }
        if(!user.getEmail().contains(".")){ // Vérifie que l'email contient un '.com'.
            throw new RuntimeException("Votre adresse e-mail doit contenir un point '.'"); // Lance une exception si l'email est invalide.
        }

        // Vérifier si l'email existe déjà
        Optional<User> utilisateurOptional = this.userRepository.findByEmail(user.getEmail());
        if(utilisateurOptional.isPresent()){ // Vérifie si un utilisateur avec cet email existe déjà.
            throw new RuntimeException("Votre adresse e-mail existe déjà."); // Lance une exception si l'email existe déjà.
        }

        // Encoder le mot de passe
        String mdpCrypte = this.passwordEncoder.encode(user.getPassword()); // Encode le mot de passe de l'utilisateur.
        user.setPassword(mdpCrypte); // Définit le mot de passe encodé pour l'utilisateur.

        // Assigner un rôle par défaut
        Role roleUtilisateur = roleRepository.findByLibelle(TypeRole.USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setLibelle(TypeRole.USER);
                    return roleRepository.save(newRole);
                });
        user.setRole(roleUtilisateur);


        // Enregistrer l'utilisateur dans le dépôt
        this.userRepository.save(user);


        this.validationService.enregistrer(user);

    }


    public void activation(Map<String, String> activation) { // Méthode pour activer un utilisateur avec un code d'activation.

        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code")); // Récupère la validation en fonction du code d'activation.
        if(Instant.now().isAfter(validation.getExpiration())){ // Vérifie si la validation a expiré.
            throw new RuntimeException("Votre code  d'activation a expiré."); // Lance une exception si la validation a expiré.
        }
        // Active la validation
        this.validationService.activerValidation(activation.get("code"));
        // Récupère l'utilisateur associé à la validation.'
        User user = this.userRepository.findById(validation.getUser().getId()).orElseThrow(()->new RuntimeException("L'utilisateur d'activation"));
        user.setActif(true); // Définit l'utilisateur comme actif.
        this.userRepository.save(user); // Sauvegarde l'utilisateur mis à jour dans le dépôt.

    }

    public User findByUsername(String username) {
        return this.userRepository
                .findByEmail(username) // Suppose que le username correspond à l'email de l'utilisateur.
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Méthode pour charger un utilisateur par son nom d'utilisateur (email).
        return this.userRepository
                .findByEmail(username) // Cherche l'utilisateur par email dans le dépôt.
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu")); // Lance une exception si l'utilisateur n'est pas trouvé.
    }

    public void resetPassWord(Map<String, String> parametres) {
        User user = (User) this.loadUserByUsername(parametres.get("email"));
        this.validationService.enregistrer(user); // Génère un code et envoie un e-mail pour confirmation
        // Message ou retour pour indiquer à l'utilisateur que le code d'activation a été envoyé par email
    }


    public void newPassWord(Map<String, String> parametres) {
        User user = (User)  this.loadUserByUsername(parametres.get("email"));
        final Validation validation = validationService.lireEnFonctionDuCode(parametres.get("code"));

        // Vérifie que le code est valide et non expiré
        if (!validation.getUser().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("L'utilisateur ne correspond pas à cette validation.");
        }
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Le code de validation a expiré.");
        }

        // Met à jour le mot de passe et enregistre
        String mdpCrypte = this.passwordEncoder.encode(parametres.get("password"));
        user.setPassword(mdpCrypte);
        this.userRepository.save(user);
    }
}
