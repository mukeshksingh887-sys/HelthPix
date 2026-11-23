package com.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Component
public class RoleFilter {

    public GatewayFilter hasRole(String... allowedRoles) {

        return (exchange, chain) -> {

            String role =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst("X-ROLE");

            log.info(" role filter role is ::: {}",role);

            if (role == null) {

                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);

                return exchange.getResponse().setComplete();
            }

            boolean authorized =
                    Arrays.stream(allowedRoles)
                            .anyMatch(r ->
                                    r.equalsIgnoreCase(role));


            log.info(" role Authorized   :::  {}",authorized);

            if (!authorized) {

                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);

                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }
}