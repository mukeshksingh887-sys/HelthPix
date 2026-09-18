package com.user_service.controller;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entity.Enm.UserStatus;
import com.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController{

    @Autowired
    private final UserService userService;

    // Create User
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest request) {

//        return new ResponseEntity<>(
//                userService.createUser(request),
//                HttpStatus.CREATED,
//
//        );
       UserResponse user =  userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    // Get User By Id
    @GetMapping("getUser/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    // Get All Users
    @GetMapping("getAll")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // Update User
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        return ResponseEntity.ok(
                userService.updateUser(id, request)
        );
    }

    // Partial Update User
    @PatchMapping("/partialUpdate/{id}")
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        return ResponseEntity.ok(
                userService.patchUser(id, request)
        );
    }

    // Delete User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }

    // Update Status
    @PutMapping("/update/{id}/status/{status}")
    public ResponseEntity<UserResponse> updateStatus(
            @PathVariable Long id,
            @PathVariable UserStatus status) {

        return ResponseEntity.ok(
                userService.updateStatus(id, status)
        );
    }

    // Get User By Email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getByEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                userService.getByEmail(email)
        );
    }

    // Get User By Phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserResponse> getByPhone(
            @PathVariable String phone) {

        return ResponseEntity.ok(
                userService.getByPhone(phone)
        );
    }

    // Get Users By Role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(
            @PathVariable String role) {

        return ResponseEntity.ok(
                userService.getUsersByRole(role)
        );
    }

    // Change User Role
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @RequestParam String role) {

        return ResponseEntity.ok(
                userService.changeRole(id, role)
        );
    }

    // Activate User
    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResponse> changeToActivateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.activateUser(id)
        );
    }

    // Deactivate User
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> changeToDeactivateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.deactivateUser(id)
        );
    }
}