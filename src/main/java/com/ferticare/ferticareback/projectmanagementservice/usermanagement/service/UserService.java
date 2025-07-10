package com.ferticare.ferticareback.projectmanagementservice.usermanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserRegisterRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserCreateByAdminRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.LoginRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.LoginResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.UserResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorScheduleDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user, RoleType role);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    // Hàm login
    Optional<User> login(String email, String rawPassword);
    
    // Authentication service methods
    LoginResponse authenticateUser(LoginRequest loginRequest);
    
    // User registration service methods
    UserResponse registerUser(UserRegisterRequest userRegisterRequest);
    
    // Admin user creation service methods
    UserResponse createUserByAdmin(UserCreateByAdminRequest adminRequest);
    
    // Doctor service methods
    List<DoctorScheduleDTO> getDoctorsWithSchedule();
    
    // Admin user management methods
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersByRole(RoleType roleType);
}