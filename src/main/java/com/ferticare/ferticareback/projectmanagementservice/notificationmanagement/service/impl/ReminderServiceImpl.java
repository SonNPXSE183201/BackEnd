package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity.ReminderLog;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository.ReminderLogRepository;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.ReminderService;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.ServiceRequest;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRequestRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderServiceImpl implements ReminderService {

    @Autowired
    private ReminderLogRepository reminderLogRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 3000) // Chạy mỗi 5 phút (300000ms)
    public void processReminders() {
        System.out.println("🔄 Đang kiểm tra reminders...");
        System.out.println("⏰ Thời gian hiện tại: " + LocalDateTime.now());
        
        List<ReminderLog> reminders = reminderLogRepository
            .findByStatusAndChannelAndReminderTimeBefore("PENDING", "EMAIL", LocalDateTime.now());

        System.out.println("📋 Tìm thấy " + reminders.size() + " reminders cần xử lý");

        for (ReminderLog reminder : reminders) {
            System.out.println("🔍 Kiểm tra reminder: " + reminder.getReminderId());
            System.out.println("   - Reminder time: " + reminder.getReminderTime());
            System.out.println("   - Current time: " + LocalDateTime.now());
            
            try {
                // Kiểm tra xem có nên gửi reminder không
                if (shouldSendReminder(reminder)) {
                    System.out.println("✅ Điều kiện gửi email thỏa mãn");
                    // Gửi email nhắc nhở
                    sendReminderEmail(reminder);
                    reminder.setStatus("SENT");
                    System.out.println("✅ Đã gửi reminder cho appointment: " + reminder.getAppointmentId());
                } else {
                    // Chưa đến thời điểm gửi, giữ nguyên status PENDING
                    System.out.println("⏳ Chưa đến thời điểm gửi reminder cho appointment: " + reminder.getAppointmentId());
                }
            } catch (Exception e) {
                reminder.setStatus("FAILED");
                System.err.println("❌ Lỗi gửi reminder: " + e.getMessage());
                e.printStackTrace();
            }
            reminderLogRepository.save(reminder);
        }
    }

    private boolean shouldSendReminder(ReminderLog reminder) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = reminder.getReminderTime();
        
        // Kiểm tra xem đã đến thời điểm gửi reminder chưa
        // Cho phép sai số 5 phút (trước hoặc sau thời điểm dự kiến)
        long minutesDiff = java.time.Duration.between(reminderTime, now).toMinutes();
        
        // Gửi reminder nếu đã đến thời điểm (có thể sớm hoặc muộn 5 phút)
        return Math.abs(minutesDiff) <= 5;
    }

    private void sendReminderEmail(ReminderLog reminder) {
        try {
            // Lấy thông tin appointment
            Appointment appointment = appointmentRepository.findById(reminder.getAppointmentId()).orElse(null);
            if (appointment == null) {
                System.err.println("❌ Không tìm thấy appointment: " + reminder.getAppointmentId());
                return;
            }

            // Lấy thông tin khách hàng
            User customer = userRepository.findById(appointment.getCustomerId()).orElse(null);
            if (customer == null) {
                System.err.println("❌ Không tìm thấy customer: " + appointment.getCustomerId());
                return;
            }

            // Lấy thông tin bác sĩ
            User doctor = userRepository.findById(appointment.getDoctorId()).orElse(null);
            if (doctor == null) {
                System.err.println("❌ Không tìm thấy doctor: " + appointment.getDoctorId());
                return;
            }

            // Lấy thông tin dịch vụ
            String serviceName = "Dịch vụ khám";
            ServiceRequest request = null;
            if (appointment.getRequestId() != null) {
                request = serviceRequestRepository.findById(appointment.getRequestId()).orElse(null);
                if (request != null && request.getServiceId() != null) {
                    com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Service service = 
                        serviceRepository.findById(request.getServiceId()).orElse(null);
                    if (service != null) {
                        serviceName = service.getName();
                    }
                }
            }

            // Định dạng ngày giờ
            String appointmentDate = appointment.getAppointmentTime() != null ? 
                appointment.getAppointmentTime().toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            String appointmentTime = appointment.getAppointmentTime() != null ? 
                appointment.getAppointmentTime().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "";

            // Tính thời gian còn lại
            LocalDateTime now = LocalDateTime.now();
            long hoursDiff = java.time.Duration.between(now, appointment.getAppointmentTime()).toHours();
            String timeRemaining = hoursDiff == 24 ? "24 giờ" : "2 giờ";

            // Tạo notes và các trường riêng biệt
            String notes = request != null ? request.getNote() : "";
            String room = appointment.getRoom();
            String reminderMessage = "⏰ Nhắc nhở: Lịch hẹn của bạn sẽ diễn ra sau " + timeRemaining;

            // Gửi email nhắc nhở
            emailService.sendAppointmentConfirmationEmail(
                customer,
                doctor,
                serviceName,
                appointmentDate,
                appointmentTime,
                room,
                notes,
                reminderMessage
            );

            System.out.println("📧 Đã gửi email nhắc nhở cho: " + customer.getEmail());
            System.out.println("   - Thời gian còn lại: " + timeRemaining);
            System.out.println("   - Ngày hẹn: " + appointmentDate);
            System.out.println("   - Giờ hẹn: " + appointmentTime);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email nhắc nhở: " + e.getMessage());
            throw e;
        }
    }
} 