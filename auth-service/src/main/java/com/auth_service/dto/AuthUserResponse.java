package com.auth_service.dto;



import com.auth_service.entity.UserType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserResponse {

    private Long id;
    private Long userId;
    private String email;
    private UserType role;
}