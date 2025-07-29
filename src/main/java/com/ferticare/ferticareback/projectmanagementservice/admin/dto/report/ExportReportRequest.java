package com.ferticare.ferticareback.projectmanagementservice.admin.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportReportRequest {
    @NotBlank(message = "Loại báo cáo không được để trống")
    private String reportType; // DEPARTMENT, SERVICE, SYSTEM
    
    private String id; // departmentId hoặc serviceId, null cho báo cáo hệ thống
    
    private String format; // PDF, EXCEL, CSV
    
    private LocalDate fromDate;
    
    private LocalDate toDate;
    
    private List<String> includeSections; // Các section muốn bao gồm trong báo cáo
    
    private String sortBy;
    
    private Boolean descending;
    
    // Tùy chọn bổ sung
    private boolean includeCharts;
    
    private String locale; // vi-VN, en-US
} 