package com.ferticare.ferticareback.projectmanagementservice.usermanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.GoogleLoginRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.LoginResponse;

public interface GoogleAuthService {
    LoginResponse authenticateGoogleUser(GoogleLoginRequest googleLoginRequest);
    boolean verifyGoogleToken(String token);
} 