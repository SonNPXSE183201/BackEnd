package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.ServiceRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/doctor/schedule")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final ServiceRequestService serviceRequestService;
    private final JwtUtil jwtUtil;

    /**
     * Lấy lịch làm việc của bác sĩ hiện tại
     */
    @DoctorOnly
    @GetMapping("/my-schedule")
    public ResponseEntity<?> getMySchedule(HttpServletRequest request) {
        try {
            // Lấy thông tin user từ JWT
            String jwt = jwtUtil.extractJwtFromRequest(request);
            String doctorId = jwtUtil.extractUserId(jwt);
            
            return serviceRequestService.getDoctorSchedule(UUID.fromString(doctorId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy lịch hẹn của bác sĩ theo ngày
     */
    @DoctorOnly
    @GetMapping("/my-appointments")
    @Operation(summary = "Lấy lịch hẹn của bác sĩ theo ngày", description = "Bác sĩ xem lịch hẹn của mình trong ngày cụ thể")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getMyAppointments(
            @RequestParam(required = false) String date,
            HttpServletRequest request) {
        try {
            // Lấy thông tin user từ JWT
            String jwt = jwtUtil.extractJwtFromRequest(request);
            String doctorId = jwtUtil.extractUserId(jwt);
            
            java.time.LocalDate localDate = date != null ? 
                java.time.LocalDate.parse(date) : java.time.LocalDate.now();
            return serviceRequestService.getDoctorAppointments(UUID.fromString(doctorId), localDate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách bệnh nhân của bác sĩ
     */
    @DoctorOnly
    @GetMapping("/my-patients")
    @Operation(summary = "Lấy danh sách bệnh nhân của bác sĩ", description = "Bác sĩ xem tất cả bệnh nhân đã từng khám với mình")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getMyPatients(HttpServletRequest request) {
        try {
            // Lấy thông tin user từ JWT
            String jwt = jwtUtil.extractJwtFromRequest(request);
            String doctorId = jwtUtil.extractUserId(jwt);
            
            return serviceRequestService.getDoctorPatients(UUID.fromString(doctorId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
} 