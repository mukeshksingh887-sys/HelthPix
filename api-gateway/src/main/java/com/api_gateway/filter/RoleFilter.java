package com.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RoleFilter {

    public GatewayFilter hasRole(String role) {

        return (exchange, chain) -> {

            String userRole =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst("X-ROLE");

            if (!role.equals(userRole)) {

                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);

                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }
}