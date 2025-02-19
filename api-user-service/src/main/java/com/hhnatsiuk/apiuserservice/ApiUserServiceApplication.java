package com.hhnatsiuk.apiuserservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ApiUserServiceApplication {

    private static final Logger logger = LogManager.getLogger(ApiUserServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiUserServiceApplication.class, args);
        logger.info("Application started successfully!");
    }

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        logger.info("*************** APPLICATION CONTEXT REFRESHED ***************");

        for (String beanName : event.getApplicationContext().getBeanDefinitionNames()) {
            logger.info("Bean registered: {}", beanName);
        }

        logger.info("Application context initialization completed.");
    }

}
