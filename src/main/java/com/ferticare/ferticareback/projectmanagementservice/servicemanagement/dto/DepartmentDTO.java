package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Long id;
    
    @NotBlank(message = "Tên phòng ban không được để trống")
    @Size(min = 2, max = 100, message = "Tên phòng ban phải từ 2-100 ký tự")
    private String name;
    
    private String description;
    private String location;
    private String headDoctor;
    private String contactInfo;
    private Boolean isActive;
    private Integer capacity;
    private Integer doctorCount;
    private Integer patientCount;
    private String createdAt;
    private String updatedAt;
} 