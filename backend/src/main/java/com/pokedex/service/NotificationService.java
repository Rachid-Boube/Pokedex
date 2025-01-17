package com.pokedex.service;


import com.pokedex.entite.Validation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static java.lang.String.format;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;

    public void envoyerNotification(Validation validation) { // Méthode pour envoyer une notification par email.
        SimpleMailMessage mailMessage = new SimpleMailMessage(); // Crée une nouvelle instance de SimpleMailMessage pour configurer le message email.
        mailMessage.setFrom("no-reply@tech.com"); // Définit l'adresse email de l'expéditeur.
        mailMessage.setTo(validation.getUser().getEmail()); // Définit l'adresse email du destinataire en utilisant l'email de l'utilisateur associé à la validation.
        mailMessage.setSubject("Votre Code de validation"); // Définit le sujet de l'email.

        String text = format("Bonjour %s, <br/> votre code d'activation est %s; A bientot", // Formate le corps de l'email avec le nom de l'utilisateur et le code de validation.
                validation.getUser().getUsername(), // Récupère le nom de l'utilisateur associé à la validation.
                validation.getCode() // Récupère le code de validation.
        );
        mailMessage.setText(text); // Définit le texte du message email.

        javaMailSender.send(mailMessage); // Envoie l'email en utilisant JavaMailSender.
    }
}