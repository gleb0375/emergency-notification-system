package com.hhnatsiuk.api_auth_service.impl.integration;

import com.hhnatsiuk.auth.api.integration.AmazonSesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Component
public class AmazonSesClientImpl implements AmazonSesClient {

    private static final Logger logger = LoggerFactory.getLogger(AmazonSesClientImpl.class);

    private final SesClient sesClient;
    private final String sourceEmail;

    public AmazonSesClientImpl(SesClient sesClient,  @Value("${aws.ses.sender}") String sourceEmail) {
        this.sesClient = sesClient;
        this.sourceEmail = sourceEmail;
    }

    @Override
    public void sendVerification(String to, String token, int ttlMin) {
        String subject = "Your verification code";
        String body    = String.format(
                "Hello,%n\nYour verification code is: %s%nIt expires in %d minutes.",
                token, ttlMin
        );

        SendEmailRequest req = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(to).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).charset("UTF-8").build())
                        .body(Body.builder()
                                .text(Content.builder().data(body).charset("UTF-8").build())
                                .build())
                        .build())
                .source(sourceEmail)
                .build();

        try {
            sesClient.sendEmail(req);
            logger.info("SES email sent to {}", to);
        } catch (SesException e) {
            logger.error("Failed to send SES email to {}: {}", to, e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
