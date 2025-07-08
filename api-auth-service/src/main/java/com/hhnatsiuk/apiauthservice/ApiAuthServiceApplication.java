package com.hhnatsiuk.apiauthservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiAuthServiceApplication {

    private static final Logger logger = LogManager.getLogger(ApiAuthServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiAuthServiceApplication.class, args);
        logger.info("Application context of Auth Service initialization completed.");
    }

}
