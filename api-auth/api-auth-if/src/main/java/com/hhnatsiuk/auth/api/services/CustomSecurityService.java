package com.hhnatsiuk.auth.api.services;

public interface CustomSecurityService {

    boolean isCurrentUser(String userUuid);

    boolean hasRole(String role);
}
