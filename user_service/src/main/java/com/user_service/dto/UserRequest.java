package com.user_service.dto;



import com.user_service.entity.UserType;
import lombok.Data;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private  String dob;
    private String phone;
    private String address;
    private UserType role;
}

