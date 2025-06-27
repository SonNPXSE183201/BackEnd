package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto.AppointmentEmailDTO;

public interface AppointmentEmailService {
    void sendAppointmentConfirmation(AppointmentEmailDTO appointmentEmailDTO);
    void sendAppointmentReminder(AppointmentEmailDTO appointmentEmailDTO);
    void sendAppointmentCancellation(AppointmentEmailDTO appointmentEmailDTO);
} 