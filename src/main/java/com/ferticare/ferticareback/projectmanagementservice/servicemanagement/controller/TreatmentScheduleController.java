package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.TreatmentScheduleRequest;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ScheduleStatusUpdateRequest;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.TreatmentSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.TreatmentScheduleRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DoctorWorkScheduleRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.common.exception.AppException;
import com.ferticare.ferticareback.common.dto.GenericResponse;
import com.ferticare.ferticareback.common.dto.MessageDTO;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/treatment-schedule")
@RequiredArgsConstructor
@Slf4j
public class TreatmentScheduleController {
    private final TreatmentScheduleRepository treatmentScheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createTreatmentSchedule(@RequestBody TreatmentScheduleRequest request) {
        // ✅ Kiểm tra conflict trước khi tạo schedule
        ResponseEntity<?> conflictCheck = checkScheduleConflict(request);
        if (conflictCheck != null) {
            return conflictCheck;
        }
        
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
    
    /**
     * ✅ Kiểm tra conflict lịch hẹn
     */
    private ResponseEntity<?> checkScheduleConflict(TreatmentScheduleRequest request) {
        try {
            LocalDateTime scheduledDate = request.getScheduledDate();
            UUID doctorId = request.getDoctorId();
            
            // 1. Kiểm tra xung đột với appointment thông thường
            boolean appointmentConflict = appointmentRepository.existsByDoctorIdAndAppointmentTime(doctorId, scheduledDate);
            if (appointmentConflict) {
                log.warn("Appointment conflict detected for doctor {} at {}", doctorId, scheduledDate);
                return ResponseEntity.badRequest().body("Bác sĩ đã có lịch hẹn vào thời gian này");
            }
            
            // 2. Kiểm tra xung đột với treatment schedule khác - BỎ QUA
            // Không kiểm tra treatment_schedule để tránh conflict
            
            // 3. Kiểm tra xem bác sĩ có lịch làm việc vào ngày này không (không cần kiểm tra giờ cụ thể)
            int dayOfWeek = scheduledDate.getDayOfWeek().getValue();
            List<DoctorWorkSchedule> workSchedules = doctorWorkScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
            
            boolean hasWorkSchedule = workSchedules.stream()
                .anyMatch(schedule -> "active".equalsIgnoreCase(schedule.getStatus()) || "pending".equalsIgnoreCase(schedule.getStatus()));
            
            if (!hasWorkSchedule) {
                log.warn("No active work schedule found for doctor {} on day {}", doctorId, dayOfWeek);
                return ResponseEntity.badRequest().body("Bác sĩ không có lịch làm việc vào ngày này");
            }
            
            log.info("No conflict detected for doctor {} at {}", doctorId, scheduledDate);
            return null; // Không có conflict
            
        } catch (Exception e) {
            log.error("Error checking schedule conflict: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Lỗi kiểm tra lịch hẹn: " + e.getMessage());
        }
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

    // 🆕 GET API: Lấy tất cả lịch hẹn của bệnh nhân
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Lấy tất cả lịch hẹn treatment schedule của bệnh nhân", 
               description = "Bệnh nhân và bác sĩ điều trị có thể xem lịch hẹn")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getTreatmentSchedulesByPatient(@PathVariable UUID patientId, 
                                                           HttpServletRequest request) {
        try {
            log.info("Getting treatment schedules for patient: {}", patientId);
            
            // Lấy thông tin user từ JWT để kiểm tra quyền
            String jwt = jwtUtil.extractJwtFromRequest(request);
            String currentUserId = jwtUtil.extractUserId(jwt);
            String userRole = jwtUtil.extractRole(jwt);
            
            // Kiểm tra quyền: chỉ bệnh nhân đó hoặc bác sĩ điều trị mới được xem
            if (!canAccessPatientSchedules(currentUserId, userRole, patientId)) {
                log.warn("User {} with role {} not authorized to access patient {} schedules", 
                        currentUserId, userRole, patientId);
                return ResponseEntity.status(403).body(
                    new GenericResponse<>(false, 
                        new MessageDTO("FORBIDDEN", "Bạn không có quyền xem lịch hẹn của bệnh nhân này"), 
                        null, null));
            }
            
            // Lấy tất cả lịch hẹn của bệnh nhân, sắp xếp theo ngày giờ giảm dần
            List<TreatmentSchedule> schedules = treatmentScheduleRepository
                .findByPatientIdOrderByScheduledDateDesc(patientId);
            
            log.info("Found {} treatment schedules for patient: {}", schedules.size(), patientId);
            
            return ResponseEntity.ok(new GenericResponse<>(true, 
                new MessageDTO("SUCCESS", "Lấy danh sách lịch hẹn thành công"), 
                null, schedules));
                
        } catch (Exception e) {
            log.error("Error getting treatment schedules for patient {}: {}", patientId, e.getMessage());
            return ResponseEntity.internalServerError().body(
                new GenericResponse<>(false, 
                    new MessageDTO("ERROR", "Lỗi khi lấy danh sách lịch hẹn: " + e.getMessage()), 
                    null, null));
        }
    }

    // 🆕 PUT API: Cập nhật trạng thái lịch hẹn (chỉ bác sĩ)
    @PutMapping("/{scheduleId}/status")
    @DoctorOnly
    @Operation(summary = "Cập nhật trạng thái lịch hẹn treatment schedule", 
               description = "Chỉ bác sĩ điều trị mới được cập nhật trạng thái")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateScheduleStatus(@PathVariable UUID scheduleId,
                                                 @Valid @RequestBody ScheduleStatusUpdateRequest request,
                                                 HttpServletRequest httpRequest) {
        try {
            log.info("Updating schedule status: {} to {}", scheduleId, request.getStatus());
            
            // Lấy thông tin user từ JWT
            String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
            String doctorId = jwtUtil.extractUserId(jwt);
            
            // Tìm lịch hẹn
            TreatmentSchedule schedule = treatmentScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException("SCHEDULE_NOT_FOUND", "Không tìm thấy lịch hẹn"));
            
            // Kiểm tra quyền: chỉ bác sĩ điều trị mới được cập nhật
            if (!schedule.getDoctorId().toString().equals(doctorId)) {
                log.warn("Doctor {} not authorized to update schedule {} (belongs to doctor {})", 
                        doctorId, scheduleId, schedule.getDoctorId());
                return ResponseEntity.status(403).body(
                    new GenericResponse<>(false, 
                        new MessageDTO("FORBIDDEN", "Bạn không có quyền cập nhật lịch hẹn này"), 
                        null, null));
            }
            
            // Cập nhật trạng thái
            String oldStatus = schedule.getStatus();
            schedule.setStatus(request.getStatus());
            
            // Cập nhật thời gian hoàn thành nếu status = 'completed'
            if ("completed".equals(request.getStatus())) {
                schedule.setCompletedAt(request.getCompletedAt() != null ? 
                    request.getCompletedAt() : LocalDateTime.now());
            }
            
            // Cập nhật ghi chú nếu có
            if (request.getNotes() != null && !request.getNotes().trim().isEmpty()) {
                schedule.setNotes(request.getNotes());
            }
            
            // Lưu thay đổi
            TreatmentSchedule updatedSchedule = treatmentScheduleRepository.save(schedule);
            
            log.info("Schedule {} status updated from {} to {} by doctor {}", 
                    scheduleId, oldStatus, request.getStatus(), doctorId);
            
            return ResponseEntity.ok(new GenericResponse<>(true, 
                new MessageDTO("SUCCESS", "Cập nhật trạng thái lịch hẹn thành công"), 
                null, updatedSchedule));
                
        } catch (AppException e) {
            log.error("AppException updating schedule status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                new GenericResponse<>(false, 
                    new MessageDTO("ERROR", e.getMessage()), 
                    null, null));
        } catch (Exception e) {
            log.error("Error updating schedule status: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(
                new GenericResponse<>(false, 
                    new MessageDTO("ERROR", "Lỗi khi cập nhật trạng thái: " + e.getMessage()), 
                    null, null));
        }
    }

    // 🆕 Helper method: Kiểm tra quyền truy cập lịch hẹn của bệnh nhân
    private boolean canAccessPatientSchedules(String currentUserId, String userRole, UUID patientId) {
        // Bệnh nhân có thể xem lịch hẹn của chính mình
        if ("PATIENT".equals(userRole) || "CUSTOMER".equals(userRole)) {
            return currentUserId.equals(patientId.toString());
        }
        
        // Bác sĩ có thể xem lịch hẹn của bệnh nhân mà mình điều trị
        if ("DOCTOR".equals(userRole)) {
            // Kiểm tra xem bác sĩ có điều trị bệnh nhân này không
            List<TreatmentSchedule> doctorSchedules = treatmentScheduleRepository
                .findByDoctorIdOrderByScheduledDateDesc(UUID.fromString(currentUserId));
            
            return doctorSchedules.stream()
                .anyMatch(schedule -> schedule.getPatientId().equals(patientId));
        }
        
        // Admin có thể xem tất cả
        if ("ADMIN".equals(userRole)) {
            return true;
        }
        
        return false;
    }
} 