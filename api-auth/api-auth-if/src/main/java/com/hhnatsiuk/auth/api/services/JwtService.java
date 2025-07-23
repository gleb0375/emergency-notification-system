package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.function.Function;

public interface JwtService {

    /**
     * Extracts the username (subject) from the given token.
     *
     * @param token JWT token
     * @return Extracted username
     */
    String extractUsername(String token);

    /**
     * Extracts a specific claim from the token using the provided claims resolver function.
     *
     * @param token JWT token
     * @param claimsResolver Function to resolve the claim
     * @param <T> Type of the extracted claim
     * @return Extracted claim value
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generates an access token for the given user.
     *
     * @param userDetails User entity containing user data
     * @return Generated JWT access token
     */
    String generateAccessToken(AuthAccountEntity userDetails);

    /**
     * Generates a refresh token for the given user.
     *
     * @param userDetails User entity containing user data
     * @return Generated JWT refresh token
     */
    String generateRefreshToken(AuthAccountEntity userDetails);

    /**
     * Validates the provided access token.
     *
     * @param token JWT access token
     * @return true if the token is valid, false otherwise
     */
    Boolean validateAccessToken(String token);

    /**
     * Validates the provided refresh token.
     *
     * @param token JWT refresh token
     * @return true if the token is valid, false otherwise
     */
    Boolean validateRefreshToken(String token);

    /**
     * Checks whether the given token has expired.
     *
     * @param token JWT token
     * @return true if the token has expired, false otherwise
     */
    Boolean isTokenExpired(String token);

    /**
     * Extracts the JWT token from the Authorization header in the HTTP request.
     *
     * @param request HTTP request containing the Authorization header
     * @return Extracted JWT token if present, otherwise null
     */
    String resolveToken(HttpServletRequest request);

    /**
     * Extracts the roles assigned to the user from the given token.
     *
     * @param token JWT token
     * @return List of role names
     */
    List<String> extractRoles(String token);
}
