package com.roima.examinationSystem.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roima.examinationSystem.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {


    public void handleException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ApiResponse apiResponse = new ApiResponse("error", message);

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        if (authException instanceof BadCredentialsException) {
            handleException(response, "Wrong login credentials.");
        } else {
            handleException(response, "Authentication Failed: Please login to access this resource.");
        }

    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        handleException(response, "You do not have permission to access this resource.");
    }
}
