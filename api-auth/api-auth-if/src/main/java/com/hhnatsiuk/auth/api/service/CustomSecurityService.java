package com.hhnatsiuk.auth.api.service;

public interface CustomSecurityService {

    boolean isCurrentUser(String userUuid);

    boolean hasRole(String role);
}
