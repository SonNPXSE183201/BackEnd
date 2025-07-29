package com.ferticare.ferticareback.projectmanagementservice.usermanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserRegisterRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserCreateByAdminRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.LoginRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.LoginResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.UserResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.AdminDashboardDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.UserStatsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.ManagerDashboardDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.AdminDoctorDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.SystemSettingsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.DoctorStatsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorCreateRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    
    // Admin specific methods
    AdminDashboardDTO getAdminDashboardData();
    Page<UserResponse> getAllUsersForAdmin(Pageable pageable, String search, String role, String status);
    UserStatsDTO getUserStats();
    UserResponse updateUserByAdmin(UUID id, UserCreateByAdminRequest request);
    void deleteUserByAdmin(UUID id);
    UserResponse toggleUserStatus(UUID id);
    
    // Manager specific methods
    ManagerDashboardDTO getManagerDashboardData();
    
    // Admin Doctor Management methods
    Page<AdminDoctorDTO> getAllDoctorsForAdmin(Pageable pageable, String search, String department, String status);
    AdminDoctorDTO createDoctorByAdmin(DoctorCreateRequest request);
    AdminDoctorDTO updateDoctorByAdmin(UUID id, DoctorCreateRequest request);
    void deleteDoctorByAdmin(UUID id);
    AdminDoctorDTO toggleDoctorStatus(UUID id);
    AdminDoctorDTO getDoctorById(UUID id);
    DoctorStatsDTO getDoctorStats();
    Page<AdminDoctorDTO> getDoctorsByDepartment(UUID departmentId, Pageable pageable);
    
    // System Settings methods
    SystemSettingsDTO getSystemSettings();
    SystemSettingsDTO updateSystemSettings(SystemSettingsDTO settings);
    String performBackup();
    String performMaintenance();
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersByRole(RoleType roleType);
}