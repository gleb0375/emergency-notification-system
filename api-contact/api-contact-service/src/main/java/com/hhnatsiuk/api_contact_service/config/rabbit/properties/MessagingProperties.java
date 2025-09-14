package com.hhnatsiuk.api_contact_service.config.rabbit.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "ens.messaging")
public class MessagingProperties {

    @NotNull
    private final Auth auth = new Auth();

    @NotNull
    private final Contact contact = new Contact();

    @Data
    public static class Auth {

        @NotBlank
        private String exchange;

        @NotNull
        private final Routing routing = new Routing();

        @Data
        public static class Routing {
            @NotBlank
            private String userCreated;

            @NotBlank
            private String userDeleted;
        }
    }

    @Data
    public static class Contact {
        @NotBlank
        private String queue;

        @NotBlank
        private String dlx;

        @NotBlank
        private String dlq;
    }
}
