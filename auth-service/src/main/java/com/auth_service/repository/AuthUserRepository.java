package com.auth_service.repository;

import com.auth_service.entity.AuthUser;
import com.auth_service.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {


    Optional<AuthUser> findByEmail(String email);



    boolean existsByEmail(String email);

    Optional<AuthUser> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    Collection<AuthUser> findByRole(UserType role);
}