package com.ferticare.ferticareback.projectmanagementservice.profile.entity;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "profile_id")
    private UUID profileId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ==================== DOCTOR FIELDS (based on actual database schema) ====================
    private String specialty;
    private String qualification;
    @Column(name = "experience_years")
    private Integer experienceYears;
    private Double rating;
    @Column(name = "case_count")
    private Integer caseCount;
    private String notes;
    @Column(nullable = false)
    private String status;

    // ==================== CUSTOMER FIELDS (based on actual database schema) ====================
    @Column(name = "marital_status")
    private String maritalStatus;
    @Column(name = "health_background")
    private String healthBackground;

    // ==================== MANAGER/ADMIN FIELDS (based on actual database schema) ====================
    @Column(name = "assigned_department")
    private String assignedDepartment;
    @Column(name = "extra_permissions")
    private String extraPermissions;
}