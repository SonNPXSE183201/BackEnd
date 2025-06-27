package com.ferticare.ferticareback.projectmanagementservice.usermanagement.response;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.Gender;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}