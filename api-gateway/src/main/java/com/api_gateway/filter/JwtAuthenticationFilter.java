package com.api_gateway.filter;

import com.api_gateway.exception.InvalidTokenException;
import com.api_gateway.exception.TokenExpiredException;
import com.api_gateway.exception.UnauthorizedException;
import com.api_gateway.util.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/api/auth/")) {
            return chain.filter(exchange);
        }

        if(path.startsWith("/api/users/create")){
            return  chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        log.info("auth herader :::: {}",authHeader);

//        if (authHeader == null ||
//                !authHeader.startsWith("Bearer ")) {
//
//            exchange.getResponse()
//                    .setStatusCode(HttpStatus.UNAUTHORIZED);
//
//            return exchange.getResponse().setComplete();
//        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException(
                    "Authorization header is missing"
            );
        }
        String token = authHeader.substring(7);



//        if (!jwtService.validateToken(token)) {
//
//            exchange.getResponse()
//                    .setStatusCode(HttpStatus.UNAUTHORIZED);
//
//            return exchange.getResponse().setComplete();
//        }


        if (!jwtService.validateToken(token)) {
            throw new InvalidTokenException(
                    "Invalid JWT Token"
            );
        }

        if (jwtService.isTokenExpired(token)) {
            throw new TokenExpiredException(
                    "JWT Token has expired"
            );
        }


        Claims claims =
                jwtService.extractClaims(token);
        log.info(" climes :::::{}",claims);

        String userId =
                String.valueOf(claims.get("userId"));

        String role =
                String.valueOf(claims.get("role"));

        String email =
                String.valueOf(claims.get("email"));

        ServerHttpRequest request =
                exchange.getRequest()
                        .mutate()
                        .header("X-USER-ID", userId)
                        .header("X-ROLE", role)
                        .header("X-EMAIL", email)
                        .build();

        return chain.filter(
                exchange.mutate()
                        .request(request)
                        .build()
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}