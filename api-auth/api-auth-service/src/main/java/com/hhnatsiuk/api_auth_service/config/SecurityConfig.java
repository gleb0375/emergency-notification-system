package com.hhnatsiuk.api_auth_service.config;

import com.hhnatsiuk.api_auth_service.impl.service.ExtendedUserDetailsService;
import com.hhnatsiuk.api_auth_service.security.JwtAuthenticationFilter;
import com.hhnatsiuk.auth.api.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.GeneralSecurityException;
import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);
    private final JwtService jwtService;
    private final ExtendedUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtService jwtService, ExtendedUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        configuration.setAllowCredentials(allowCredentials);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws GeneralSecurityException, Exception {
        logger.debug("Initializing SecurityFilterChain...");
        JwtAuthenticationFilter jwtTokenFilter = new JwtAuthenticationFilter(jwtService);

        try {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .exceptionHandling(exception -> exception
                            .authenticationEntryPoint((request, response, authException) -> {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                            })
                            .accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                            })
                    )
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/ws/**").permitAll()

                            .requestMatchers(HttpMethod.POST, "/accounts").permitAll()

                            .requestMatchers(HttpMethod.POST, "/verifications").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/verifications/*").permitAll()

                            .requestMatchers(HttpMethod.POST,   "/sessions").permitAll()
                            .requestMatchers(HttpMethod.PATCH,  "/sessions/*").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/sessions/*").authenticated()

                            .requestMatchers(HttpMethod.GET,    "/users/*").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")

                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(new JwtAuthenticationFilter(jwtService),
                            UsernamePasswordAuthenticationFilter.class);


            logger.info("SecurityFilterChain configured successfully.");
            return http.build();
        } catch (Exception e) {
            logger.error("Error configuring the SecurityFilterChain", e);
            throw new GeneralSecurityException("Error configuring the security filter chain", e);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws GeneralSecurityException {
        logger.debug("Initializing AuthenticationManager...");
        try {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);

            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);

            logger.info("AuthenticationManager initialized successfully.");
            return authenticationManagerBuilder.build();
        } catch (Exception e) {
            logger.error("Error initializing the AuthenticationManager", e);
            throw new GeneralSecurityException("Error configuring the authentication manager", e);
        }
    }
}