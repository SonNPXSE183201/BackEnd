package com.ferticare.ferticareback.projectmanagementservice.profile.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAdminProfileResponse {
    // User information (from users table)
    private String avatarUrl;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String address;
    
    // Work information (from profile table)
    private String assignedDepartment;
    private String extraPermissions;
}