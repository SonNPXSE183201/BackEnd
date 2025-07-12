package com.ferticare.ferticareback.projectmanagementservice.profile.controller;

import com.ferticare.ferticareback.common.dto.GenericResponse;
import com.ferticare.ferticareback.common.dto.MessageDTO;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateCustomerProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateDoctorProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateManagerAdminProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.*;
import com.ferticare.ferticareback.projectmanagementservice.profile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // ==================== COMMON PROFILE API ====================
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        Object response = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // ==================== DOCTOR PROFILE API ====================
    @GetMapping("/doctor/me")
    public ResponseEntity<DoctorProfileResponse> getDoctorProfile(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        DoctorProfileResponse response = profileService.getDoctorProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/doctor/me")
    public ResponseEntity<?> updateDoctorProfile(
            @Valid @RequestBody UpdateDoctorProfileRequest request,
            Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        DoctorProfileResponse response = profileService.updateDoctorProfile(userId, request);
        return ResponseEntity.ok(GenericResponse.builder()
                .isSuccess(true)
                .message(MessageDTO.builder().messageDetail("Doctor profile updated successfully").build())
                .data(response)
                .build());
    }

    // ==================== CUSTOMER PROFILE API ====================
    @GetMapping("/customer/me")
    public ResponseEntity<CustomerProfileResponse> getCustomerProfile(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        CustomerProfileResponse response = profileService.getCustomerProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/customer/me")
    public ResponseEntity<?> updateCustomerProfile(
            @Valid @RequestBody UpdateCustomerProfileRequest request,
            Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        CustomerProfileResponse response = profileService.updateCustomerProfile(userId, request);
        return ResponseEntity.ok(GenericResponse.builder()
                .isSuccess(true)
                .message(MessageDTO.builder().messageDetail("Customer profile updated successfully").build())
                .data(response)
                .build());
    }

    // ==================== MANAGER/ADMIN PROFILE API ====================
    @GetMapping("/admin/me")
    public ResponseEntity<ManagerAdminProfileResponse> getManagerAdminProfile(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        ManagerAdminProfileResponse response = profileService.getManagerAdminProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/me")
    public ResponseEntity<?> updateManagerAdminProfile(
            @Valid @RequestBody UpdateManagerAdminProfileRequest request,
            Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        ManagerAdminProfileResponse response = profileService.updateManagerAdminProfile(userId, request);
        return ResponseEntity.ok(GenericResponse.builder()
                .isSuccess(true)
                .message(MessageDTO.builder().messageDetail("Manager/Admin profile updated successfully").build())
                .data(response)
                .build());
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<?> updateMyAvatar(
            @RequestParam("avatar") MultipartFile avatarFile,
            Principal principal) {
        try {
            UUID userId = UUID.fromString(principal.getName());
            String avatarUrl = profileService.updateAvatar(userId, avatarFile);
            
            Map<String, String> data = new HashMap<>();
            data.put("avatarUrl", avatarUrl);
            
            return ResponseEntity.ok(GenericResponse.builder()
                    .isSuccess(true)
                    .message(MessageDTO.builder().messageDetail("Avatar updated successfully").build())
                    .data(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder().messageDetail("Failed to update avatar: " + e.getMessage()).build())
                    .build());
        }
    }
}