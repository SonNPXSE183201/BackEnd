package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;

import java.util.UUID;

public interface AppointmentService {
    Appointment createAppointment(ServiceRequestDTO dto, User doctor, UUID customerId);
}