package com.ferticare.ferticareback.projectmanagementservice.profile.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileResponse {
    // User information (from users table)
    private String avatarUrl;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String address;
    
    // Customer personal information (from profile table)
    private String maritalStatus;
    private String healthBackground;
}