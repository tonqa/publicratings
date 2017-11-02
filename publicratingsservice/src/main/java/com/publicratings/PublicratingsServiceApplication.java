package com.publicratings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class PublicratingsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PublicratingsServiceApplication.class, args);
	}
}
