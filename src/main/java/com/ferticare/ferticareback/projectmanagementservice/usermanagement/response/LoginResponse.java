package com.ferticare.ferticareback.projectmanagementservice.usermanagement.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoginResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String token;
}