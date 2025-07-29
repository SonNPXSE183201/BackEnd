package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.AppointmentService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.ClinicalResultRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service.ClinicalResultService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClinicalResultService clinicalResultService;

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
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Tự động tạo ClinicalResult rỗng gắn với appointmentId, patientId, doctorId
        ClinicalResultRequest clinicalResultRequest = new ClinicalResultRequest();
        clinicalResultRequest.setAppointmentId(savedAppointment.getAppointmentId());
        clinicalResultRequest.setPatientId(savedAppointment.getCustomerId());
        clinicalResultRequest.setDoctorId(savedAppointment.getDoctorId());
        clinicalResultService.createClinicalResultWithDoctor(clinicalResultRequest, savedAppointment.getDoctorId().toString());

        return savedAppointment;
    }
} 