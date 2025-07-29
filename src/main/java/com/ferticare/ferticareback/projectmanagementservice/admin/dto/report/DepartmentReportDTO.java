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
public class DepartmentReportDTO {
    private String departmentId;
    private String departmentName;
    private Integer totalDoctors;
    private Integer totalPatients;
    private Integer totalAppointments;
    private Double successRate;  // Tỷ lệ thành công (0-100%)
    private Double revenue;      // Doanh thu
    private Integer capacity;    // Công suất khám chữa bệnh
    private Double utilizationRate;  // Tỉ lệ sử dụng (0-100%)
    
    // Dữ liệu theo thời gian (theo tháng)
    private List<MonthlyData> monthlyData;
    
    // Thông tin về bác sĩ nổi bật
    private List<DoctorPerformance> topDoctors;
    
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
    public static class DoctorPerformance {
        private String doctorId;
        private String doctorName;
        private Integer patientCount;
        private Double successRate;
        private Double avgRating;
    }
} 