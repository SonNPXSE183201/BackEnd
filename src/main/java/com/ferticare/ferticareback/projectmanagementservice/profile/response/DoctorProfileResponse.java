package com.ferticare.ferticareback.projectmanagementservice.profile.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileResponse {
    private String avatarUrl;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private String specialty;
    private String qualification;
    private Integer experienceYears;
}