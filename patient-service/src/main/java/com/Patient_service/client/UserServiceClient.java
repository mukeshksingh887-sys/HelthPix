package com.Patient_service.client;


import com.Patient_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/internal/users/{id}")
    public UserResponse getUserById(@PathVariable Long id);

}
