package com.ferticare.ferticareback.features.auth.controller;

import com.ferticare.ferticareback.common.security.annotation.AdminOnly;
import com.ferticare.ferticareback.features.role.entity.RoleType;
import com.ferticare.ferticareback.features.user.dto.UserCreateByAdminRequest;
import com.ferticare.ferticareback.features.user.dto.UserResponse;
import com.ferticare.ferticareback.features.user.entity.User;
import com.ferticare.ferticareback.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @AdminOnly
    @GetMapping("/admin")
    public ResponseEntity<?> onlyAdminAccess() {
        return ResponseEntity.ok("Welcome, Admin!");
    }

    @AdminOnly
    @PostMapping("/admin")
    public ResponseEntity<?> createUserByAdmin(@RequestBody UserCreateByAdminRequest request) {
        try {
            if (userService.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email đã tồn tại. Vui lòng chọn email khác.");
            }

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

            User savedUser = userService.save(user, request.getRole());

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
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
