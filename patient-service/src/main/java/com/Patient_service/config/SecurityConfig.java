package com.Patient_service.config;


import com.Patient_service.security.GatewayHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final HeaderAuthenticationFilter headerAuthenticationFilter;
    @Autowired
    private  final GatewayHeaderFilter gatewayHeaderFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(
                gatewayHeaderFilter,
                org.springframework.security.web.authentication.
                        UsernamePasswordAuthenticationFilter.class
        );

        http.authorizeHttpRequests(auth -> auth

                // Public APIs
                .requestMatchers(
                        "/api/patients/public/**"
                ).permitAll()

                // Everything else authenticated
                .anyRequest().authenticated()
        );

        return http.build();
    }
}