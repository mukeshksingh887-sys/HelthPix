package com.user_service.entity;


import com.user_service.entity.Enm.Gender;
import com.user_service.entity.Enm.UserStatus;
import com.user_service.entity.Enm.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private  String dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

   private String bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType role;


    @Enumerated(EnumType.STRING)
    private UserStatus status;


    private  String address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

