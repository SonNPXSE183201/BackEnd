package com.ferticare.ferticareback.projectmanagementservice.profile.entity;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
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

    @Column(columnDefinition = "NVARCHAR(255)")
    private String specialty;
    
    @Column(columnDefinition = "NVARCHAR(255)")
    private String specialization;
    
    @Column(columnDefinition = "NVARCHAR(500)")
    private String qualification;
    
    private Integer experienceYears;
    
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String workSchedule;
    
    private Double rating;
    private Integer caseCount;
    
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String notes;
    
    @Column(columnDefinition = "NVARCHAR(50)")
    private String status;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String maritalStatus;
    
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String healthBackground;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String assignedDepartment;
    
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String extraPermissions;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
    
    // Trường department cũ (String) được giữ lại để tương thích ngược
    @Column(name = "department_name", columnDefinition = "NVARCHAR(255)")
    private String departmentName; 
    
    @Column(columnDefinition = "NVARCHAR(100)")
    private String contractType; // Loại hợp đồng (full-time, part-time, contract)
}