package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.ServiceRequestService;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/service-request")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ServiceRequestService serviceRequestService;

    @PostMapping
    public ResponseEntity<?> submitRequest(
            @RequestBody ServiceRequestDTO dto,
            HttpServletRequest request
    ) {
        try {
            String jwt = jwtUtil.extractJwtFromRequest(request);
            String userId = jwtUtil.extractUserId(jwt);
            return serviceRequestService.handleRequest(UUID.fromString(userId), dto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Yêu cầu không hợp lệ: " + e.getMessage());
        }
    }
}