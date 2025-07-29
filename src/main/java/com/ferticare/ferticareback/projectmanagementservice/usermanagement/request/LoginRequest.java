package com.ferticare.ferticareback.projectmanagementservice.usermanagement.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}