package com.hhnatsiuk.api_auth_service.impl.integration;

import com.hhnatsiuk.auth.api.integration.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "email", name = "enabled", havingValue = "true")
public class SmtpEmailClientImpl implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailClientImpl.class);

    private final JavaMailSender mailSender;
    private final String from;

    public SmtpEmailClientImpl(
            JavaMailSender mailSender,
            @Value("${SMTP_FROM}") String from
    ) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void sendVerification(String to, String token, int ttlMin) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            log.error("FROM raw='{}'", from);
            log.error("FROM length={}", from == null ? -1 : from.length());

            if (from != null) {
                for (int i = 0; i < from.length(); i++) {
                    log.error("FROM char[{}]=U+{}", i, Integer.toHexString(from.charAt(i)));
                }
            }

            log.error("TO raw='{}'", to);
            log.error("TO length={}", to == null ? -1 : to.length());

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Your verification code");
            helper.setText(
                    "Hello,\n\nYour verification code is: "
                            + token + "\nIt expires in " + ttlMin + " minutes.",
                    false
            );

            mailSender.send(message);
            log.info("SMTP email sent to {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send SMTP email to {}", to, e);
            throw new RuntimeException(e);
        }
    }
}
