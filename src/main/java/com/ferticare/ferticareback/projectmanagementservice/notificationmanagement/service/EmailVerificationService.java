package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto.VerifyResponse;

public interface EmailVerificationService {
    VerifyResponse verifyEmail(String token);
} 