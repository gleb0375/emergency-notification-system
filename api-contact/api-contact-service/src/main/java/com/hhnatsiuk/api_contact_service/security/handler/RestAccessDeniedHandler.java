package com.hhnatsiuk.api_contact_service.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhnatsiuk.api_contact_if.model.generated.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper om;

    public RestAccessDeniedHandler(ObjectMapper om) { this.om = om; }

    @Override
    public void handle(jakarta.servlet.http.HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException) {
        write(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
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
