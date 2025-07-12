package com.ferticare.ferticareback.projectmanagementservice.profile.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateManagerAdminProfileRequest {
    
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
    
    // ==================== MANAGER/ADMIN SPECIFIC FIELDS ====================
    @Size(max = 100, message = "Assigned department must not exceed 100 characters")
    private String assignedDepartment;
    
    private String extraPermissions;
} 