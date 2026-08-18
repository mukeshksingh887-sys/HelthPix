package com.user_service.service;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {




    UserResponse createUser(UserRequest request);

    UserResponse getUser(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserRequest request);

    UserResponse patchUser(Long id, UserRequest request);

    void deleteUser(Long id);

    UserResponse updateStatus(Long id, String status);

    UserResponse getByEmail(String email);

    UserResponse getByPhone(String phone);

    List<UserResponse> getUsersByRole(String role);

    UserResponse changeRole(Long id, String role);

    UserResponse activateUser(Long id);

    UserResponse deactivateUser(Long id);


























//
//    Page<UserResponse> getUsers(Pageable pageable);
//
//    UserResponse updateUser(
//            UUID id,
//            UserRequest request
//    );
//
//    UserResponse patchUser(
//            UUID id,
//            UserRequest request
//    );
//
//    void deleteUser(UUID id);
//
//    UserResponse updateStatus(
//            UUID id,
//            UserRequest  request
//    );
//
//    Page<UserResponse> searchUsers(
//            String keyword,
//            Pageable pageable
//    );
//
//
//    UserResponse getByEmail(String email);
//
//    UserResponse getByPhone(String phone);
//
//    UserResponse changeRole(
//            UUID id,
//           UserRequest   request
//    );
//
//    Page<UserResponse> getUsersByRole(
//            UUID roleId,
//            Pageable pageable
//    );
//
//    UserResponse assignRole(
//            UUID id,
//            UUID roleId
//    );
//
//    UserResponse removeRole(
//            UUID id,
//            UUID roleId
//    );
//
//    UserResponse getProfile(UUID id);


//    UserResponse updateProfile(
//            UUID id,
//            UpdateProfileRequest request
//    );

//    void updatePassword(
//            UUID id,
//            UpdatePasswordRequest request
//    );

//    UserResponse updatePreferences(
//            UUID id,
//            UpdatePreferencesRequest request
//    );
}
