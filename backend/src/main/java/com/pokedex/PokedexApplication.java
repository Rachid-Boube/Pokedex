package com.pokedex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.SpringSecurityMessageSource;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class PokedexApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokedexApplication.class, args);
	}

}
