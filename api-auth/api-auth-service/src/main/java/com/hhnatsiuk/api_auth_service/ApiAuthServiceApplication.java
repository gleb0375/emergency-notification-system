package com.hhnatsiuk.api_auth_service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ApiAuthServiceApplication {

    private static final Logger logger = LogManager.getLogger(ApiAuthServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiAuthServiceApplication.class, args);
        logger.info("Application context of Auth Service initialization completed.");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Initializing BCryptPasswordEncoder bean.");
        return new BCryptPasswordEncoder();
    }

}
