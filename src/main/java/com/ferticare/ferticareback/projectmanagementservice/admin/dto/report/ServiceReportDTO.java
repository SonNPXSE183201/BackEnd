package com.ferticare.ferticareback.projectmanagementservice.admin.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReportDTO {
    private String serviceId;
    private String serviceName;
    private String serviceType;
    private Integer totalPatients;
    private Integer totalAppointments;
    private Double successRate;
    private Double totalRevenue;
    private Double averageCost;
    private Double averageDuration; // Thời gian trung bình (phút)
    private String mostUsedDepartment; // Phòng ban sử dụng dịch vụ nhiều nhất
    
    // Dữ liệu theo thời gian
    private List<MonthlyData> monthlyData;
    
    // Thông tin bác sĩ thực hiện
    private List<DoctorServiceData> topDoctors;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyData {
        private String month; // "MM/YYYY"
        private Integer patientCount;
        private Integer appointmentCount;
        private Double revenue;
        private Double successRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorServiceData {
        private String doctorId;
        private String doctorName;
        private String departmentName;
        private Integer serviceCount;
        private Double successRate;
        private Double avgRating;
    }
} 