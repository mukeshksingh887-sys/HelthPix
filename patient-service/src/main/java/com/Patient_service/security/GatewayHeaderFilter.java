package com.Patient_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class GatewayHeaderFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("request header {}",request);
        String userId = request.getHeader("X-User-Id");
        log.info("in patient userId  :::: {}", userId);

        String email = request.getHeader("X-EMAIL");
        log.info(" in patient  email :::: {}", email);

        String role = request.getHeader("X-ROLE");
        log.info(" in patient role ::::{}", role);

        if (userId == null || role == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }


        UserPrincipal principal =
                new UserPrincipal(
                        Long.valueOf(userId),
                        email,
                        role);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role)));

        SecurityContextHolder
                .getContext()
                .setAuthentication(auth);

        filterChain.doFilter(
                request,
                response);
    }


}
