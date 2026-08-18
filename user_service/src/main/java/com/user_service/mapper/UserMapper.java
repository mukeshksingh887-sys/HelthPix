package com.user_service.mapper;


import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entity.User;
import com.user_service.entity.UserStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());

        // Default Values
        user.setStatus(UserStatus.ACTIVE);

        return user;
    };


//    public UserResponse toResponse(User user) {
//
//        return new UserResponse(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getPhone(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getGender(),
//                user.getDateOfBirth(),
//                user.getAddress(),
//                user.getCity(),
//                user.getState(),
//                user.getCountry(),
//                user.getPostalCode(),
//                user.getProfileImageUrl(),
//                user.getStatus(),
//                user.getUserType(),
//                user.getPatientId(),
//                user.getDoctorId(),
//                user.getStaffId(),
//                user.isEmailVerified(),
//                user.isPhoneVerified(),
//                user.getRoles()
//                        .stream()
//                        .map(role -> role.getName())
//                        .collect(Collectors.toSet()),
//                user.getCreatedAt(),
//                user.getUpdatedAt()
//        );
//    }



    public  UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .dob(user.getDob())
                .address(user.getAddress())
                .role(user.getRole())
                .status(user.getStatus())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }




    private UUID parseUUID(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return UUID.fromString(value);
    }
}
