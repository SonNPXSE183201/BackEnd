package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.TreatmentScheduleRequest;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.TreatmentSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.TreatmentScheduleRepository;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/treatment-schedule")
@RequiredArgsConstructor
@Slf4j
public class TreatmentScheduleController {
    private final TreatmentScheduleRepository treatmentScheduleRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createTreatmentSchedule(@RequestBody TreatmentScheduleRequest request) {
        TreatmentSchedule schedule = new TreatmentSchedule();
        schedule.setPlanId(request.getPlanId());
        schedule.setStepNumber(request.getStepNumber());
        schedule.setStepName(request.getStepName());
        schedule.setDoctorId(request.getDoctorId());
        schedule.setPatientId(request.getPatientId());
        schedule.setScheduledDate(request.getScheduledDate());
        schedule.setRoomId(request.getRoomId());
        schedule.setNotes(request.getNotes());
        schedule.setTreatmentType(request.getTreatmentType());
        schedule.setDeadline(request.getDeadline());
        schedule.setGracePeriodDays(request.getGracePeriodDays());
        schedule.setStatus("scheduled");
        // BaseEntity sẽ tự động xử lý createdAt, updatedAt, createdBy, updatedBy
        
        TreatmentSchedule saved = treatmentScheduleRepository.save(schedule);
        
        // Gửi email thông báo lịch hẹn cho phase
        try {
            sendScheduleNotificationEmail(saved);
            log.info("Email notification sent for treatment schedule: {}", saved.getScheduleId());
        } catch (Exception e) {
            log.error("Error sending email notification for schedule: {}", e.getMessage());
            // Không throw exception để không ảnh hưởng đến việc tạo schedule
        }
        
        return ResponseEntity.ok(saved);
    }
    
    private void sendScheduleNotificationEmail(TreatmentSchedule schedule) {
        try {
            // Lấy thông tin bệnh nhân và bác sĩ
            User patient = userRepository.findById(schedule.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found: " + schedule.getPatientId()));
            
            User doctor = userRepository.findById(schedule.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found: " + schedule.getDoctorId()));
            
            // Gửi email thông báo lịch hẹn
            emailService.sendScheduleNotification(patient, doctor, schedule);
            
        } catch (Exception e) {
            log.error("Error in sendScheduleNotificationEmail: {}", e.getMessage());
            throw e;
        }
    }
} 