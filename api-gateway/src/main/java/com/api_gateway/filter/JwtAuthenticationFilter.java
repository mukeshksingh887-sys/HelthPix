package com.api_gateway.filter;

import com.api_gateway.util.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


//@Component
//@RequiredArgsConstructor
//public class JwtAuthorizationFilter
//        implements GlobalFilter {
//
//    private final JwtService jwtService;
//
//    @Override
//    public Mono<Void> filter(
//            ServerWebExchange exchange,
//            org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
//
//        String path = exchange.getRequest()
//                .getURI()
//                .getPath();
//
//        // Public API
//        if (path.startsWith("/auth/")) {
//            return chain.filter(exchange);
//        }
//
//        String authorization =
//                exchange.getRequest()
//                        .getHeaders()
//                        .getFirst(HttpHeaders.AUTHORIZATION);
//
//        // JWT missing
//        if (authorization == null
//                || !authorization.startsWith("Bearer ")) {
//
//            return unauthorized(exchange);
//        }
//
//        String token =
//                authorization.substring(7);
//
//
//
//        // JWT invalid
//        if (!jwtService.validateToken(token)) {
//
//            return unauthorized(exchange);
//        }
//
//
//
//        Claims claims =
//                jwtService.extractClaims(token);
//
//        String username =
//                claims.getSubject();
//
//        List<String> roles =
//                claims.get("role", List.class);
//
//        // Authorization
//        if (!hasPermission(path, roles)) {
//
//            return forbidden(exchange);
//        }
//
//        // Forward authenticated user information
//        ServerHttpRequest request =
//                exchange.getRequest()
//                        .mutate()
//                        .header("X-User", username)
//                        .header(
//                                "X-Roles",
//                                String.join(",", roles))
//                        .build();
//
//        return chain.filter(
//                exchange.mutate()
//                        .request(request)
//                        .build()
//        );
//    }
//
//    private boolean hasPermission(
//            String path,
//            List<String> roles) {
//
//        if (path.startsWith("/api/admin/")) {
//
//            return roles.contains("ADMIN");
//        }
//
//        if (path.startsWith("/api/doctors/")) {
//
//            return roles.contains("DOCTOR")
//                    || roles.contains("ADMIN");
//        }
//
//        if (path.startsWith("/api/patients/")) {
//
//            return roles.contains("PATIENT")
//                    || roles.contains("DOCTOR")
//                    || roles.contains("ADMIN");
//        }
//
//        if (path.startsWith("/api/appointments/")) {
//
//            return roles.contains("PATIENT")
//                    || roles.contains("DOCTOR")
//                    || roles.contains("ADMIN");
//        }
//
//        return true;
//    }
//
//    private Mono<Void> unauthorized(
//            ServerWebExchange exchange) {
//
//        exchange.getResponse()
//                .setStatusCode(
//                        HttpStatus.UNAUTHORIZED);
//
//        return exchange.getResponse()
//                .setComplete();
//    }
//
//    private Mono<Void> forbidden(
//            ServerWebExchange exchange) {
//
//        exchange.getResponse()
//                .setStatusCode(
//                        HttpStatus.FORBIDDEN);
//
//        return exchange.getResponse()
//                .setComplete();
//    }
//}






@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private  JwtService jwtService;

    private static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/api/auth/forgot-password",
            "/api/auth/reset-password"
//            "/api/user/getAll"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        boolean isPublic = PUBLIC_URLS.stream().anyMatch(path::startsWith);

        if (isPublic) {
            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        log.info(" auth header :  -----  {}",authHeader);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        log.info(" auth token :  -----  {}",token);

        if (!jwtService.validateToken(token)) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        Claims claims = jwtService.extractClaims(token);

        log.info(" auth climes  :  -----  {}",claims);

        String userId = claims.get("userId").toString();

        String email = claims.get("email").toString();

        String role = claims.get("role").toString();

        ServerWebExchange modifiedExchange =
                exchange.mutate()
                        .request(r -> r.headers(headers -> {
                            headers.add("X-USER-ID", userId);
                            headers.add("X-EMAIL", email);
                            headers.add("X-ROLE", role);
                        }))
                        .build();

        return chain.filter(modifiedExchange);
    }


}