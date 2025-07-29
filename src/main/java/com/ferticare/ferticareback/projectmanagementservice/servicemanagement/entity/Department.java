package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity;

import com.ferticare.ferticareback.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "departments")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;
    
    @Column(columnDefinition = "NVARCHAR(255)")
    private String location;
    private String headDoctor;
    private String contactInfo;
    private Boolean isActive;
    private Integer capacity;
    private Integer doctorCount;
    private Integer patientCount;
    
    public LocalDateTime getCreatedAt() {
        return getCreatedDate();
    }
    
    public LocalDateTime getUpdatedAt() {
        return getUpdatedDate();
    }
} 