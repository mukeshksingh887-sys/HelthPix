package com.user_service.service;

import com.netflix.discovery.converters.Auto;
import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entity.User;
import com.user_service.entity.UserStatus;
import com.user_service.entity.UserType;
import com.user_service.mapper.UserMapper;
import com.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImp implements  UserService {

    @Autowired
    private UserRepository userRepository;
    private UserMapper mapper;

    @Override
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setAddress(request.getAddress());

        User savedUser = userRepository.save(user);

//        return mapToResponse(savedUser);
        return mapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

//        return mapToResponse(user);
        return mapper.toResponse(user);
    }


    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> mapper.toResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id,
                                   UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setRole(request.getRole());
        user.setAddress(request.getAddress());

        User updatedUser = userRepository.save(user);

         return mapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse patchUser(Long id,
                                  UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (request.getFirstName() != null)
            user.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            user.setLastName(request.getLastName());

        if (request.getEmail() != null)
            user.setEmail(request.getEmail());

        if (request.getPhone() != null)
            user.setPhone(request.getPhone());

        if (request.getAddress() != null)
            user.setAddress(request.getAddress());

        User updatedUser = userRepository.save(user);

//        return mapToResponse(updatedUser);
        return mapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserResponse updateStatus(Long id,
                                     String status) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setStatus(UserStatus.valueOf(status.toUpperCase()));

//        return mapToResponse(userRepository.save(user));
        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

//        return mapToResponse(user);

        return mapper.toResponse(user);
    }

    @Override
    public UserResponse getByPhone(String phone) {

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

//        return mapToResponse(user);
        return mapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getUsersByRole(String role) {

        return userRepository
                .findByRole(UserType.valueOf(role.toUpperCase()))
                .stream()
                .map(user -> mapper.toResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse changeRole(Long id,
                                   String role) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setRole(UserType.valueOf(role.toUpperCase()));

//        return mapToResponse(userRepository.save(user));

        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActive(true);

//        return mapToResponse(userRepository.save(user));
        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActive(false);

//        return mapToResponse(userRepository.save(user));
        return mapper.toResponse(userRepository.save(user));
    }


//    private UserResponse mapToResponse(User user) {
//
//        UserResponse response = new UserResponse();
//
//        response.setId(user.getId());
//        response.setFirstName(user.getFirstName());
//        response.setLastName(user.getLastName());
//        response.setUsername(user.getUsername());
//        response.setEmail(user.getEmail());
//        response.setPhone(user.getPhone());
//        response.setDob(user.getDob());
//        response.setRole(user.getRole());
//        response.setStatus(user.getStatus());
//        response.setActive(user.getActive());
//        response.setAddress(user.getAddress());
//        response.setCreatedAt(user.getCreatedAt());
//        response.setUpdatedAt(user.getUpdatedAt());
//
//        return response;
//    }
}
