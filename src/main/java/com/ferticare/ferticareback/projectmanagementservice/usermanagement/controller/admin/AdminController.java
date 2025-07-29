package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller.admin;

import com.ferticare.ferticareback.common.exception.AppException;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.AdminOnly;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DepartmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.DoctorWorkScheduleService;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.request.ScheduleCreateRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.UserCreateByAdminRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.UserResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.AdminDashboardDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.UserStatsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.AdminDoctorDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.SystemSettingsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto.DoctorStatsDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorCreateRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin APIs for system management")
public class AdminController {

    private final UserService userService;
    private final DoctorWorkScheduleService doctorWorkScheduleService;
    private final DepartmentRepository departmentRepository;

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/dashboard")
    @Operation(summary = "Get admin dashboard data")
    public ResponseEntity<AdminDashboardDTO> getDashboardData() {
        try {
            AdminDashboardDTO dashboardData = userService.getAdminDashboardData();
            return ResponseEntity.ok(dashboardData);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/users")
    @Operation(summary = "Get all users with pagination and filters")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        try {
            Page<UserResponse> users = userService.getAllUsersForAdmin(pageable, search, role, status);
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/users/stats")
    @Operation(summary = "Get user statistics")
    public ResponseEntity<UserStatsDTO> getUserStats() {
        try {
            UserStatsDTO stats = userService.getUserStats();
            return ResponseEntity.ok(stats);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/users")
    @Operation(summary = "Create new user by admin")
    public ResponseEntity<?> createUserByAdmin(@Valid @RequestBody UserCreateByAdminRequest request) {
        try {
            UserResponse response = userService.createUserByAdmin(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Tạo user thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/users/{id}")
    @Operation(summary = "Update user by admin")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @Valid @RequestBody UserCreateByAdminRequest request) {
        try {
            UserResponse response = userService.updateUserByAdmin(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Cập nhật user thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user by admin")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUserByAdmin(id);
            return ResponseEntity.ok("Xóa user thành công");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Xóa user thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/users/{id}/status")
    @Operation(summary = "Toggle user status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable UUID id) {
        try {
            UserResponse response = userService.toggleUserStatus(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Cập nhật trạng thái thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/system/health")
    @Operation(summary = "Get system health status")
    public ResponseEntity<?> getSystemHealth() {
        return ResponseEntity.ok().body(java.util.Map.of(
            "status", "healthy",
            "uptime", "99.5%",
            "lastBackup", "2024-01-15T10:30:00Z"
        ));
    }

    // Legacy endpoint for compatibility
    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<?> onlyAdminAccess() {
        return ResponseEntity.ok("Welcome, Admin!");
    }

    // Legacy endpoint for compatibility  
    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("")
    public ResponseEntity<?> createUserByAdminLegacy(@Valid @RequestBody UserCreateByAdminRequest request) {
        return createUserByAdmin(request);
    }

    // ===================== DOCTOR MANAGEMENT ENDPOINTS =====================

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/doctors")
    @Operation(summary = "Get all doctors with pagination and filters")
    public ResponseEntity<Page<AdminDoctorDTO>> getAllDoctors(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status) {
        try {
            Page<AdminDoctorDTO> doctors = userService.getAllDoctorsForAdmin(pageable, search, department, status);
            return ResponseEntity.ok(doctors);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/doctors/stats")
    @Operation(summary = "Get doctor statistics (without revenue)")
    public ResponseEntity<DoctorStatsDTO> getDoctorStats() {
        try {
            DoctorStatsDTO stats = userService.getDoctorStats();
            return ResponseEntity.ok(stats);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/doctors/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<AdminDoctorDTO> getDoctorById(@PathVariable UUID id) {
        try {
            AdminDoctorDTO doctor = userService.getDoctorById(id);
            return ResponseEntity.ok(doctor);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/doctors")
    @Operation(summary = "Create new doctor")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorCreateRequest request) {
        try {
            AdminDoctorDTO doctor = userService.createDoctorByAdmin(request);
            return ResponseEntity.ok(doctor);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Tạo bác sĩ thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/doctors/{id}")
    @Operation(summary = "Update doctor by ID")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @Valid @RequestBody DoctorCreateRequest request) {
        try {
            AdminDoctorDTO doctor = userService.updateDoctorByAdmin(id, request);
            return ResponseEntity.ok(doctor);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Cập nhật bác sĩ thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/doctors/{id}")
    @Operation(summary = "Delete doctor by ID")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {
        try {
            userService.deleteDoctorByAdmin(id);
            return ResponseEntity.ok("Xóa bác sĩ thành công");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Xóa bác sĩ thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/doctors/{id}/status")
    @Operation(summary = "Toggle doctor status")
    public ResponseEntity<?> toggleDoctorStatus(@PathVariable UUID id) {
        try {
            AdminDoctorDTO doctor = userService.toggleDoctorStatus(id);
            return ResponseEntity.ok(doctor);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Cập nhật trạng thái thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/departments/{departmentId}/doctors")
    @Operation(summary = "Get all doctors by department ID")
    public ResponseEntity<?> getDoctorsByDepartment(
            @PathVariable UUID departmentId,
            Pageable pageable) {
        try {
            Page<AdminDoctorDTO> doctors = userService.getDoctorsByDepartment(departmentId, pageable);
            return ResponseEntity.ok(doctors);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Lỗi khi lấy danh sách bác sĩ: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/departments/{departmentId}/schedules")
    @Operation(summary = "Get all schedules by department ID")
    public ResponseEntity<?> getSchedulesByDepartment(@PathVariable Long departmentId, Pageable pageable) {
        try {
            Page<DoctorScheduleDTO> schedules = doctorWorkScheduleService.getSchedulesByDepartment(departmentId, pageable);
            return ResponseEntity.ok(schedules);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Lỗi khi lấy lịch làm việc: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/settings")
    @Operation(summary = "Get system settings")
    public ResponseEntity<SystemSettingsDTO> getSystemSettings() {
        try {
            SystemSettingsDTO settings = userService.getSystemSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/settings")
    @Operation(summary = "Update system settings")
    public ResponseEntity<?> updateSystemSettings(@Valid @RequestBody SystemSettingsDTO settings) {
        try {
            SystemSettingsDTO updatedSettings = userService.updateSystemSettings(settings);
            return ResponseEntity.ok(updatedSettings);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Cập nhật cài đặt thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/settings/backup")
    @Operation(summary = "Perform system backup")
    public ResponseEntity<?> performBackup() {
        try {
            String result = userService.performBackup();
            return ResponseEntity.ok().body(java.util.Map.of("message", result));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Sao lưu thất bại: " + ex.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/settings/maintenance")
    @Operation(summary = "Perform system maintenance")
    public ResponseEntity<?> performMaintenance() {
        try {
            String result = userService.performMaintenance();
            return ResponseEntity.ok().body(java.util.Map.of("message", result));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Bảo trì thất bại: " + ex.getMessage());
        }
    }

    // ===================== DOCTOR SCHEDULE MANAGEMENT =====================

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/doctor-schedules")
    @Operation(summary = "Lấy tất cả lịch làm việc của bác sĩ")
    public ResponseEntity<?> getAllSchedules(
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) Long departmentId,
            Pageable pageable) {
        try {
            if (doctorId != null) {
                List<DoctorScheduleDTO> schedules = doctorWorkScheduleService.getDoctorSchedule(doctorId);
                return ResponseEntity.ok(schedules);
            } else if (departmentId != null) {
                Page<DoctorScheduleDTO> schedules = doctorWorkScheduleService.getSchedulesByDepartment(departmentId, pageable);
                return ResponseEntity.ok(schedules);
            } else {
                return ResponseEntity.badRequest().body("Cần cung cấp doctorId hoặc departmentId");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi lấy lịch làm việc: " + e.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/doctor-schedules/{id}")
    @Operation(summary = "Lấy chi tiết lịch làm việc theo ID")
    public ResponseEntity<?> getScheduleById(@PathVariable String id) {
        try {
            DoctorScheduleDTO schedule = doctorWorkScheduleService.getScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (AppException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi lấy lịch làm việc: " + e.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/doctor-schedules")
    @Operation(summary = "Tạo lịch làm việc mới cho bác sĩ")
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleCreateRequest request) {
        try {
            // Chuyển đổi từ request sang entity
            DoctorWorkSchedule schedule = DoctorWorkSchedule.builder()
                    .doctorId(request.getDoctorId())
                    .dayOfWeek(request.getDayOfWeek())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .room(request.getRoom())
                    .effectiveFrom(request.getEffectiveFrom())
                    .effectiveTo(request.getEffectiveTo())
                    .maxAppointments(request.getMaxAppointments())
                    .note(request.getNote())
                    .status(request.getStatus())
                    .build();
            
            // Thêm department nếu có
            if (request.getDepartmentId() != null) {
                Department department = departmentRepository.findById(Long.parseLong(request.getDepartmentId().toString()))
                        .orElseThrow(() -> new AppException("DEPARTMENT_NOT_FOUND", "Không tìm thấy phòng ban"));
                schedule.setDepartment(department);
            }
            
            DoctorScheduleDTO createdSchedule = doctorWorkScheduleService.createSchedule(schedule);
            return ResponseEntity.status(201).body(createdSchedule);
        } catch (AppException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi tạo lịch làm việc: " + e.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/doctor-schedules/{id}")
    @Operation(summary = "Cập nhật lịch làm việc")
    public ResponseEntity<?> updateSchedule(@PathVariable String id, @Valid @RequestBody DoctorWorkSchedule schedule) {
        try {
            DoctorScheduleDTO updatedSchedule = doctorWorkScheduleService.updateSchedule(id, schedule);
            return ResponseEntity.ok(updatedSchedule);
        } catch (AppException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi cập nhật lịch làm việc: " + e.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/doctor-schedules/{id}")
    @Operation(summary = "Xóa lịch làm việc")
    public ResponseEntity<?> deleteSchedule(@PathVariable String id) {
        try {
            doctorWorkScheduleService.deleteSchedule(id);
            return ResponseEntity.ok("Xóa lịch làm việc thành công");
        } catch (AppException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi xóa lịch làm việc: " + e.getMessage());
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/doctor-schedules/{id}/approve")
    @Operation(summary = "Phê duyệt lịch làm việc")
    public ResponseEntity<?> approveSchedule(@PathVariable String id) {
        try {
            DoctorScheduleDTO approvedSchedule = doctorWorkScheduleService.approveSchedule(id);
            return ResponseEntity.ok(approvedSchedule);
        } catch (AppException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi phê duyệt lịch làm việc: " + e.getMessage());
        }
    }
}