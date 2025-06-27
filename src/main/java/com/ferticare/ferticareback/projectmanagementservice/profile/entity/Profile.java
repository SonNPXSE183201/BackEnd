package com.ferticare.ferticareback.projectmanagementservice.profile.entity;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "Profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID profileId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String specialty;
    private String qualification;
    private Integer experienceYears;
    private String workSchedule;
    private Double rating;
    private Integer caseCount;
    private String notes;
    private String status;

    private String maritalStatus;
    private String healthBackground;

    private String assignedDepartment;
    private String extraPermissions;
}