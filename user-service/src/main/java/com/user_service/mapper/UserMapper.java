package com.user_service.mapper;


import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entity.User;
import com.user_service.entity.Enm.UserStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE); // Default Values
        user.setAddress(request.getAddress());

        return user;
    }



    public  UserResponse toResponse(User user) {


        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setDob(user.getDob());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setAddress(user.getAddress());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }




//    private UUID parseUUID(String value) {
//
//        if (value == null || value.isBlank()) {
//            return null;
//        }
//
//        return UUID.fromString(value);
//    }
}
