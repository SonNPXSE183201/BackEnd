package com.ferticare.ferticareback.projectmanagementservice.admin.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemReportDTO {
    private String reportId;
    private String reportName;
    private LocalDateTime generatedAt;
    private String generatedBy;
    
    // Thông tin tổng quan hệ thống
    private SystemOverview overview;
    
    // Thống kê theo phòng ban
    private List<DepartmentSummary> departmentSummaries;
    
    // Thống kê theo dịch vụ
    private List<ServiceSummary> serviceSummaries;
    
    // Dữ liệu theo thời gian
    private List<MonthlyData> monthlyData;
    
    // KPI và chỉ số hiệu suất
    private PerformanceMetrics performance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemOverview {
        private Integer totalDepartments;
        private Integer totalDoctors;
        private Integer totalPatients;
        private Integer totalAppointments;
        private Double totalRevenue;
        private Double overallSuccessRate;
        private Double patientSatisfaction; // 0-100%
        private Integer appointmentsThisMonth;
        private Double revenueThisMonth;
        private Double growthRate; // % tăng trưởng so với tháng trước
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentSummary {
        private String departmentId;
        private String departmentName;
        private Integer doctorCount;
        private Integer patientCount;
        private Double revenue;
        private Double successRate;
        private Double utilizationRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceSummary {
        private String serviceId;
        private String serviceName;
        private Integer appointmentCount;
        private Double revenue;
        private Double successRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyData {
        private String month; // "MM/YYYY"
        private Integer appointmentCount;
        private Integer patientCount;
        private Double revenue;
        private Double successRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceMetrics {
        private Double averageWaitingTime; // Phút
        private Double appointmentCompletionRate; // % hoàn thành cuộc hẹn
        private Double doctorUtilizationRate; // % thời gian bác sĩ làm việc
        private Integer cancelledAppointments;
        private Double revenueTrend; // % thay đổi doanh thu
        private Map<String, Double> departmentPerformance; // Map<DepartmentName, PerformanceScore>
    }
} 