package com.ferticare.ferticareback.features.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}