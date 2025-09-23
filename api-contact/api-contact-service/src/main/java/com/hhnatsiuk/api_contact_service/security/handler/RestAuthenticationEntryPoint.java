package com.hhnatsiuk.api_contact_service.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhnatsiuk.api_contact_if.model.generated.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper om;

    public RestAuthenticationEntryPoint(ObjectMapper om) { this.om = om; }

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request,
                         HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) {
        write(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private void write(HttpServletResponse resp, int status, String message) {
        try {
            resp.setStatus(status);
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            om.writeValue(resp.getWriter(), new ErrorResponseDTO().code(status).message(message));
        } catch (Exception ignored) {}
    }
}
