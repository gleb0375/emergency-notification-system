package com.hhnatsiuk.auth.api.services;

public interface CryptoUtilService {

    /**
     * Hashes a given token using a secure algorithm.
     *
     * @param token the token to be hashed
     * @return the hashed token
     */
    String hashToken(String token);

    /**
     * Retrieves the access token secret key.
     *
     * @return the access token secret key
     */
    String getAccessTokenSecretKey();

    /**
     * Retrieves the refresh token secret key.
     *
     * @return the refresh token secret key
     */
    String getRefreshTokenSecretKey();

    /**
     * Retrieves the expiration time for access tokens in milliseconds.
     *
     * @return the access token expiration time
     */
    long getAccessTokenExpirationInMs();

    /**
     * Retrieves the expiration time for refresh tokens in milliseconds.
     *
     * @return the refresh token expiration time
     */
    long getRefreshTokenExpirationInMs();

}
