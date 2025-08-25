package com.hhnatsiuk.auth.api.services;

import javax.crypto.SecretKey;

public interface CryptoUtilService {

    String hashToken(String token);

    SecretKey getAccessTokenSecretKey();

    SecretKey getRefreshTokenKey();

    long getAccessTokenExpirationInMs();

    long getRefreshTokenExpirationInMs();

}
