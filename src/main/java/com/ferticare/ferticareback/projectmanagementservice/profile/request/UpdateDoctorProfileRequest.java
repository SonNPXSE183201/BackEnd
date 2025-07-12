package com.ferticare.ferticareback.projectmanagementservice.profile.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateDoctorProfileRequest {
    
    // ==================== USER INFORMATION (Common fields) ====================
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Gender must be MALE, FEMALE, or OTHER")
    private String gender;
    
    private LocalDate dateOfBirth;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid (10-15 digits)")
    private String phone;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
    
    // ==================== DOCTOR SPECIFIC FIELDS ====================
    @Size(max = 255, message = "Specialty must not exceed 255 characters")
    private String specialty;
    
    @Size(max = 255, message = "Qualification must not exceed 255 characters")
    private String qualification;
    
    @Min(value = 0, message = "Experience years must be non-negative")
    @Max(value = 50, message = "Experience years must not exceed 50")
    private Integer experienceYears;
    
    private String notes;
    
    @Pattern(regexp = "^(ACTIVE|INACTIVE|SUSPENDED|ON_LEAVE)$", 
             message = "Status must be ACTIVE, INACTIVE, SUSPENDED, or ON_LEAVE")
    private String status;
} 