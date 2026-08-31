package com.auth_service.client;

import com.auth_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICE",  fallback = UserFeignClientFallback.class)
public interface UserFeignClient {

    @GetMapping("/internal/users/email/{email}")
    UserResponse getByEmail(@PathVariable  String email);


    @GetMapping("/internal/users/{id}")
    UserResponse getById(
            @PathVariable  Long id
    );
}