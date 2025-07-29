package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller.manager;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.ManagerOnly;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.ManagerDashboardDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@Tag(name = "Manager Management", description = "Manager APIs for team management")
public class ManagerController {

    private final UserService userService;

    @ManagerOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/dashboard")
    @Operation(summary = "Get manager dashboard data")
    public ResponseEntity<ManagerDashboardDTO> getDashboardData() {
        try {
            ManagerDashboardDTO dashboardData = userService.getManagerDashboardData();
            return ResponseEntity.ok(dashboardData);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Doctor management moved to ManagerDoctorManagementController
    // Endpoint: /api/manager/doctor-management

    // Doctor status toggle moved to ManagerDoctorManagementController
    // Endpoint: /api/manager/doctor-management/{id}/status

    @ManagerOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/appointments")
    @Operation(summary = "Get appointments for manager view")
    public ResponseEntity<?> getAppointments() {
        try {
            // Mock data cho appointments - sẽ thay bằng service thực tế
            return ResponseEntity.ok(java.util.List.of(
                java.util.Map.of(
                    "id", "1",
                    "time", "09:00",
                    "date", "2024-01-15",
                    "patientName", "Nguyễn Thị A",
                    "doctorName", "BS. Trần Văn B",
                    "type", "Khám tổng quát",
                    "status", "confirmed"
                ),
                java.util.Map.of(
                    "id", "2",
                    "time", "14:30",
                    "date", "2024-01-15",
                    "patientName", "Lê Thị C",
                    "doctorName", "BS. Nguyễn Văn D",
                    "type", "Tái khám",
                    "status", "pending"
                )
            ));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ManagerOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/appointments")
    @Operation(summary = "Create appointment")
    public ResponseEntity<?> createAppointment(@RequestBody Object appointmentRequest) {
        try {
            // Implementation sẽ được thêm sau khi có appointment service
            return ResponseEntity.ok("Appointment created successfully");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Tạo lịch hẹn thất bại: " + ex.getMessage());
        }
    }

    @ManagerOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/reports")
    @Operation(summary = "Get manager reports")
    public ResponseEntity<?> getReports() {
        try {
            // Mock data cho reports
            return ResponseEntity.ok(java.util.Map.of(
                "totalDoctors", 12,
                "activeDoctors", 10,
                "totalAppointments", 85,
                "todayAppointments", 15,
                "averageRating", 4.7,
                "patientSatisfaction", 92
            ));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Legacy endpoint for compatibility
    @ManagerOnly
    @GetMapping("/report")
    public ResponseEntity<?> viewReport() {
        return ResponseEntity.ok("Welcome Manager! Here is your report.");
    }
}