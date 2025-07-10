package com.ferticare.ferticareback.projectmanagementservice.profile.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileResponse {
    // User information (from users table)
    private String avatarUrl;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String address;
    
    // Doctor professional information (from profile table)
    private String specialty;
    private String qualification;
    private Integer experienceYears;
    private String workSchedule;
    private Double rating;
    private Integer caseCount;
    private String notes;
    private String status;
}