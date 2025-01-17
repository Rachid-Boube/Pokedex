package com.pokedex.controller;


import com.pokedex.entite.User;
import com.pokedex.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path ="/auth",consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        this.userService.register(user);
    }

}
