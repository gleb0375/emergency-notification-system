package com.hhnatsiuk.auth.api.integration;

public interface EmailSender {
    void sendVerification(String to, String token, int ttlMin);
}
