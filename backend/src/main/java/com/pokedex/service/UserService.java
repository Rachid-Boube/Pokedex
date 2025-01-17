package com.pokedex.service;


import com.pokedex.entite.Role;
import com.pokedex.entite.TypeRole;
import com.pokedex.entite.User;
import com.pokedex.repository.RoleRepository;
import com.pokedex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

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
        Optional<User> utilisateurOptional = this.userRepository.findByEmail(user.getEmail()); // Cherche un utilisateur avec le même email dans le dépôt.
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




}
