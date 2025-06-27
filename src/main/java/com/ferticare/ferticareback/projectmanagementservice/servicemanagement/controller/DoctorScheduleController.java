package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.ServiceRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/doctor-schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final ServiceRequestService serviceRequestService;
    private final JwtUtil jwtUtil;

    /**
     * Lấy lịch làm việc của bác sĩ hiện tại (tự động lấy từ JWT)
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
            return ResponseEntity.status(400).body("Lỗi xác thực: " + e.getMessage());
        }
    }

    /**
     * Lấy lịch hẹn của bác sĩ hiện tại theo ngày (tự động lấy từ JWT)
     */
    @DoctorOnly
    @GetMapping("/my-appointments")
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
} 