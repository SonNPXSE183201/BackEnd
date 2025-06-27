package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto.AppointmentEmailDTO;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.AppointmentEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications/appointment")
@RequiredArgsConstructor
public class AppointmentEmailController {

    private final AppointmentEmailService appointmentEmailService;

    @PostMapping("/send-confirmation")
    public ResponseEntity<?> sendAppointmentConfirmation(@RequestBody AppointmentEmailDTO emailDTO) {
        try {
            appointmentEmailService.sendAppointmentConfirmation(emailDTO);
            return ResponseEntity.ok(Map.of("message", "Email xác nhận lịch hẹn đã được gửi thành công!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Lỗi khi gửi email: " + e.getMessage()));
        }
    }

    @PostMapping("/send-reminder")
    public ResponseEntity<?> sendAppointmentReminder(@RequestBody AppointmentEmailDTO emailDTO) {
        try {
            appointmentEmailService.sendAppointmentReminder(emailDTO);
            return ResponseEntity.ok(Map.of("message", "Email nhắc nhở lịch hẹn đã được gửi thành công!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Lỗi khi gửi email: " + e.getMessage()));
        }
    }

    @PostMapping("/send-cancellation")
    public ResponseEntity<?> sendAppointmentCancellation(@RequestBody AppointmentEmailDTO emailDTO) {
        try {
            appointmentEmailService.sendAppointmentCancellation(emailDTO);
            return ResponseEntity.ok(Map.of("message", "Email hủy lịch hẹn đã được gửi thành công!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Lỗi khi gửi email: " + e.getMessage()));
        }
    }
} 