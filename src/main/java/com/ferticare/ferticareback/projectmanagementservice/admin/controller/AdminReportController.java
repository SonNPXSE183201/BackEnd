package com.ferticare.ferticareback.projectmanagementservice.admin.controller;

import com.ferticare.ferticareback.common.dto.GenericResponse;
import com.ferticare.ferticareback.common.dto.MessageDTO;
import com.ferticare.ferticareback.projectmanagementservice.admin.dto.report.*;
import com.ferticare.ferticareback.projectmanagementservice.admin.service.ReportService;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.AdminOnly;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@Tag(name = "Admin Reports", description = "APIs for system reports and analytics")
public class AdminReportController {

    private final ReportService reportService;

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/departments/{departmentId}")
    @Operation(summary = "Get department report")
    public ResponseEntity<?> getDepartmentReport(
            @PathVariable UUID departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            DepartmentReportDTO report = reportService.getDepartmentReport(departmentId, fromDate, toDate);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy báo cáo phòng ban thành công"), 
                    null, report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/services/{serviceId}")
    @Operation(summary = "Get service report")
    public ResponseEntity<?> getServiceReport(
            @PathVariable UUID serviceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            ServiceReportDTO report = reportService.getServiceReport(serviceId, fromDate, toDate);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy báo cáo dịch vụ thành công"), 
                    null, report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/system")
    @Operation(summary = "Get system report")
    public ResponseEntity<?> getSystemReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            SystemReportDTO report = reportService.getSystemReport(fromDate, toDate);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy báo cáo hệ thống thành công"), 
                    null, report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/history")
    @Operation(summary = "Get report history")
    public ResponseEntity<?> getReportHistory(
            @RequestParam(required = false, defaultValue = "ALL") String reportType,
            Pageable pageable) {
        try {
            Page<Map<String, Object>> history = reportService.getReportHistory(reportType, pageable);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy lịch sử báo cáo thành công"), 
                    null, history));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/export")
    @Operation(summary = "Export report to file")
    public ResponseEntity<?> exportReport(@Valid @RequestBody ExportReportRequest request) {
        try {
            Resource reportFile = reportService.exportReport(request);
            
            // Tạo tên file báo cáo
            String filename = "report_" + request.getReportType().toLowerCase() + "_" + 
                             System.currentTimeMillis() + "." + request.getFormat().toLowerCase();
            
            // Xác định MediaType
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if ("PDF".equalsIgnoreCase(request.getFormat())) {
                mediaType = MediaType.APPLICATION_PDF;
            } else if ("EXCEL".equalsIgnoreCase(request.getFormat())) {
                mediaType = MediaType.parseMediaType("application/vnd.ms-excel");
            } else if ("CSV".equalsIgnoreCase(request.getFormat())) {
                mediaType = MediaType.parseMediaType("text/csv");
            }
            
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(reportFile);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi xuất báo cáo: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/schedule")
    @Operation(summary = "Schedule automatic report generation")
    public ResponseEntity<?> scheduleReport(
            @RequestParam String reportType,
            @RequestParam String schedule, // cron expression (e.g., "0 0 1 * *" for monthly)
            @Valid @RequestBody ExportReportRequest request) {
        try {
            reportService.scheduleReport(reportType, schedule, request);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Đã lập lịch tạo báo cáo tự động"), 
                    null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi lập lịch báo cáo: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/schedules")
    @Operation(summary = "Get report schedules")
    public ResponseEntity<?> getReportSchedules() {
        try {
            List<Map<String, Object>> schedules = reportService.getReportSchedules();
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy danh sách lịch báo cáo thành công"), 
                    null, schedules));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }
}