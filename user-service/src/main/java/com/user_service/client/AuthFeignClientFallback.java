package com.user_service.client;


import com.user_service.dto.AuthUserRequest;
import com.user_service.exception.AuthServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class AuthFeignClientFallback implements AuthFeignClient {

    @Override
    public void createAuthUser(AuthUserRequest request) {
        throw new AuthServiceUnavailableException(
                "AUTH-SERVICE is unavailable. User creation failed."
        );
    }

    @Override
    public void deleteAuthUser(Long userId) {
        throw new AuthServiceUnavailableException(
                "AUTH-SERVICE is unavailable. User deletion failed."
        );
    }

}