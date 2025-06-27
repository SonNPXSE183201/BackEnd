package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller.admin;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.AdminOnly;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserCreateByAdminRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.UserResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/admin")
    public ResponseEntity<?> onlyAdminAccess() {
        return ResponseEntity.ok("Welcome, Admin!");
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/admin")
    public ResponseEntity<?> createUserByAdmin(@Valid @RequestBody UserCreateByAdminRequest request) {
        try {
            UserResponse response = userService.createUserByAdmin(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Log lỗi chi tiết để debug
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Tạo user thất bại: " + ex.getMessage());
        }
    }
}