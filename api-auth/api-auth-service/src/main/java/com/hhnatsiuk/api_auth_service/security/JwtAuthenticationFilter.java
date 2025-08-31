package com.hhnatsiuk.api_auth_service.security;

import com.hhnatsiuk.auth.api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtService.resolveToken(request);

        if (token == null) {
            logger.debug("No JWT token found in request.");
            filterChain.doFilter(request, response);
            return;
        }

        logger.debug("JWT token found: {}", token);

        if(!jwtService.validateAccessToken(token)) {
            logger.warn("Invalid JWT token: {}", token);
            filterChain.doFilter(request, response);
            return;
        }

        logger.debug("JWT token is valid. Extracting user information...");

        String email = jwtService.extractEmail(token);
        List<String> roles = jwtService.extractRoles(token);

        logger.debug("User: {}, Roles: {}", email, roles);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email, null, authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("User '{}' authenticated successfully.", email);

        filterChain.doFilter(request, response);
    }
}
