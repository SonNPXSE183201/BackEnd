package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.common.dto.GenericResponse;
import com.ferticare.ferticareback.common.dto.MessageDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @GetMapping("/patient/{patientId}/appointments")
    @Operation(summary = "Get appointments for a specific patient", description = "Retrieve all appointments for a patient by their ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getPatientAppointments(@PathVariable UUID patientId) {
        try {
            // Kiểm tra patient có tồn tại không
            User patient = userRepository.findById(patientId)
                    .orElse(null);
            
            if (patient == null) {
                return ResponseEntity.badRequest().body(GenericResponse.builder()
                        .isSuccess(false)
                        .message(MessageDTO.builder().messageDetail("Patient not found").build())
                        .build());
            }

            // Lấy danh sách appointments của patient
            List<Appointment> appointments = appointmentRepository.findByCustomerId(patientId);
            
            // Convert appointments thành DTO với thông tin chi tiết
            List<Map<String, Object>> appointmentDTOs = appointments.stream()
                    .map(appointment -> {
                        // Lấy thông tin bác sĩ
                        User doctor = userRepository.findById(appointment.getDoctorId()).orElse(null);
                        
                        Map<String, Object> appointmentDTO = new HashMap<>();
                        appointmentDTO.put("appointmentId", appointment.getAppointmentId());
                        appointmentDTO.put("appointmentTime", appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                        appointmentDTO.put("appointmentDate", appointment.getAppointmentTime().toLocalDate().toString());
                        appointmentDTO.put("appointmentTimeOnly", appointment.getAppointmentTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                        appointmentDTO.put("doctorId", appointment.getDoctorId());
                        appointmentDTO.put("doctorName", doctor != null ? doctor.getFullName() : "Unknown Doctor");
                        appointmentDTO.put("doctorEmail", doctor != null ? doctor.getEmail() : "");
                        appointmentDTO.put("patientId", appointment.getCustomerId());
                        appointmentDTO.put("patientName", patient.getFullName());
                        appointmentDTO.put("room", appointment.getRoom() != null ? appointment.getRoom() : "Chưa xác định");
                        appointmentDTO.put("checkInStatus", appointment.getCheckInStatus() != null ? appointment.getCheckInStatus() : "Pending");
                        appointmentDTO.put("createdAt", appointment.getCreatedAt() != null ? 
                            appointment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
                        appointmentDTO.put("updatedAt", appointment.getUpdatedAt() != null ? 
                            appointment.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
                        
                        return appointmentDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(GenericResponse.builder()
                    .isSuccess(true)
                    .message(MessageDTO.builder().messageDetail("Appointments retrieved successfully").build())
                    .data(appointmentDTOs)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder().messageDetail("Error retrieving appointments: " + e.getMessage()).build())
                    .build());
        }
    }

    @GetMapping("/patient/{patientId}/appointments/count")
    @Operation(summary = "Get appointment count for a patient", description = "Get total number of appointments for a patient")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getPatientAppointmentCount(@PathVariable UUID patientId) {
        try {
            // Kiểm tra patient có tồn tại không
            User patient = userRepository.findById(patientId)
                    .orElse(null);
            
            if (patient == null) {
                return ResponseEntity.badRequest().body(GenericResponse.builder()
                        .isSuccess(false)
                        .message(MessageDTO.builder().messageDetail("Patient not found").build())
                        .build());
            }

            // Đếm tổng số appointments
            long totalAppointments = appointmentRepository.countByCustomerId(patientId);
            
            return ResponseEntity.ok(GenericResponse.builder()
                    .isSuccess(true)
                    .message(MessageDTO.builder().messageDetail("Appointment count retrieved successfully").build())
                    .data(Map.of(
                            "patientId", patientId,
                            "patientName", patient.getFullName(),
                            "totalAppointments", totalAppointments
                    ))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder().messageDetail("Error retrieving appointment count: " + e.getMessage()).build())
                    .build());
        }
    }
} 