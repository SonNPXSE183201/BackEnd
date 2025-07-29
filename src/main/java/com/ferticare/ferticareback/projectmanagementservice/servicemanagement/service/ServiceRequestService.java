package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

public interface ServiceRequestService {
    
    ResponseEntity<?> handleRequest(UUID userId, ServiceRequestDTO dto);
    
    ResponseEntity<?> getDoctorSchedule(UUID doctorId);
    
    ResponseEntity<?> getAvailableDoctorsByService(UUID serviceId);
    
    ResponseEntity<?> getDoctorAvailableTimes(UUID doctorId, LocalDate date);
    
    ResponseEntity<?> handleRequestWithAuth(ServiceRequestDTO dto, HttpServletRequest request);
    
    ResponseEntity<?> getAvailableDates(UUID doctorId);
    
    ResponseEntity<?> getDoctorAppointments(UUID doctorId, LocalDate date);
    
    // Doctor's patients management
    ResponseEntity<?> getDoctorPatients(UUID doctorId);
    
    // 🆕 Customer appointment limit management
    ResponseEntity<?> getCustomerAppointmentLimit(UUID customerId);
} 