package com.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


//    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
//
//        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
//
////                .authorizeExchange(exchange -> exchange
////
////                        .pathMatchers("/api/auth/**", "/eureka/**").permitAll()
////                        .pathMatchers("/api/auth/**").permitAll()
////
////                        .pathMatchers("/api/user/create").permitAll()
////                        .pathMatchers("/api/user/getAll").hasRole("DOCTOR")
////
////                        .pathMatchers("/api/user/delete/**").hasRole("ADMIN")
////
////                        .pathMatchers("/api/user/update/**").hasAnyRole("ADMIN", "DOCTOR")
////
////                        .anyExchange().authenticated())
//
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .authorizeExchange(exchange ->
//                        exchange
//                                .anyExchange()
//                                .permitAll()
//                )
//
//                .build();
//    }
////









@Bean
public SecurityWebFilterChain securityWebFilterChain(
        ServerHttpSecurity http) {

    return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .logout(ServerHttpSecurity.LogoutSpec::disable)
            .authorizeExchange(exchange -> exchange
                    .pathMatchers("/api/auth/**").permitAll()
                    .anyExchange().permitAll()
            )
            .build();
}


}
