package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller.customer;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.AdminOnly;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserRegisterRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.UserResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Đăng ký tài khoản mới", description = "Tạo tài khoản người dùng mới")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
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

    @GetMapping
    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "Admin lấy danh sách tất cả users trong hệ thống")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/role/{roleType}")
    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Lấy danh sách người dùng theo role", description = "Admin lấy danh sách users theo loại role cụ thể")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable RoleType roleType) {
        try {
            List<UserResponse> users = userService.getUsersByRole(roleType);
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}