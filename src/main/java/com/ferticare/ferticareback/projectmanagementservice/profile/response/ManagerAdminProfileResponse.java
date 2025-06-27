package com.ferticare.ferticareback.projectmanagementservice.profile.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAdminProfileResponse {
    private String avatarUrl;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private String assignedDepartment;
    private String extraPermissions;
}