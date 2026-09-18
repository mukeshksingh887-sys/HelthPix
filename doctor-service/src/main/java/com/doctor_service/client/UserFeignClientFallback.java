package com.doctor_service.client;



import com.doctor_service.dto.UserResponse;
import com.doctor_service.exception.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientFallback implements UserServiceClient {

    @Override
    public UserResponse getUserByEmail(String email) {
        throw new ServiceUnavailableException("USER-SERVICE is unavailable");
    }

    @Override
    public UserResponse getUserById(Long id) {
        throw  new ServiceUnavailableException("USER-SERVICE is unavailable");
    }

    @Override
    public void deleteUser(Long id) {
        throw  new ServiceUnavailableException("USER-SERVICE is unavailable");
    }


}
