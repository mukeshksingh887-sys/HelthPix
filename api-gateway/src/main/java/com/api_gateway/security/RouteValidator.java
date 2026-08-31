package com.api_gateway.security;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints =
            List.of(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/api/auth/refresh"
            );

    public boolean isSecured(String path) {

        return openApiEndpoints
                .stream()
                .noneMatch(path::contains);
    }
}
