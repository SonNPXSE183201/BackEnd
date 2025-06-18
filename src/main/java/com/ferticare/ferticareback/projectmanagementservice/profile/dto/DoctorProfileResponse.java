package com.ferticare.ferticareback.projectmanagementservice.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileResponse implements BaseProfileResponse {
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
