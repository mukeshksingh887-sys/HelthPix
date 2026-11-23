package com.api_gateway.config;

import com.api_gateway.filter.RoleFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final RoleFilter roleFilter;

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder) {

        return builder.routes()

                .route("AUTH-SERVICE",
                        r -> r.path("/api/auth/**")
                                .uri("lb://AUTH-SERVICE"))

                .route("USER-SERVICE",
                        r -> r.path("/api/users/create")
                                .uri("lb://USER-SERVICE"))

                .route("USER-SERVICE",
                        r -> r.path("/api/users/**")
                                .filters(f ->
                                        f.filter(
                                                roleFilter.hasRole("ADMIN", "PATIENT","DOCTOR")))
                                .uri("lb://USER-SERVICE"))

                .route("PATIENT-SERVICE",
                        r -> r.path("/api/patients/**")
                                .filters(f ->
                                        f.filter(roleFilter.hasRole("ADMIN", "DOCTOR", "PATIENT")))
                                .uri("lb://PATIENT-SERVICE"))

                .route("DOCTOR-SERVICE",
                        r -> r.path("/api/doctors/**")
                                .filters(f ->
                                        f.filter(roleFilter.hasRole("ADMIN", "DOCTOR", "PATIENT")))
                                .uri("lb://DOCTOR-SERVICE"))

                .route("APPOINTMENT-SERVICE",
                        r -> r.path("/api/appointments/**")
                                .filters(f ->
                                        f.filter(roleFilter.hasRole("ADMIN", "DOCTOR", "PATIENT")))
                                .uri("lb://APPOINTMENT-SERVICE"))

                .build();
    }
}