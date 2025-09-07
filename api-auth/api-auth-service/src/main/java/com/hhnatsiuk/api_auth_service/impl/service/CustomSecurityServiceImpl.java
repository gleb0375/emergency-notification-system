package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import com.hhnatsiuk.auth.api.service.CustomSecurityService;
import com.hhnatsiuk.auth.api.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomSecurityServiceImpl implements CustomSecurityService {

    private final UserService userService;

    public CustomSecurityServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isCurrentUser(String userUuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthAccountEntity currentUser = userService.findUserByEmail(email);

        return currentUser.getUuid().equals(userUuid);
    }

    @Override
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            return authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
        }
        return false;
    }
}
