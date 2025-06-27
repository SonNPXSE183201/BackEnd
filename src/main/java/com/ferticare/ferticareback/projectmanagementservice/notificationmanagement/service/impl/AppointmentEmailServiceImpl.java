package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto.AppointmentEmailDTO;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.AppointmentEmailService;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentEmailServiceImpl implements AppointmentEmailService {

    private final EmailService emailService;

    @Override
    public void sendAppointmentConfirmation(AppointmentEmailDTO appointmentEmailDTO) {
        try {
            User customer = new User();
            customer.setFullName(appointmentEmailDTO.getCustomerName());
            customer.setEmail(appointmentEmailDTO.getCustomerEmail());

            User doctor = new User();
            doctor.setFullName(appointmentEmailDTO.getDoctorName());

            String notes = appointmentEmailDTO.getNotes();
            String room = appointmentEmailDTO.getRoom();
            String reminderMessage = appointmentEmailDTO.getStatus() != null && appointmentEmailDTO.getStatus().contains("Nhắc nhở:") ? appointmentEmailDTO.getStatus() : "";

            emailService.sendAppointmentConfirmationEmail(
                customer,
                doctor,
                appointmentEmailDTO.getServiceName(),
                appointmentEmailDTO.getAppointmentDate(),
                appointmentEmailDTO.getAppointmentTime(),
                room,
                notes,
                reminderMessage
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email xác nhận lịch hẹn: " + e.getMessage());
        }
    }

    @Override
    public void sendAppointmentReminder(AppointmentEmailDTO appointmentEmailDTO) {
        try {
            User customer = new User();
            customer.setFullName(appointmentEmailDTO.getCustomerName());
            customer.setEmail(appointmentEmailDTO.getCustomerEmail());

            User doctor = new User();
            doctor.setFullName(appointmentEmailDTO.getDoctorName());

            String notes = appointmentEmailDTO.getNotes();
            String room = appointmentEmailDTO.getRoom();
            String reminderMessage = appointmentEmailDTO.getStatus() != null && appointmentEmailDTO.getStatus().contains("Nhắc nhở:") ? appointmentEmailDTO.getStatus() : "";

            emailService.sendAppointmentConfirmationEmail(
                customer,
                doctor,
                appointmentEmailDTO.getServiceName(),
                appointmentEmailDTO.getAppointmentDate(),
                appointmentEmailDTO.getAppointmentTime(),
                room,
                notes,
                reminderMessage
            );
            System.out.println("✅ Email nhắc nhở đã gửi thành công cho: " + appointmentEmailDTO.getCustomerEmail());
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email nhắc nhở: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void sendAppointmentCancellation(AppointmentEmailDTO appointmentEmailDTO) {
        // TODO: Implement cancellation email
        System.out.println("📧 Gửi email hủy lịch hẹn cho: " + appointmentEmailDTO.getCustomerEmail());
    }
} 