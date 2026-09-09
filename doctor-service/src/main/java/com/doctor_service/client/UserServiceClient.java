package com.doctor_service.client;

import com.doctor_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "USER-SERVICE",  fallback = UserFeignClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/internal/users/email/{email}")
  public   UserResponse getUserByEmail(@PathVariable String email);

   // /internal/users/
   @GetMapping("/internal/users/{id}")
   public UserResponse getUserById(
            @PathVariable Long id
    );
}

