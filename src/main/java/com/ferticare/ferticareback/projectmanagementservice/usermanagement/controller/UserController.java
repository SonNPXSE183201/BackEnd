package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.Gender;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserRegisterRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.UserResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ProfileRepository profileRepository;

    public UserController(UserService userService, ProfileRepository profileRepository) {
        this.userService = userService;
        this.profileRepository = profileRepository;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterRequest request) {
        try {
            RoleType roleType = RoleType.CUSTOMER; // ✅ luôn là CUSTOMER khi user tự đăng ký

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

            User savedUser = userService.save(user, roleType);

            // ✅ tạo profile tương ứng để tránh lỗi khi /api/profiles/me
            Profile profile = Profile.builder()
                    .user(savedUser)
                    .status("active") // ✅ THÊM DÒNG NÀY để tránh lỗi NOT NULL
                    .build();
            profileRepository.save(profile);

            UserResponse response = UserResponse.builder()
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

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            // Log lỗi chi tiết để debug
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Đăng ký thất bại: " + ex.getMessage(),
                "error", ex.getClass().getSimpleName()
            ));
        }
    }
}