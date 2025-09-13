package com.hhnatsiuk.api_auth_service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.hhnatsiuk.api_auth_service",
        "com.hhnatsiuk.api_auth_adapter_db",
        "com.hhnatsiuk.api_auth_core"
})

@EnableJpaRepositories(basePackages = "com.hhnatsiuk.api_auth_adapter_db.repository")
@EntityScan(basePackages = {
        "com.hhnatsiuk.api_auth_core.domain.entity",
        "com.hhnatsiuk.api_auth_core.outbox"
})
@EnableScheduling
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
