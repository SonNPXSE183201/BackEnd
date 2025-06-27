package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;

public interface EmailService {
    void sendVerificationEmail(User user, String token);
    void sendPasswordResetEmail(User user, String token);
    void sendAppointmentConfirmationEmail(User customer, User doctor, String serviceName, String appointmentDate, String appointmentTime, String room, String notes, String reminderMessage);
}
