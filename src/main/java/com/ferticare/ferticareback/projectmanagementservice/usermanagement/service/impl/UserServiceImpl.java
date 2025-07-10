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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DoctorWorkScheduleRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
            case 1 -> "Thứ 2";
            case 2 -> "Thứ 3";
            case 3 -> "Thứ 4";
            case 4 -> "Thứ 5";
            case 5 -> "Thứ 6";
            case 6 -> "Thứ 7";
            case 7 -> "Chủ nhật";
            default -> "Không xác định";
        };
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getUsersByRole(RoleType roleType) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> {
                    Optional<Role> role = roleRepository.findByUser(user);
                    return role.isPresent() && role.get().getRoleType().equals(roleType.name());
                })
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
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
}