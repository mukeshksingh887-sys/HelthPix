package com.auth_service.client;

import com.auth_service.dto.UserResponse;
import com.auth_service.exception.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientFallback implements UserFeignClient {

//    @Override
//    public UserResponse getByEmail(String email) {
//        throw new ServiceUnavailableException("USER-SERVICE is unavailable");
//    }
//
//    @Override
//    public UserResponse getById(Long id) {
//        throw new ServiceUnavailableException("USER-SERVICE is unavailable");
//    }

    @Override
    public UserResponse getUserByEmail(String email) {
        throw new ServiceUnavailableException("USER-SERVICE is unavailable");
    }

    @Override
    public UserResponse getUserById(Long id) {
        throw new ServiceUnavailableException("USER-SERVICE is unavailable");
    }
}
