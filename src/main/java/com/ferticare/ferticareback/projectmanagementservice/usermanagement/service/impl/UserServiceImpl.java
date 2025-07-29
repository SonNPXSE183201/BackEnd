package com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.impl;

import com.ferticare.ferticareback.common.response.ErrorResponse;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity.EmailVerificationToken;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository.EmailVerificationTokenRepository;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.Gender;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserRegisterRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserCreateByAdminRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.LoginRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.LoginResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.UserResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.AdminDashboardDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.UserStatsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.ManagerDashboardDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.AdminDoctorDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.SystemSettingsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.DoctorStatsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorCreateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DoctorWorkScheduleRepository;
import lombok.RequiredArgsConstructor;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DepartmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.DepartmentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;

    @Override
    public User save(User user, RoleType role) {
        // Kiểm tra trùng email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }

        // Kiểm tra trùng số điện thoại (nếu có)
        if (user.getPhone() != null && userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng.");
        }

        // Mặc định avatar nếu để trống
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
            user.setAvatarUrl("https://example.com/default-avatar.png"); // thay bằng URL mặc định của bạn
        }

        // Mã hóa mật khẩu (chỉ khi có password thật, không phải Google auth)
        if (!"GOOGLE_AUTH".equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Lưu user trước để lấy ID
        User savedUser = userRepository.save(user);

        // Cấp quyền
        int roleLevel = switch (role) {
            case ADMIN -> 4;
            case MANAGER -> 3;
            case DOCTOR -> 2;
            default -> 1;
        };

        // Tạo bản ghi Role
        Role newRole = new Role();
        newRole.setUser(savedUser);
        newRole.setRoleType(role.name());
        newRole.setRoleLevel(roleLevel);
        roleRepository.save(newRole);

        return savedUser;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public Optional<User> login(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        var userOpt = login(loginRequest.getEmail(), loginRequest.getPassword());

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        var user = userOpt.get();

        // Kiểm tra nếu chưa xác thực email
        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email của bạn chưa được xác thực. Vui lòng kiểm tra hộp thư.");
        }

        // Lấy role từ bảng riêng
        Role role = roleRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Sinh token với role từ bảng
        String token = jwtUtil.generateToken(user.getId(), role.getRoleType());

        return LoginResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(role.getRoleType())
                .token(token)
                .build();
    }

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        RoleType roleType = RoleType.CUSTOMER; // luôn là CUSTOMER khi user tự đăng ký

        User user = User.builder()
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .avatarUrl(request.getAvatarUrl())
                .password(request.getPassword())
                .build();

        User savedUser = save(user, roleType);

        // Tạo profile tương ứng để tránh lỗi khi /api/profiles/me
        Profile profile = Profile.builder()
                .user(savedUser)
                .status("active") // THÊM DÒNG NÀY để tránh lỗi NOT NULL
                .build();
        profileRepository.save(profile);

        // Tạo token xác nhận email
        String token = UUID.randomUUID().toString();
        EmailVerificationToken emailToken = EmailVerificationToken.builder()
                .token(token)
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        emailVerificationTokenRepository.save(emailToken);

        emailService.sendVerificationEmail(savedUser, token);

        return UserResponse.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .gender(savedUser.getGender())
                .dateOfBirth(savedUser.getDateOfBirth())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .address(savedUser.getAddress())
                .avatarUrl(savedUser.getAvatarUrl())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }

    @Override
    public UserResponse createUserByAdmin(UserCreateByAdminRequest request) {
        if (findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại. Vui lòng chọn email khác.");
        }

        // Tạo user
        User user = User.builder()
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .avatarUrl(request.getAvatarUrl())
                .password(request.getPassword())
                .build();

        User savedUser = save(user, request.getRole());

        // Tạo profile cho user (bắt buộc)
        Profile profile = Profile.builder()
                .user(savedUser)
                .status("ACTIVE")
                .build();
        profileRepository.save(profile);

        // Trả response
        return UserResponse.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .gender(savedUser.getGender())
                .dateOfBirth(savedUser.getDateOfBirth())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .address(savedUser.getAddress())
                .avatarUrl(savedUser.getAvatarUrl())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }

    @Override
    public List<DoctorScheduleDTO> getDoctorsWithSchedule() {
        // Lấy tất cả bác sĩ
        List<User> doctors = userRepository.findAllDoctors();
        
        return doctors.stream()
                .map(doctor -> {
                    // Lấy thông tin từ profile
                    Profile profile = profileRepository.findByUser_Id(doctor.getId()).orElse(null);
                    String specialty = profile != null ? profile.getSpecialty() : "Không xác định";
                    
                    // Lấy lịch làm việc từ bảng DoctorWorkSchedule mới
                    List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository.findByDoctorId(doctor.getId());
                    
                    List<DoctorScheduleDTO.WorkSchedule> workSchedules = schedules.stream()
                            .map(schedule -> new DoctorScheduleDTO.WorkSchedule(
                                    schedule.getDayOfWeek(),
                                    getDayName(schedule.getDayOfWeek()),
                                    schedule.getStartTime(),
                                    schedule.getEndTime(),
                                    schedule.getRoom()
                            ))
                            .toList();
                    
                    return new DoctorScheduleDTO(
                            doctor.getId(),
                            doctor.getFullName(),
                            doctor.getEmail(),
                            doctor.getPhone(),
                            specialty,
                            workSchedules
                    );
                })
                .toList();
    }
    
    private String getDayName(Integer dayOfWeek) {
        return switch (dayOfWeek) {
            case 2 -> "Thứ 2";
            case 3 -> "Thứ 3";
            case 4 -> "Thứ 4";
            case 5 -> "Thứ 5";
            case 6 -> "Thứ 6";
            case 7 -> "Thứ 7";
            case 8 -> "Chủ nhật";
            default -> "Không xác định";
        };
    }

    // New method for Google users that doesn't check for existing email
    public User saveGoogleUser(User user, RoleType role) {
        // Mặc định avatar nếu để trống
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
            user.setAvatarUrl("https://example.com/default-avatar.png");
        }

        // Google users have placeholder password
        user.setPassword("GOOGLE_AUTH");

        // Lưu user trước để lấy ID
        User savedUser = userRepository.save(user);

        // Cấp quyền
        int roleLevel = switch (role) {
            case ADMIN -> 4;
            case MANAGER -> 3;
            case DOCTOR -> 2;
            default -> 1;
        };

        // Tạo bản ghi Role
        Role newRole = new Role();
        newRole.setUser(savedUser);
        newRole.setRoleType(role.name());
        newRole.setRoleLevel(roleLevel);
        roleRepository.save(newRole);

        return savedUser;
    }

    // Admin specific methods implementation
    @Override
    public AdminDashboardDTO getAdminDashboardData() {
        int totalUsers = (int) userRepository.count();
        int totalDoctors = (int) userRepository.countByRoleType("DOCTOR");
        int totalPatients = (int) userRepository.countByRoleType("CUSTOMER");
        int totalDepartments = 2; // IVF and IUI only

        // Mock data for charts
        List<AdminDashboardDTO.UserGrowthData> growthData = Arrays.asList(
            AdminDashboardDTO.UserGrowthData.builder().month("Jan").users(25).build(),
            AdminDashboardDTO.UserGrowthData.builder().month("Feb").users(35).build(),
            AdminDashboardDTO.UserGrowthData.builder().month("Mar").users(45).build(),
            AdminDashboardDTO.UserGrowthData.builder().month("Apr").users(55).build(),
            AdminDashboardDTO.UserGrowthData.builder().month("May").users(65).build(),
            AdminDashboardDTO.UserGrowthData.builder().month("Jun").users(75).build()
        );

        List<AdminDashboardDTO.DepartmentData> departmentData = Arrays.asList(
            AdminDashboardDTO.DepartmentData.builder().department("IVF").patients(156).color("#3b82f6").build(),
            AdminDashboardDTO.DepartmentData.builder().department("IUI").patients(125).color("#ef4444").build()
        );

        return AdminDashboardDTO.builder()
                .totalUsers(totalUsers)
                .totalDoctors(totalDoctors)
                .totalPatients(totalPatients)
                .totalDepartments(totalDepartments)
                .monthlyGrowth(12.5)
                .activeUsers(totalUsers - 2)
                .pendingApprovals(3)
                .systemHealth(98.5)
                .userGrowthData(growthData)
                .departmentData(departmentData)
                .build();
    }

    @Override
    public Page<UserResponse> getAllUsersForAdmin(Pageable pageable, String search, String role, String status) {
        List<User> allUsers = userRepository.findAll();
        
        // Apply filters
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> {
                    boolean matchesSearch = search == null || search.isBlank() ||
                            user.getFullName().toLowerCase().contains(search.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(search.toLowerCase());
                    
                    boolean matchesRole = role == null || role.isBlank() ||
                            getUserRole(user).equalsIgnoreCase(role);
                    
                    boolean matchesStatus = status == null || status.isBlank() ||
                            (status.equalsIgnoreCase("active") && user.isEmailVerified()) ||
                            (status.equalsIgnoreCase("inactive") && !user.isEmailVerified());
                    
                    return matchesSearch && matchesRole && matchesStatus;
                })
                .collect(Collectors.toList());

        // Convert to UserResponse
        List<UserResponse> userResponses = filteredUsers.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userResponses.size());
        List<UserResponse> paginatedList = userResponses.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, userResponses.size());
    }

    @Override
    public UserStatsDTO getUserStats() {
        long total = userRepository.count();
        long admin = userRepository.countByRoleType("ADMIN");
        long manager = userRepository.countByRoleType("MANAGER");
        long doctor = userRepository.countByRoleType("DOCTOR");
        long patient = userRepository.countByRoleType("CUSTOMER");
        long customer = patient; // CUSTOMER is the same as patient
        long active = userRepository.countByIsEmailVerified(true);
        long inactive = total - active;

        return UserStatsDTO.builder()
                .total(total)
                .admin(admin)
                .manager(manager)
                .doctor(doctor)
                .patient(patient)
                .customer(customer)
                .active(active)
                .inactive(inactive)
                .build();
    }

    @Override
    public UserResponse updateUserByAdmin(UUID id, UserCreateByAdminRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        // Check if email is taken by another user
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email đã được sử dụng bởi user khác");
        }

        // Update user fields
        user.setFullName(request.getFullName());
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setAvatarUrl(request.getAvatarUrl());

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User savedUser = userRepository.save(user);

        // Update role if different
        Role currentRole = roleRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        if (!currentRole.getRoleType().equals(request.getRole().name())) {
            currentRole.setRoleType(request.getRole().name());
            currentRole.setRoleLevel(switch (request.getRole()) {
                case ADMIN -> 4;
                case MANAGER -> 3;
                case DOCTOR -> 2;
                default -> 1;
            });
            roleRepository.save(currentRole);
        }

        return convertToUserResponse(savedUser);
    }

    @Override
    public void deleteUserByAdmin(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
        
        // Delete related entities first
        roleRepository.deleteByUser(user);
        profileRepository.deleteByUser(user);
        
        userRepository.delete(user);
    }

    @Override
    public UserResponse toggleUserStatus(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
        
        user.setEmailVerified(!user.isEmailVerified());
        User savedUser = userRepository.save(user);
        
        return convertToUserResponse(savedUser);
    }

    // Manager specific methods implementation
    @Override
    public ManagerDashboardDTO getManagerDashboardData() {
        int totalDoctors = (int) userRepository.countByRoleType("DOCTOR");
        int activeDoctors = totalDoctors - 2; // Mock data
        
        // Mock performance data
        List<ManagerDashboardDTO.PerformanceData> performanceData = Arrays.asList(
            ManagerDashboardDTO.PerformanceData.builder()
                .month("Jan").patients(45).appointments(52).success(48).build(),
            ManagerDashboardDTO.PerformanceData.builder()
                .month("Feb").patients(52).appointments(58).success(55).build(),
            ManagerDashboardDTO.PerformanceData.builder()
                .month("Mar").patients(61).appointments(67).success(63).build()
        );

        List<ManagerDashboardDTO.DepartmentData> departmentData = Arrays.asList(
            ManagerDashboardDTO.DepartmentData.builder().name("IVF").value(60).color("#3b82f6").build(),
            ManagerDashboardDTO.DepartmentData.builder().name("IUI").value(40).color("#ef4444").build()
        );

        List<ManagerDashboardDTO.DoctorInfo> topDoctors = getDoctorsWithSchedule().stream()
                .limit(5)
                .map(doctor -> ManagerDashboardDTO.DoctorInfo.builder()
                    .id(doctor.getId().toString())
                    .name(doctor.getName())
                    .specialty(doctor.getSpecialty())
                    .patientCount(15 + (int)(Math.random() * 20))
                    .todayAppointments(3 + (int)(Math.random() * 5))
                    .performance(85.0 + (Math.random() * 15))
                    .status("active")
                    .build())
                .collect(Collectors.toList());

        return ManagerDashboardDTO.builder()
                .totalDoctors(totalDoctors)
                .activeDoctors(activeDoctors)
                .totalAppointments(85)
                .todayAppointments(15)
                .averageRating(4.7)
                .patientSatisfaction(92)
                .performanceData(performanceData)
                .departmentData(departmentData)
                .topDoctors(topDoctors)
                .build();
    }

    // Helper methods
    private String getUserRole(User user) {
        return roleRepository.findByUser(user)
                .map(Role::getRoleType)
                .orElse("CUSTOMER");
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::convertToUserResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getUsersByRole(RoleType roleType) {
        return userRepository.findAll().stream()
            .filter(user -> roleRepository.findByUser(user)
                .map(role -> role.getRoleType().equals(roleType.name())).orElse(false))
            .map(this::convertToUserResponse)
            .collect(Collectors.toList());
    }

    // ===================== ADMIN DOCTOR MANAGEMENT METHODS =====================

    @Override
    public Page<AdminDoctorDTO> getAllDoctorsForAdmin(Pageable pageable, String search, String department, String status) {
        try {
            List<User> allDoctors = userRepository.findAllDoctors();
            
            // Apply filters with null safety
            List<User> filteredDoctors = allDoctors.stream()
                    .filter(doctor -> {
                        try {
                            // Search filter
                            boolean matchesSearch = search == null || search.trim().isEmpty() ||
                                    (doctor.getFullName() != null && doctor.getFullName().toLowerCase().contains(search.toLowerCase())) ||
                                    (doctor.getEmail() != null && doctor.getEmail().toLowerCase().contains(search.toLowerCase()));
                            
                            // Get profile for department and status check
                            Profile profile = profileRepository.findByUser(doctor).orElse(null);
                            
                            // Department filter
                            boolean matchesDepartment = department == null || department.trim().isEmpty() ||
                                    (profile != null && profile.getDepartment() != null && 
                                     profile.getDepartment().getName() != null &&
                                     profile.getDepartment().getName().toLowerCase().contains(department.toLowerCase()));
                            
                            // Status filter
                            boolean matchesStatus = status == null || status.trim().isEmpty() ||
                                    (profile != null && profile.getStatus() != null && 
                                     status.equalsIgnoreCase(profile.getStatus()));
                            
                            return matchesSearch && matchesDepartment && matchesStatus;
                        } catch (Exception e) {
                            // Log the error and exclude this doctor from results
                            System.err.println("Error filtering doctor " + doctor.getId() + ": " + e.getMessage());
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            // Convert to AdminDoctorDTO with error handling
            List<AdminDoctorDTO> doctorDTOs = filteredDoctors.stream()
                    .map(doctor -> {
                        try {
                            return convertToAdminDoctorDTO(doctor);
                        } catch (Exception e) {
                            System.err.println("Error converting doctor " + doctor.getId() + " to DTO: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            // Manual pagination
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), doctorDTOs.size());
            
            // Handle empty list or invalid pagination
            if (start >= doctorDTOs.size()) {
                return new PageImpl<>(new ArrayList<>(), pageable, doctorDTOs.size());
            }
            
            List<AdminDoctorDTO> paginatedList = doctorDTOs.subList(start, end);
            return new PageImpl<>(paginatedList, pageable, doctorDTOs.size());
            
        } catch (Exception e) {
            System.err.println("Error in getAllDoctorsForAdmin: " + e.getMessage());
            e.printStackTrace();
            // Return empty page on error
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public AdminDoctorDTO createDoctorByAdmin(DoctorCreateRequest request) {
        // Create user
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(Gender.valueOf(request.getGender()))
                .address(request.getAddress())
                .avatarUrl(request.getAvatar())
                .password("password123") // Default password
                .isEmailVerified(true) // Admin created users are auto-verified
                .build();

        User savedUser = save(user, RoleType.DOCTOR);

        // Tìm department theo ID nếu có
        Department department = null;
        if (request.getDepartmentId() != null && !request.getDepartmentId().isEmpty()) {
            department = departmentRepository.findById(Long.parseLong(request.getDepartmentId())).orElse(null);
        }

        // Determine specialty - use specialty field if available, otherwise use specialization
        String specialty = request.getSpecialty() != null && !request.getSpecialty().trim().isEmpty() 
                         ? request.getSpecialty() 
                         : request.getSpecialization();

        // Create profile
        Profile profile = Profile.builder()
                .user(savedUser)
                .status("active")
                .specialty(specialty)
                .department(department) // Sử dụng Department object
                .departmentName(request.getDepartment()) // Lưu tên phòng ban để tương thích ngược
                .workSchedule(convertScheduleToString(request.getSchedule()))
                .experienceYears(request.getExperience())
                .contractType(request.getContractType())
                .build();
        profileRepository.save(profile);

        // Cập nhật số lượng bác sĩ trong department nếu có
        if (department != null) {
            departmentService.updateDoctorCount(department.getId(), 1);
        }

        return convertToAdminDoctorDTO(savedUser);
    }

    @Override
    public AdminDoctorDTO updateDoctorByAdmin(UUID id, DoctorCreateRequest request) {
        User doctor = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bác sĩ không tồn tại"));

        // Verify this is a doctor
        Role role = roleRepository.findByUser(doctor)
                .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại"));
        if (!"DOCTOR".equals(role.getRoleType())) {
            throw new IllegalArgumentException("User này không phải là bác sĩ");
        }

        // Update user fields
        doctor.setFullName(request.getFullName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setDateOfBirth(request.getDateOfBirth());
        doctor.setGender(Gender.valueOf(request.getGender()));
        doctor.setAddress(request.getAddress());
        doctor.setAvatarUrl(request.getAvatar());
        userRepository.save(doctor);

        // Update profile
        Profile profile = profileRepository.findByUser(doctor)
                .orElseThrow(() -> new IllegalArgumentException("Profile không tồn tại"));
        
        // Lưu department cũ để cập nhật số lượng
        Department oldDepartment = profile.getDepartment();
        
        // Tìm department mới nếu có
        Department newDepartment = null;
        if (request.getDepartmentId() != null && !request.getDepartmentId().isEmpty()) {
            newDepartment = departmentRepository.findById(Long.parseLong(request.getDepartmentId())).orElse(null);
        }
        
        // Determine specialty - use specialty field if available, otherwise use specialization
        String specialty = request.getSpecialty() != null && !request.getSpecialty().trim().isEmpty() 
                         ? request.getSpecialty() 
                         : request.getSpecialization();

        profile.setSpecialty(specialty);
        profile.setDepartment(newDepartment); // Cập nhật department mới
        profile.setDepartmentName(request.getDepartment()); // Vẫn lưu tên department
        profile.setWorkSchedule(convertScheduleToString(request.getSchedule()));
        profile.setExperienceYears(request.getExperience());
        profile.setContractType(request.getContractType());
        profileRepository.save(profile);
        
        // Cập nhật số lượng bác sĩ trong các department
        if (oldDepartment != null && (newDepartment == null || !oldDepartment.getId().equals(newDepartment.getId()))) {
            // Giảm số lượng ở department cũ
            departmentService.updateDoctorCount(oldDepartment.getId(), -1);
        }
        
        if (newDepartment != null && (oldDepartment == null || !newDepartment.getId().equals(oldDepartment.getId()))) {
            // Tăng số lượng ở department mới
            departmentService.updateDoctorCount(newDepartment.getId(), 1);
        }

        return convertToAdminDoctorDTO(doctor);
    }

    @Override
    public void deleteDoctorByAdmin(UUID id) {
        User doctor = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bác sĩ không tồn tại"));

        // Verify this is a doctor
        Role role = roleRepository.findByUser(doctor)
                .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại"));
        if (!"DOCTOR".equals(role.getRoleType())) {
            throw new IllegalArgumentException("User này không phải là bác sĩ");
        }

        // Delete related entities
        profileRepository.deleteByUser(doctor);
        roleRepository.deleteByUser(doctor);
        userRepository.delete(doctor);
    }

    @Override
    public AdminDoctorDTO toggleDoctorStatus(UUID id) {
        User doctor = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bác sĩ không tồn tại"));

        Profile profile = profileRepository.findByUser(doctor)
                .orElseThrow(() -> new IllegalArgumentException("Profile không tồn tại"));

        // Toggle status
        String newStatus = "active".equals(profile.getStatus()) ? "inactive" : "active";
        profile.setStatus(newStatus);
        profileRepository.save(profile);

        return convertToAdminDoctorDTO(doctor);
    }

    @Override
    public AdminDoctorDTO getDoctorById(UUID id) {
        User doctor = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bác sĩ không tồn tại"));
        return convertToAdminDoctorDTO(doctor);
    }

    @Override
    public DoctorStatsDTO getDoctorStats() {
        List<User> allDoctors = userRepository.findAllDoctors();
        
        long total = allDoctors.size();
        long active = allDoctors.stream()
                .mapToLong(doctor -> {
                    Profile profile = profileRepository.findByUser(doctor).orElse(null);
                    return (profile != null && "active".equals(profile.getStatus())) ? 1 : 0;
                })
                .sum();
        long inactive = total - active;
        
        // Calculate average rating from mock data
        double avgRating = allDoctors.isEmpty() ? 0.0 : 
                allDoctors.stream()
                    .mapToDouble(doctor -> 4.0 + (Math.random() * 1.0)) // Mock rating 4.0-5.0
                    .average()
                    .orElse(0.0);
        
        return DoctorStatsDTO.builder()
                .total(total)
                .active(active)
                .inactive(inactive)
                .avgRating(Math.round(avgRating * 10.0) / 10.0) // Round to 1 decimal
                .build();
    }

    // ===================== SYSTEM SETTINGS METHODS =====================

    @Override
    public SystemSettingsDTO getSystemSettings() {
        // Mock system settings data
        return SystemSettingsDTO.builder()
                .general(SystemSettingsDTO.GeneralSettings.builder()
                        .systemName("FertiCare Management System")
                        .systemVersion("2.1.0")
                        .timezone("Asia/Ho_Chi_Minh")
                        .language("vi")
                        .currency("VND")
                        .maintenanceMode(false)
                        .supportEmail("support@ferticare.com")
                        .supportPhone("1900-1234")
                        .build())
                
                .security(SystemSettingsDTO.SecuritySettings.builder()
                        .passwordMinLength(8)
                        .requireSpecialChar(true)
                        .requireNumber(true)
                        .requireUppercase(true)
                        .sessionTimeout(1440) // 24 hours
                        .twoFactorAuth(false)
                        .maxLoginAttempts(5)
                        .lockoutDuration(15) // minutes
                        .build())
                
                .email(SystemSettingsDTO.EmailSettings.builder()
                        .smtpHost("smtp.gmail.com")
                        .smtpPort(587)
                        .smtpUsername("noreply@ferticare.com")
                        .smtpSsl(true)
                        .fromEmail("noreply@ferticare.com")
                        .fromName("FertiCare System")
                        .emailNotifications(true)
                        .appointmentReminders(true)
                        .build())
                
                .backup(SystemSettingsDTO.BackupSettings.builder()
                        .autoBackup(true)
                        .backupFrequency("daily")
                        .retentionDays(30)
                        .backupLocation("/backup/ferticare")
                        .lastBackup(LocalDateTime.now().minusHours(6))
                        .backupStatus("completed")
                        .backupSize(2048000L) // 2MB
                        .build())
                
                .maintenance(SystemSettingsDTO.MaintenanceSettings.builder()
                        .scheduledMaintenance(false)
                        .nextMaintenance(LocalDateTime.now().plusDays(7))
                        .maintenanceWindow("02:00-04:00")
                        .maintenanceMessage("Hệ thống đang bảo trì, vui lòng thử lại sau.")
                        .notifyUsers(true)
                        .build())
                
                .health(SystemSettingsDTO.SystemHealth.builder()
                        .status("healthy")
                        .cpuUsage(35.2)
                        .memoryUsage(68.7)
                        .diskUsage(45.3)
                        .totalUsers(userRepository.count())
                        .activeUsers(userRepository.countByIsEmailVerified(true))
                        .totalDoctors(userRepository.countByRoleType("DOCTOR"))
                        .totalAppointments(256L)
                        .uptime("15 days, 8 hours")
                        .lastRestart(LocalDateTime.now().minusDays(15))
                        .build())
                .build();
    }

    @Override
    public SystemSettingsDTO updateSystemSettings(SystemSettingsDTO settings) {
        // In a real implementation, you would save these settings to database
        // For now, just return the updated settings
        return settings;
    }

    @Override
    public String performBackup() {
        // Mock backup operation
        try {
            Thread.sleep(2000); // Simulate backup time
            return "Backup completed successfully at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Backup failed: " + e.getMessage();
        }
    }

    @Override
    public String performMaintenance() {
        // Mock maintenance operation
        try {
            Thread.sleep(3000); // Simulate maintenance time
            return "System maintenance completed successfully at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Maintenance failed: " + e.getMessage();
        }
    }

    // ===================== HELPER METHODS =====================

    private AdminDoctorDTO convertToAdminDoctorDTO(User doctor) {
        try {
            Profile profile = profileRepository.findByUser(doctor).orElse(null);
            
            // Create department info with null safety
            AdminDoctorDTO.DepartmentInfo departmentInfo = null;
            if (profile != null && profile.getDepartment() != null) {
                Department dept = profile.getDepartment();
                departmentInfo = AdminDoctorDTO.DepartmentInfo.builder()
                        .id(dept.getId() != null ? String.valueOf(dept.getId()) : "")
                        .name(dept.getName() != null ? dept.getName() : "")
                        .location(dept.getLocation() != null ? dept.getLocation() : "")
                        .headDoctor(dept.getHeadDoctor() != null ? dept.getHeadDoctor() : "")
                        .isActive(dept.getIsActive() != null ? dept.getIsActive() : true)
                        .build();
            }
            
            // Mock schedule data
            AdminDoctorDTO.DoctorScheduleInfo schedule = AdminDoctorDTO.DoctorScheduleInfo.builder()
                    .monday(AdminDoctorDTO.DoctorScheduleInfo.WeeklySchedule.builder().morning(true).afternoon(true).evening(false).build())
                    .tuesday(AdminDoctorDTO.DoctorScheduleInfo.WeeklySchedule.builder().morning(true).afternoon(false).evening(true).build())
                    .wednesday(AdminDoctorDTO.DoctorScheduleInfo.WeeklySchedule.builder().morning(false).afternoon(true).evening(true).build())
                    .thursday(AdminDoctorDTO.DoctorScheduleInfo.WeeklySchedule.builder().morning(true).afternoon(true).evening(false).build())
                    .friday(AdminDoctorDTO.DoctorScheduleInfo.WeeklySchedule.builder().morning(true).afternoon(true).evening(true).build())
                    .saturday(AdminDoctorDTO.DoctorScheduleInfo.WeeklySchedule.builder().morning(true).afternoon(false).evening(false).build())
                    .sunday(AdminDoctorDTO.DoctorScheduleInfo.WeeklySchedule.builder().morning(false).afternoon(false).evening(false).build())
                    .build();
            
            // Mock performance data
            AdminDoctorDTO.DoctorPerformance performance = AdminDoctorDTO.DoctorPerformance.builder()
                    .totalPatients(150 + (int)(Math.random() * 100))
                    .completedTreatments(120 + (int)(Math.random() * 80))
                    .successRate(80 + (int)(Math.random() * 15))
                    .avgRating(4.0 + (Math.random() * 1.0))
                    .patientSatisfaction(85 + (int)(Math.random() * 15))
                    .monthlyAppointments(25 + (int)(Math.random() * 15))
                    .build();

            return AdminDoctorDTO.builder()
                    .id(doctor.getId())
                    .fullName(doctor.getFullName() != null ? doctor.getFullName() : "Unknown")
                    .email(doctor.getEmail() != null ? doctor.getEmail() : "")
                    .phone(doctor.getPhone() != null ? doctor.getPhone() : "")
                    .avatar(doctor.getAvatarUrl() != null ? doctor.getAvatarUrl() : "")
                    .specialization(profile != null && profile.getSpecialty() != null ? profile.getSpecialty() : "Chưa phân công")
                    .department(profile != null && profile.getDepartment() != null && profile.getDepartment().getName() != null 
                               ? profile.getDepartment().getName() : "Chưa phân công")
                    .departmentInfo(departmentInfo)
                    .experience(profile != null && profile.getExperienceYears() != null ? profile.getExperienceYears() : 0)
                    .rating(4.0 + (Math.random() * 1.0))
                    .status(profile != null && profile.getStatus() != null ? profile.getStatus() : "inactive")
                    .salary(25000000.0 + (Math.random() * 25000000.0))
                    .contractType(profile != null && profile.getContractType() != null ? profile.getContractType() : "full-time")
                    .joinDate(doctor.getCreatedAt())
                    .createdAt(doctor.getCreatedAt())
                    .updatedAt(doctor.getUpdatedAt())
                    .schedule(schedule)
                    .performance(performance)
                    .build();
                    
        } catch (Exception e) {
            System.err.println("Error in convertToAdminDoctorDTO for doctor " + doctor.getId() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to convert doctor to DTO", e);
        }
    }

    private String convertScheduleToString(DoctorCreateRequest.DoctorScheduleRequest schedule) {
        if (schedule == null) return "Chưa thiết lập";
        
        // Convert schedule object to string representation
        StringBuilder sb = new StringBuilder();
        sb.append("Mon: ").append(getScheduleString(schedule.getMonday())).append("; ");
        sb.append("Tue: ").append(getScheduleString(schedule.getTuesday())).append("; ");
        sb.append("Wed: ").append(getScheduleString(schedule.getWednesday())).append("; ");
        sb.append("Thu: ").append(getScheduleString(schedule.getThursday())).append("; ");
        sb.append("Fri: ").append(getScheduleString(schedule.getFriday())).append("; ");
        sb.append("Sat: ").append(getScheduleString(schedule.getSaturday())).append("; ");
        sb.append("Sun: ").append(getScheduleString(schedule.getSunday()));
        
        return sb.toString();
    }
    
    private String getScheduleString(DoctorCreateRequest.DoctorScheduleRequest.WeeklyScheduleRequest schedule) {
        if (schedule == null) return "Off";
        
        StringBuilder sb = new StringBuilder();
        if (schedule.isMorning()) sb.append("Morning ");
        if (schedule.isAfternoon()) sb.append("Afternoon ");
        if (schedule.isEvening()) sb.append("Evening");
        
        return sb.toString().trim().isEmpty() ? "Off" : sb.toString().trim();
    }

    @Override
    public Page<AdminDoctorDTO> getDoctorsByDepartment(UUID departmentId, Pageable pageable) {
        // Convert UUID to String và kiểm tra department có tồn tại không
        String departmentIdStr = departmentId.toString();
        Department department = departmentRepository.findById(Long.parseLong(departmentIdStr))
                .orElseThrow(() -> new IllegalArgumentException("Department với ID " + departmentIdStr + " không tồn tại"));
        
        // Tạo pageable không có sort để tránh lỗi 'string' field
        Pageable unsortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        
        // Tìm tất cả profile có department_id = departmentId
        Page<Profile> doctorProfiles = profileRepository.findByDepartment(department, unsortedPageable);
        
        // Chuyển đổi sang DTO và filter null values
        List<AdminDoctorDTO> doctorDTOs = doctorProfiles.getContent().stream()
            .filter(profile -> profile != null && profile.getUser() != null)
            .map(profile -> {
                User doctor = profile.getUser();
                
                // Xác minh đây có phải là bác sĩ không
                Role role = roleRepository.findByUser(doctor).orElse(null);
                if (role == null || !"DOCTOR".equals(role.getRoleType())) {
                    return null;
                }
                
                return convertToAdminDoctorDTO(doctor);
            })
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
        
        return new PageImpl<>(doctorDTOs, pageable, doctorProfiles.getTotalElements());
    }
}