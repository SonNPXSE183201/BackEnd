package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.TreatmentSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.TreatmentScheduleRepository;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentScheduleReminderService {
    private final TreatmentScheduleRepository treatmentScheduleRepository;
    private final EmailService emailService;

    // Gửi nhắc nhở 24h và 2h trước lịch hẹn
    @Scheduled(fixedDelay = 600000) // 10 phút
    public void sendRemindersForUpcomingSchedules() {
        LocalDateTime now = LocalDateTime.now();
        
        // Nhắc nhở 24h trước (trong khoảng 24h-23h30)
        LocalDateTime start24h = now.plusHours(23).plusMinutes(30);
        LocalDateTime end24h = now.plusHours(24);
        List<TreatmentSchedule> schedules24h = treatmentScheduleRepository.findSchedulesFor24HourReminder(start24h, end24h);
        for (TreatmentSchedule schedule : schedules24h) {
            emailService.sendAppointmentReminder(schedule, 24);
        }
        
        // Nhắc nhở 2h trước (trong khoảng 2h-1h50)
        LocalDateTime start2h = now.plusHours(1).plusMinutes(50);
        LocalDateTime end2h = now.plusHours(2);
        List<TreatmentSchedule> schedules2h = treatmentScheduleRepository.findSchedulesFor2HourReminder(start2h, end2h);
        for (TreatmentSchedule schedule : schedules2h) {
            emailService.sendAppointmentReminder(schedule, 2);
        }
    }

    // Gửi cảnh báo quá hạn 30 phút
    @Scheduled(fixedDelay = 600000) // 10 phút
    public void sendOverdueWarnings() {
        LocalDateTime now = LocalDateTime.now();
        
        // Cảnh báo quá hạn 30-40 phút
        LocalDateTime startOverdue = now.minusMinutes(40);
        LocalDateTime endOverdue = now.minusMinutes(30);
        List<TreatmentSchedule> overdueSchedules = treatmentScheduleRepository.findOverdueSchedules(startOverdue, endOverdue);
        
        for (TreatmentSchedule schedule : overdueSchedules) {
            emailService.sendOverdueWarning(schedule);
        }
    }

    // Hủy plan nếu quá hạn 1 ngày
    @Scheduled(fixedDelay = 600000) // 10 phút
    public void cancelOverduePlans() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffTime = now.minusDays(1);
        
        List<TreatmentSchedule> schedulesToCancel = treatmentScheduleRepository.findSchedulesToCancel(cutoffTime);
        
        for (TreatmentSchedule schedule : schedulesToCancel) {
            // Hủy plan
            schedule.setStatus("cancelled");
            // BaseEntity sẽ tự động xử lý updatedAt khi save
            treatmentScheduleRepository.save(schedule);
            
            // Gửi email thông báo hủy
            emailService.sendTreatmentCancelled(schedule);
        }
    }
} 