package com.ferticare.ferticareback.projectmanagementservice.usermanagement.request;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String googleToken;  // Google ID token
    private String email;
    private String fullName;
    private String avatarUrl;
} 