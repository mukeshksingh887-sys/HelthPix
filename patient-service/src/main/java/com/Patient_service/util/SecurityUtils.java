package com.Patient_service.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final HttpServletRequest request;

    public Long getUserId() {

        String userId =
                request.getHeader("X-USER-ID");

        return Long.valueOf(userId);
    }

    public String getRole() {

        return request.getHeader("X-ROLE");
    }

    public String getEmail() {

        return request.getHeader("X-EMAIL");
    }
}
