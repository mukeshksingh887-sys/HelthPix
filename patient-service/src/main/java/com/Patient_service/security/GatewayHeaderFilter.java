//package com.Patient_service.security;
//
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Slf4j
//@Component
//public class GatewayHeaderFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        // Skip internal endpoints
//        if (path.startsWith("/internal")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String userId = request.getHeader("X-USER-ID");
//        String role = request.getHeader("X-ROLE");
//
//
//        log.info(" user id : {}", userId);
//
//        log.info(" user Role : {}", role);
//
//        if (userId == null || role == null) {
//
//            log.error("Missing Gateway Security Headers");
//
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//
//            response.getWriter()
//                    .write("Unauthorized Access");
//
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//
//}