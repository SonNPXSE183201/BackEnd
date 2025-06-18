package com.ferticare.ferticareback.projectmanagementservice.usermanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user, RoleType role);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    // Hàm login
    Optional<User> login(String email, String rawPassword);
}