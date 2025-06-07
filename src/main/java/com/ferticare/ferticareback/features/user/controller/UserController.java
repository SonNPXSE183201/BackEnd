package com.ferticare.ferticareback.features.user.controller;

import com.ferticare.ferticareback.features.role.entity.RoleType;
import com.ferticare.ferticareback.features.user.dto.UserRegisterRequest;
import com.ferticare.ferticareback.features.user.dto.UserResponse;
import com.ferticare.ferticareback.features.user.entity.User;
import com.ferticare.ferticareback.features.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegisterRequest request) {
        try {
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

            User savedUser = userService.save(user, RoleType.CUSTOMER);

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
            return ResponseEntity.internalServerError().body(Map.of("message", "Đăng ký thất bại."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        Optional<User> userOpt = userService.findById(id);

        return userOpt.map(user -> ResponseEntity.ok(UserResponse.builder()
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
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }
}