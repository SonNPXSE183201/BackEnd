package com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDTO {
    private int totalUsers;
    private int totalDoctors;
    private int totalPatients;
    private int totalDepartments;
    private double monthlyGrowth;
    private int activeUsers;
    private int pendingApprovals;
    private double systemHealth;
    
    // Chart data
    private List<UserGrowthData> userGrowthData;
    private List<DepartmentData> departmentData;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGrowthData {
        private String month;
        private int users;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentData {
        private String department;
        private int patients;
        private String color;
    }
} 