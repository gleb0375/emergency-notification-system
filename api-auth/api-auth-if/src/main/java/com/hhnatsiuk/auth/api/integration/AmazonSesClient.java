package com.hhnatsiuk.auth.api.integration;

public interface AmazonSesClient {
    void sendVerification(String to, String token, int ttlMin);
}
