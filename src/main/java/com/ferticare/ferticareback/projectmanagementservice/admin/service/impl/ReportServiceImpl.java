package com.ferticare.ferticareback.projectmanagementservice.admin.service.impl;

import com.ferticare.ferticareback.common.exception.AppException;
import com.ferticare.ferticareback.projectmanagementservice.admin.dto.report.*;
import com.ferticare.ferticareback.projectmanagementservice.admin.service.ReportService;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DepartmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DoctorWorkScheduleRepository;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final DepartmentRepository departmentRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;

    @Override
    public DepartmentReportDTO getDepartmentReport(UUID departmentId, LocalDate fromDate, LocalDate toDate) {
        // Lấy thông tin phòng ban
        Department department = departmentRepository.findById(Long.parseLong(departmentId.toString()))
                .orElseThrow(() -> new AppException("DEPARTMENT_NOT_FOUND", "Không tìm thấy phòng ban"));
        
        // TODO: Lấy thông tin bệnh nhân, lịch hẹn từ các repository liên quan
        
        // Tạo dữ liệu mẫu cho môi trường phát triển
        List<DepartmentReportDTO.MonthlyData> monthlyData = createMockMonthlyData();
        List<DepartmentReportDTO.DoctorPerformance> topDoctors = createMockDoctorPerformance();
        
        return DepartmentReportDTO.builder()
                .departmentId(String.valueOf(department.getId()))
                .departmentName(department.getName())
                .totalDoctors(department.getDoctorCount())
                .totalPatients(department.getPatientCount())
                .totalAppointments(135) // Dữ liệu mẫu
                .successRate(78.5)      // Dữ liệu mẫu
                .revenue(2450000000.0)  // Dữ liệu mẫu - 2.45 tỷ VND
                .capacity(department.getCapacity())
                .utilizationRate(85.3)  // Dữ liệu mẫu
                .monthlyData(monthlyData)
                .topDoctors(topDoctors)
                .build();
    }

    @Override
    public ServiceReportDTO getServiceReport(UUID serviceId, LocalDate fromDate, LocalDate toDate) {
        // TODO: Lấy thông tin dịch vụ khi có service repository
        
        // Tạo dữ liệu mẫu cho môi trường phát triển
        List<ServiceReportDTO.MonthlyData> monthlyData = createServiceMockMonthlyData();
        List<ServiceReportDTO.DoctorServiceData> topDoctors = createServiceMockDoctorData();
        
        return ServiceReportDTO.builder()
                .serviceId(serviceId.toString())
                .serviceName("Thụ tinh trong ống nghiệm (IVF)") // Dữ liệu mẫu
                .serviceType("Điều trị")
                .totalPatients(87)
                .totalAppointments(120)
                .successRate(65.8)
                .totalRevenue(1750000000.0) // 1.75 tỷ VND
                .averageCost(25000000.0) // 25 triệu VND
                .averageDuration(90.0) // 90 phút
                .mostUsedDepartment("IVF")
                .monthlyData(monthlyData)
                .topDoctors(topDoctors)
                .build();
    }

    @Override
    public SystemReportDTO getSystemReport(LocalDate fromDate, LocalDate toDate) {
        // Lấy thông tin phòng ban
        List<Department> departments = departmentRepository.findAll();
        int totalDoctors = 0;
        int totalPatients = 0;
        
        for (Department dept : departments) {
            totalDoctors += dept.getDoctorCount() != null ? dept.getDoctorCount() : 0;
            totalPatients += dept.getPatientCount() != null ? dept.getPatientCount() : 0;
        }
        
        // Tạo dữ liệu mẫu cho môi trường phát triển
        List<SystemReportDTO.DepartmentSummary> deptSummaries = departments.stream()
                .map(this::createDepartmentSummary)
                .collect(Collectors.toList());
        
        List<SystemReportDTO.ServiceSummary> serviceSummaries = createMockServiceSummaries();
        List<SystemReportDTO.MonthlyData> monthlyData = createSystemMockMonthlyData();
        
        SystemReportDTO.SystemOverview overview = SystemReportDTO.SystemOverview.builder()
                .totalDepartments(departments.size())
                .totalDoctors(totalDoctors)
                .totalPatients(totalPatients)
                .totalAppointments(450) // Dữ liệu mẫu
                .totalRevenue(5750000000.0) // 5.75 tỷ VND
                .overallSuccessRate(72.5)
                .patientSatisfaction(88.7)
                .appointmentsThisMonth(45)
                .revenueThisMonth(580000000.0)
                .growthRate(4.5)
                .build();
                
        SystemReportDTO.PerformanceMetrics metrics = createMockPerformanceMetrics();
                
        return SystemReportDTO.builder()
                .reportId(UUID.randomUUID().toString())
                .reportName("Báo cáo hệ thống " + DateTimeFormatter.ofPattern("yyyy-MM").format(LocalDateTime.now()))
                .generatedAt(LocalDateTime.now())
                .generatedBy("Admin")
                .overview(overview)
                .departmentSummaries(deptSummaries)
                .serviceSummaries(serviceSummaries)
                .monthlyData(monthlyData)
                .performance(metrics)
                .build();
    }

    @Override
    public Page<Map<String, Object>> getReportHistory(String reportType, Pageable pageable) {
        // TODO: Thực hiện khi có report repository
        
        // Tạo dữ liệu mẫu
        List<Map<String, Object>> mockHistory = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> report = new HashMap<>();
            report.put("id", UUID.randomUUID().toString());
            report.put("name", "Báo cáo " + reportType + " " + (i + 1));
            report.put("type", reportType);
            report.put("createdAt", LocalDateTime.now().minusDays(i));
            report.put("createdBy", "Admin");
            report.put("format", "PDF");
            mockHistory.add(report);
        }
        
        return new PageImpl<>(mockHistory, pageable, mockHistory.size());
    }

    @Override
    public Resource exportReport(ExportReportRequest request) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // TODO: Thực hiện xuất báo cáo thực tế khi có các thư viện xuất PDF/Excel
        // Hiện tại trả về mẫu byte array
        try {
            byte[] mockPdf = "Mock PDF Content for report".getBytes();
            baos.write(mockPdf);
            return new ByteArrayResource(baos.toByteArray());
        } catch (Exception e) {
            log.error("Error exporting report", e);
            throw new AppException("EXPORT_FAILED", "Xuất báo cáo thất bại: " + e.getMessage());
        }
    }

    @Override
    public String saveReport(String reportType, String reportName, Object data) {
        // TODO: Thực hiện khi có report repository
        return UUID.randomUUID().toString();
    }

    @Override
    public void scheduleReport(String reportType, String schedule, ExportReportRequest request) {
        // TODO: Thực hiện khi có chức năng lập lịch
        log.info("Scheduled report creation: {} with schedule {}", reportType, schedule);
    }

    @Override
    public List<Map<String, Object>> getReportSchedules() {
        // TODO: Thực hiện khi có report schedule repository
        
        // Tạo dữ liệu mẫu
        List<Map<String, Object>> mockSchedules = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> schedule = new HashMap<>();
            schedule.put("id", UUID.randomUUID().toString());
            schedule.put("reportType", i % 2 == 0 ? "DEPARTMENT" : "SYSTEM");
            schedule.put("schedule", "0 0 1 * *"); // Mỗi đầu tháng
            schedule.put("createdBy", "Admin");
            schedule.put("nextRun", LocalDateTime.now().plusMonths(1).withDayOfMonth(1));
            mockSchedules.add(schedule);
        }
        
        return mockSchedules;
    }
    
    // Helper methods để tạo dữ liệu mẫu
    
    private List<DepartmentReportDTO.MonthlyData> createMockMonthlyData() {
        List<DepartmentReportDTO.MonthlyData> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            data.add(DepartmentReportDTO.MonthlyData.builder()
                    .month(month.format(DateTimeFormatter.ofPattern("MM/yyyy")))
                    .patientCount(10 + i * 3)
                    .appointmentCount(15 + i * 4)
                    .revenue(250000000.0 + i * 50000000.0)
                    .successRate(70.0 + i * 1.5)
                    .build());
        }
        
        return data;
    }
    
    private List<DepartmentReportDTO.DoctorPerformance> createMockDoctorPerformance() {
        List<DepartmentReportDTO.DoctorPerformance> data = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            data.add(DepartmentReportDTO.DoctorPerformance.builder()
                    .doctorId(UUID.randomUUID().toString())
                    .doctorName("Bác sĩ " + (char)('A' + i))
                    .patientCount(15 + i * 2)
                    .successRate(75.0 + i * 1.0)
                    .avgRating(4.0 + (i * 0.2))
                    .build());
        }
        
        return data;
    }
    
    private List<ServiceReportDTO.MonthlyData> createServiceMockMonthlyData() {
        List<ServiceReportDTO.MonthlyData> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            data.add(ServiceReportDTO.MonthlyData.builder()
                    .month(month.format(DateTimeFormatter.ofPattern("MM/yyyy")))
                    .patientCount(8 + i * 2)
                    .appointmentCount(12 + i * 3)
                    .revenue(180000000.0 + i * 30000000.0)
                    .successRate(65.0 + i * 1.0)
                    .build());
        }
        
        return data;
    }
    
    private List<ServiceReportDTO.DoctorServiceData> createServiceMockDoctorData() {
        List<ServiceReportDTO.DoctorServiceData> data = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            data.add(ServiceReportDTO.DoctorServiceData.builder()
                    .doctorId(UUID.randomUUID().toString())
                    .doctorName("Bác sĩ " + (char)('A' + i))
                    .departmentName("IVF")
                    .serviceCount(8 + i * 3)
                    .successRate(65.0 + i * 2.5)
                    .avgRating(4.0 + (i * 0.2))
                    .build());
        }
        
        return data;
    }
    
    private SystemReportDTO.DepartmentSummary createDepartmentSummary(Department dept) {
        return SystemReportDTO.DepartmentSummary.builder()
                .departmentId(String.valueOf(dept.getId()))
                .departmentName(dept.getName())
                .doctorCount(dept.getDoctorCount())
                .patientCount(dept.getPatientCount())
                .revenue((double)(dept.getPatientCount() != null ? dept.getPatientCount() : 0) * 50000000.0) // Dữ liệu mẫu
                .successRate(70.0 + (Math.random() * 10.0))
                .utilizationRate(75.0 + (Math.random() * 15.0))
                .build();
    }
    
    private List<SystemReportDTO.ServiceSummary> createMockServiceSummaries() {
        List<SystemReportDTO.ServiceSummary> data = new ArrayList<>();
        
        String[] services = {"IVF", "IUI", "Tư vấn sinh sản", "Khám sức khỏe sinh sản", "Điều trị nội tiết"};
        
        for (int i = 0; i < services.length; i++) {
            data.add(SystemReportDTO.ServiceSummary.builder()
                    .serviceId(UUID.randomUUID().toString())
                    .serviceName(services[i])
                    .appointmentCount(50 + i * 10)
                    .revenue(500000000.0 + i * 200000000.0)
                    .successRate(65.0 + i * 3.0)
                    .build());
        }
        
        return data;
    }
    
    private List<SystemReportDTO.MonthlyData> createSystemMockMonthlyData() {
        List<SystemReportDTO.MonthlyData> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            data.add(SystemReportDTO.MonthlyData.builder()
                    .month(month.format(DateTimeFormatter.ofPattern("MM/yyyy")))
                    .appointmentCount(60 + i * 8)
                    .patientCount(45 + i * 5)
                    .revenue(850000000.0 + i * 120000000.0)
                    .successRate(68.0 + i * 1.2)
                    .build());
        }
        
        return data;
    }
    
    private SystemReportDTO.PerformanceMetrics createMockPerformanceMetrics() {
        Map<String, Double> deptPerformance = new HashMap<>();
        deptPerformance.put("IVF", 85.5);
        deptPerformance.put("IUI", 80.2);
        deptPerformance.put("Nội tiết sinh sản", 78.7);
        deptPerformance.put("Phụ khoa", 82.3);
        
        return SystemReportDTO.PerformanceMetrics.builder()
                .averageWaitingTime(15.5) // 15.5 phút
                .appointmentCompletionRate(92.5)
                .doctorUtilizationRate(87.3)
                .cancelledAppointments(12)
                .revenueTrend(4.8) // +4.8%
                .departmentPerformance(deptPerformance)
                .build();
    }
} 