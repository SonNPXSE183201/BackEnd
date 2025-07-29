package com.ferticare.ferticareback.projectmanagementservice.profile.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCustomerProfileRequest {
    
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
    
    // ==================== CUSTOMER SPECIFIC FIELDS ====================
    @Pattern(regexp = "^(SINGLE|MARRIED|DIVORCED|WIDOWED|OTHER)$", 
             message = "Marital status must be SINGLE, MARRIED, DIVORCED, WIDOWED, or OTHER")
    private String maritalStatus;
    
    private String healthBackground;
} 