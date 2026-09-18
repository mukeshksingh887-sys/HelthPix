package com.auth_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long userId;

    private String email;

    private String role;

    private String accessToken;

    private String refreshToken;
}