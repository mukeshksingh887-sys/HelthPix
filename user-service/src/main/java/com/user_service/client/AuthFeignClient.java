package com.user_service.client;

import com.user_service.dto.AuthUserRequest;
import com.user_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



// this data send to auth service using feign client api calling
//@FeignClient(
//        name = "auth-service",
//        url = "http://localhost:8087"
//)
@FeignClient(name = "AUTH-SERVICE",fallback = AuthFeignClientFallback.class)
public interface AuthFeignClient {

    @PostMapping("/internal/auth/users")
     void createAuthUser(
            @RequestBody AuthUserRequest request
    );

    @DeleteMapping("/internal/auth/users/{userId}")
    void deleteAuthUser(
            @PathVariable Long userId);



}