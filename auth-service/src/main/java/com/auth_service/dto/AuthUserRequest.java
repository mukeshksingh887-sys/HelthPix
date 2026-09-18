package com.auth_service.dto;

import com.auth_service.entity.UserType;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserRequest {

    private Long userId;

    private String email;

    private String password;

    private UserType role;
}