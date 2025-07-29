package com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDoctorDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private String specialization;
    
    // Thông tin phòng ban cũ (chuỗi)
    private String department;
    
    // Thông tin phòng ban mới (object)
    private DepartmentInfo departmentInfo;
    
    private Integer experience;
    private Double rating;
    private String status;
    private Double salary;
    private String contractType;
    private LocalDateTime joinDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Schedule information
    private DoctorScheduleInfo schedule;
    
    // Performance metrics
    private DoctorPerformance performance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentInfo {
        private String id;
        private String name;
        private String location;
        private String headDoctor;
        private Boolean isActive;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorScheduleInfo {
        private WeeklySchedule monday;
        private WeeklySchedule tuesday;
        private WeeklySchedule wednesday;
        private WeeklySchedule thursday;
        private WeeklySchedule friday;
        private WeeklySchedule saturday;
        private WeeklySchedule sunday;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WeeklySchedule {
            private boolean morning;
            private boolean afternoon;
            private boolean evening;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorPerformance {
        private int totalPatients;
        private int completedTreatments;
        private int successRate;
        private double avgRating;
        private int patientSatisfaction;
        private int monthlyAppointments;
    }
} 