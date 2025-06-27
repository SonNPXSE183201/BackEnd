package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto.VerifyResponse;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify-email")
    public ResponseEntity<VerifyResponse> verifyEmail(@RequestParam("token") String token) {
        try {
            VerifyResponse response = emailVerificationService.verifyEmail(token);
            
            if (response.success()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(
                new VerifyResponse("Lỗi hệ thống", false)
            );
        }
    }
}