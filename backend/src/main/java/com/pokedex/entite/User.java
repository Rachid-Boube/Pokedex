package com.pokedex.entite;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    private String username;

    private String email;

    private String password;


    private boolean actif = false; // Indique si l'utilisateur est actif ou non


    @DBRef // Relation entre Utilisateur et Role (référence vers un rôle)
    private Role role; // Le rôle de l'utilisateur


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Permet de gérer les autorités (ici on ajoute un préfixe ROLE_ pour Spring Security)
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.getLibelle()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }
}
