package com.hhnatsiuk.api_contact_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.hhnatsiuk.api_contact_service",
        "com.hhnatsiuk.api_contact_adapter_db",
        "com.hhnatsiuk.api_contact_core"
})
@EnableJpaRepositories(basePackages = "com.hhnatsiuk.api_contact_adapter_db.repository")
@EntityScan(basePackages = "com.hhnatsiuk.api_contact_core.domain.entity")
public class ApiContactServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiContactServiceApplication.class, args);
    }

}
