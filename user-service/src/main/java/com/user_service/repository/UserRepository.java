package com.user_service.repository;

import com.user_service.entity.Enm.UserStatus;
import com.user_service.entity.User;
import com.user_service.entity.Enm.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    List<User> findByRole(UserType role);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    List<User>findByStatus(UserStatus userStatus);
}
