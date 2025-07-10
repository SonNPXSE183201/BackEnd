package com.ferticare.ferticareback.projectmanagementservice.profile.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    
    // ==================== USER INFORMATION ====================
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Gender must be MALE, FEMALE, or OTHER")
    private String gender;
    
    private LocalDate dateOfBirth;
    
    @Email(message = "Email must be valid format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid (10-15 digits)")
    private String phone;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
    
    // ==================== DOCTOR PROFILE FIELDS ====================
    @Size(max = 100, message = "Specialty must not exceed 100 characters")
    private String specialty;
    
    @Size(max = 200, message = "Qualification must not exceed 200 characters")
    private String qualification;
    
    @Min(value = 0, message = "Experience years must be non-negative")
    @Max(value = 50, message = "Experience years must not exceed 50")
    private Integer experienceYears;
    
    @Size(max = 500, message = "Work schedule must not exceed 500 characters")
    private String workSchedule;
    
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    @Pattern(regexp = "^(ACTIVE|INACTIVE|SUSPENDED|ON_LEAVE)$", message = "Status must be ACTIVE, INACTIVE, SUSPENDED, or ON_LEAVE")
    private String status;
    
    // Thêm fields mới cho Doctor
    @Size(max = 100, message = "License number must not exceed 100 characters")
    private String licenseNumber;
    
    @Size(max = 200, message = "Education must not exceed 200 characters")
    private String education;
    
    @Size(max = 500, message = "Certifications must not exceed 500 characters")
    private String certifications;
    
    @Size(max = 500, message = "Languages must not exceed 500 characters")
    private String languages;
    
    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;
    
    // ==================== CUSTOMER PROFILE FIELDS ====================
    @Pattern(regexp = "^(SINGLE|MARRIED|DIVORCED|WIDOWED|OTHER)$", message = "Marital status must be SINGLE, MARRIED, DIVORCED, WIDOWED, or OTHER")
    private String maritalStatus;
    
    @Size(max = 2000, message = "Health background must not exceed 2000 characters")
    private String healthBackground;
    
    // Thêm fields mới cho Customer
    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String emergencyContactName;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Emergency contact phone must be valid")
    private String emergencyContactPhone;
    
    @Size(max = 100, message = "Emergency contact relationship must not exceed 100 characters")
    private String emergencyContactRelationship;
    
    @Size(max = 500, message = "Allergies must not exceed 500 characters")
    private String allergies;
    
    @Size(max = 500, message = "Current medications must not exceed 500 characters")
    private String currentMedications;
    
    @Size(max = 1000, message = "Medical history must not exceed 1000 characters")
    private String medicalHistory;
    
    @Size(max = 100, message = "Insurance provider must not exceed 100 characters")
    private String insuranceProvider;
    
    @Size(max = 100, message = "Insurance number must not exceed 100 characters")
    private String insuranceNumber;
    
    // ==================== MANAGER/ADMIN PROFILE FIELDS ====================
    @Size(max = 100, message = "Assigned department must not exceed 100 characters")
    private String assignedDepartment;
    
    @Size(max = 1000, message = "Extra permissions must not exceed 1000 characters")
    private String extraPermissions;
    
    // Thêm fields mới cho Manager/Admin
    @Size(max = 100, message = "Employee ID must not exceed 100 characters")
    private String employeeId;
    
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;
    
    @Size(max = 100, message = "Manager name must not exceed 100 characters")
    private String managerName;
    
    private LocalDate hireDate;
    
    @Min(value = 0, message = "Salary must be non-negative")
    private Double salary;
    
    @Size(max = 500, message = "Access levels must not exceed 500 characters")
    private String accessLevels;
    
    @Size(max = 1000, message = "Responsibilities must not exceed 1000 characters")
    private String responsibilities;
}