package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.AppointmentService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public Appointment createAppointment(ServiceRequestDTO dto, User doctor, UUID customerId) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(UUID.randomUUID());
        appointment.setDoctorId(doctor.getId());
        appointment.setCustomerId(customerId);
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setCheckInStatus("Pending"); // ✅ Field hợp lệ
        appointment.setRoom("Phòng A1"); // hoặc để null nếu chưa xác định
        return appointmentRepository.save(appointment);
    }
} 