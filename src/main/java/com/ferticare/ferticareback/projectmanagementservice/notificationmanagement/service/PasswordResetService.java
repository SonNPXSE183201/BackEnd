package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service;

public interface PasswordResetService {
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
} 