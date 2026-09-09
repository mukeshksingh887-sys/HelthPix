package com.user_service.dto;

import com.user_service.entity.Enm.Gender;
import com.user_service.entity.Enm.UserStatus;
import com.user_service.entity.Enm.UserType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private UserType role;
    private  String dob;
    private Gender gender;
    private String bloodGroup;
    private String address;
    private UserStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
