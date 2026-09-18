package com.api_gateway.config;

import com.api_gateway.filter.JwtAuthenticationFilter;
import com.api_gateway.filter.RoleFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class GatewayConfig {

    @Autowired
    private  JwtAuthenticationFilter jwtFilter;
    @Autowired
    private RoleFilter roleFilter;

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder) {

        return builder.routes()

                .route("AUTH-SERVICE",
                        r -> r.path("/api/auth/**")
                                .uri("lb://AUTH-SERVICE"))

                .route("USER-SERVICE",
                        r -> r.path("/api/users/**")
//                                .filters(f -> {
//                                    f.filter(jwtFilter);
//                                    f.filter(roleFilter.hasRole("DOCTOR"));
//                                    return f;
//                                })
                                .uri("lb://USER-SERVICE"))

                .route("PATIENT-SERVICE",
                        r -> r.path("/api/patients/**")
//                                .filters(f -> {
//                                    f.filter(jwtFilter);
//                                    f.filter(roleFilter.hasRole("DOCTOR"));
//                                    return f;
//                                })
                                .uri("lb://PATIENT-SERVICE"))

                .route("DOCTOR-SERVICE",
                        r -> r.path("/api/doctors/**")
//                                .filters(f -> f.filter(jwtFilter))
                                .uri("lb://DOCTOR-SERVICE"))

                .route("APPOINTMENT-SERVICE",
                        r -> r.path("/api/appointments/**")
//                                .filters(f -> {
//                                    f.filter(jwtFilter);
//                                    f.filter(roleFilter.hasRole("DOCTOR"));
//                                    f.filter(roleFilter.hasRole("PATIENT"));
//                                    return f;
//                                })
                                .uri("lb://APPOINTMENT-SERVICE"))
                .build();
    }
}