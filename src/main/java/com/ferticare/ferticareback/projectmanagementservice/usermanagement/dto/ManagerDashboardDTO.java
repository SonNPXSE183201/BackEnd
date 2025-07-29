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
public class ManagerDashboardDTO {
    private int totalDoctors;
    private int activeDoctors;
    private int totalAppointments;
    private int todayAppointments;
    private double averageRating;
    private int patientSatisfaction;
    
    // Chart data
    private List<PerformanceData> performanceData;
    private List<DepartmentData> departmentData;
    private List<DoctorInfo> topDoctors;
    private List<AppointmentInfo> recentAppointments;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceData {
        private String month;
        private int patients;
        private int appointments;
        private int success;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentData {
        private String name;
        private int value;
        private String color;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorInfo {
        private String id;
        private String name;
        private String specialty;
        private int patientCount;
        private int todayAppointments;
        private double performance;
        private String status;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentInfo {
        private String time;
        private String date;
        private String patientName;
        private String doctorName;
        private String type;
        private String status;
    }
} 