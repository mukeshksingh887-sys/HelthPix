package com.user_service.service;

import com.user_service.client.AuthFeignClient;
import com.user_service.dto.AuthUserRequest;
import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entity.User;
import com.user_service.entity.Enm.UserStatus;
import com.user_service.entity.Enm.UserType;
import com.user_service.exception.*;
import com.user_service.mapper.UserMapper;
import com.user_service.repository.UserRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImp implements  UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthFeignClient authFeignClient;



    @Override
//    @Transactional
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new UserAlreadyExistsException("Phone already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setBloodGroup(request.getBloodGroup());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setAddress(request.getAddress());

        User savedUser = userRepository.save(user);


        AuthUserRequest authRequest = AuthUserRequest.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .password(request.getPassword())
                .role(savedUser.getRole())
                .build();

        try {
            authFeignClient.createAuthUser(authRequest);
//            log.info(" sent to the auth service : {}", authRequest.getEmail());
        } catch (FeignException ex) {
            userRepository.deleteById(savedUser.getId());
//            throw new UserCreationException("Failed to create user in auth service");
            log.error("Auth Service Response : {}", ex.contentUTF8());

            throw new UserCreationException(
                    ex.contentUTF8());
        }

        return mapper.toResponse(savedUser);
    }



    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

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
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        try {

            authFeignClient.deleteAuthUser(id);

            userRepository.delete(user);

        } catch ( Exception ex) {
          log.error(" user deletion faild : {}", ex.getMessage());
            throw new UserDeleteException(
                    "Failed to delete user completely");
        }


    }


    @Override
    public UserResponse updateUser(Long id,
                                   UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

//       if(userRepository.findById(id) != null){
//           userRepository.delete(user);
//
//       }


        if (userRepository.existsByEmail(request.getEmail())
                && !user.getEmail().equals(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())
                && !user.getUsername().equals(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

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
    public UserResponse updateStatus(Long id,
                                     UserStatus status) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

//        user.setStatus(UserStatus.valueOf(status.toUpperCase()));
     log.info(" user status :   {}",user.getStatus());

        try {

//            UserStatus userStatus =
//                    UserStatus.valueOf(status.toUpperCase());
            user.setStatus(status);

//            User updatedUser = userRepository.save(user);

        } catch (IllegalArgumentException ex) {

            throw new BadRequestException(
                    "Invalid status. Allowed values: ACTIVE, INACTIVE, BLOCKED");
        }

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

         user.setStatus(UserStatus.ACTIVE);

//        return mapToResponse(userRepository.save(user));
        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public List<org.apache.catalina.User> findByStatus(UserStatus status) {
        return List.of();
    }

    @Override
    public UserResponse deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
         user.setStatus(UserStatus.INACTIVE);

        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse blockeUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setStatus(UserStatus.ACTIVE);

//        return mapToResponse(userRepository.save(user));
        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse  suspendeUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setStatus(UserStatus.ACTIVE);

//        return mapToResponse(userRepository.save(user));
        return mapper.toResponse(userRepository.save(user));
    }

    public List<UserResponse> getInactiveUsers() {

        return userRepository.findByStatus(UserStatus.INACTIVE)
                .stream()
                .map(mapper::toResponse)
                .toList();
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
