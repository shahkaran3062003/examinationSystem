//package com.roima.examinationSystem.customValidators;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.roima.examinationSystem.exception.InvalidENUMException;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Data;
//
//import java.io.IOException;
//import java.util.stream.Collectors;
//
//@WebFilter(urlPatterns = "/mcq/add")
//@Data
//public class CustomValidationFilter implements Filter {
//
//    private final ObjectMapper objectMapper;
//
//    public CustomValidationFilter(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//        // Initialization code, if needed
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String json = httpRequest.getReader().lines().collect(Collectors.joining());
//        try {
//            JsonNode rootNode = objectMapper.readTree(json);
//            validateJson(rootNode);
//        } catch (IOException e) {
//            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
//            return;
//        } catch (InvalidENUMException e) {
//            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
//            return;
//        }
//        chain.doFilter(request, response);
//    }
//
//    private void validateJson(JsonNode rootNode) throws InvalidENUMException {
//        JsonNode optionsNode = rootNode.get("options");
//
//        if (optionsNode != null && optionsNode.isArray()) {
//            for (JsonNode node : optionsNode) {
//                if (!node.isTextual()) {
//                    throw new InvalidENUMException("Options must be of type String");
//                }
//            }
//        } else {
//            throw new InvalidENUMException("Options must be a list of Strings");
//        }
//    }
//
//    @Override
//    public void destroy() {
//        // Cleanup code, if needed
//    }
//}
