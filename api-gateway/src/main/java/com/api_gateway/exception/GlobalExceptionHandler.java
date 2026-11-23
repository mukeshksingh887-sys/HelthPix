package com.api_gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        HttpStatus status;
        String message;

        if (ex instanceof TokenExpiredException) {

            status = HttpStatus.UNAUTHORIZED;
            message = ex.getMessage();

        } else if (ex instanceof InvalidTokenException) {

            status = HttpStatus.UNAUTHORIZED;
            message = ex.getMessage();

        } else if (ex instanceof UnauthorizedException) {

            status = HttpStatus.UNAUTHORIZED;
            message = ex.getMessage();

        } else {

            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Internal Server Error";
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .status(status.value())
                .path(exchange.getRequest().getPath().value())
                .timestamp(java.time.LocalDateTime.now())
                .build();

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        try {

            byte[] responseBody =
                    objectMapper.writeValueAsBytes(errorResponse);

            return exchange.getResponse().writeWith(
                    Mono.just(
                            exchange.getResponse()
                                    .bufferFactory()
                                    .wrap(responseBody)
                    )
            );

        } catch (Exception e) {

            return Mono.error(e);
        }
    }
}