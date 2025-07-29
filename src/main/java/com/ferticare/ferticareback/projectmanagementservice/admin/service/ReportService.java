package com.ferticare.ferticareback.projectmanagementservice.admin.service;

import com.ferticare.ferticareback.projectmanagementservice.admin.dto.report.*;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReportService {
    /**
     * Lấy báo cáo chi tiết về một phòng ban
     */
    DepartmentReportDTO getDepartmentReport(UUID departmentId, LocalDate fromDate, LocalDate toDate);
    
    /**
     * Lấy báo cáo chi tiết về một dịch vụ
     */
    ServiceReportDTO getServiceReport(UUID serviceId, LocalDate fromDate, LocalDate toDate);
    
    /**
     * Lấy báo cáo tổng hợp toàn bộ hệ thống
     */
    SystemReportDTO getSystemReport(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Lấy danh sách báo cáo đã tạo trước đó
     */
    Page<Map<String, Object>> getReportHistory(String reportType, Pageable pageable);
    
    /**
     * Xuất báo cáo theo định dạng
     * @return Resource đại diện cho file được tạo ra
     */
    Resource exportReport(ExportReportRequest request);
    
    /**
     * Lưu báo cáo mới vào hệ thống
     * @return ID của báo cáo đã lưu
     */
    String saveReport(String reportType, String reportName, Object data);
    
    /**
     * Tạo lịch tạo báo cáo tự động
     */
    void scheduleReport(String reportType, String schedule, ExportReportRequest request);
    
    /**
     * Lấy danh sách lịch báo cáo đã đặt
     */
    List<Map<String, Object>> getReportSchedules();
} 