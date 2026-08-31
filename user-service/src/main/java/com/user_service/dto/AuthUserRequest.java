package com.user_service.dto;

import com.user_service.entity.Enm.UserType;
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
