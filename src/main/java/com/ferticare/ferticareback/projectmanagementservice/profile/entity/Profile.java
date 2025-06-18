package com.ferticare.ferticareback.projectmanagementservice.profile.entity;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "profile_id")
    private UUID profileId;

    @OneToOne(fetch = FetchType.EAGER) // hoặc LAZY nếu dùng @Transactional
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Dành cho Doctor
    private String specialty;
    private String qualification;
    private Integer experienceYears;
    @Column(name = "work_schedule") // ✅ CHUẨN
    private String workSchedule;
    private Double rating;
    private Integer caseCount;
    private String notes;
    private String status;

    // Dành cho Customer
    private String maritalStatus;
    private String healthBackground;

    // Dành cho Manager / Admin
    private String assignedDepartment;
    private String extraPermissions;
}