package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.auth.api.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExtendedUserDetailsService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(ExtendedUserDetailsService.class);

    private final UserService userService;

    public ExtendedUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user by email: {}", email);

        AuthAccountEntity user = userService.findUserByEmail(email);

        if (user == null) {
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        logger.debug("User found: {}", user);

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toSet());


        logger.debug("Granted authorities for user {}: {}", email, authorities);

        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
