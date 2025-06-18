package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.ServiceRequest;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRequestRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {

    private final ServiceRequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    public ResponseEntity<?> handleRequest(UUID userId, ServiceRequestDTO dto) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            System.out.println(">>> Người dùng không tồn tại: " + userId);
            return ResponseEntity.status(404).body("Người dùng không tồn tại");
        }
        User user = userOpt.get();

        // Lấy tên chuyên môn từ ID dịch vụ
        String specialty = serviceRepository.findById(dto.getServiceId())
                .map(s -> s.getName())
                .orElse(null);

        if (specialty == null) {
            System.out.println(">>> Không tìm thấy dịch vụ: " + dto.getServiceId());
            return ResponseEntity.badRequest().body("Không tìm thấy dịch vụ.");
        }

        System.out.println(">>> Tên chuyên môn từ dịch vụ: " + specialty);

        // Log danh sách bác sĩ theo chuyên môn (đã active)
        List<Object[]> doctorRows = userRepository.findDoctorWithScheduleBySpecialty(specialty);
        System.out.println(">>> Tổng số bác sĩ có chuyên môn " + specialty + ": " + doctorRows.size());
        for (Object[] row : doctorRows) {
            System.out.println("Doctor ID: " + row[0] + ", Name: " + row[1] + ", Schedule: " + row[2]);
        }

        LocalDateTime preferredTime = dto.getAppointmentTime();

        // Tạo bản ghi yêu cầu
        ServiceRequest request = new ServiceRequest();
        request.setCustomerId(userId);
        request.setServiceId(dto.getServiceId());
        request.setPreferredDatetime(preferredTime);
        request.setNote(dto.getNote());
        request.setDoctorSelection(dto.getDoctorSelection());
        request.setPreferredDoctorId("manual".equalsIgnoreCase(dto.getDoctorSelection()) ? dto.getDoctorId() : null);
        request.setStatus("PendingAssignment");
        requestRepository.save(request);

        UUID assignedDoctorId;

        if ("manual".equalsIgnoreCase(dto.getDoctorSelection())) {
            System.out.println(">>> Chế độ chọn bác sĩ: THỦ CÔNG");
            boolean isBusy = appointmentRepository.existsByDoctorIdAndAppointmentTime(dto.getDoctorId(), preferredTime);
            System.out.println(">>> Bác sĩ " + dto.getDoctorId() + " có bị trùng giờ " + preferredTime + ": " + isBusy);
            if (isBusy) {
                return ResponseEntity.badRequest().body("Bác sĩ đã bị trùng lịch. Vui lòng chọn giờ khác");
            }
            assignedDoctorId = dto.getDoctorId();
        } else {
            System.out.println(">>> Chế độ chọn bác sĩ: TỰ ĐỘNG");
            List<UUID> busyDoctorIds = appointmentRepository.findDoctorIdsBusyAt(preferredTime);
            System.out.println(">>> Bác sĩ bận tại " + preferredTime + ": " + busyDoctorIds);

            // Gọi danh sách bác sĩ không bận
            PageRequest pageRequest = PageRequest.of(0, 1);
            List<User> availableDoctors = userRepository.findFirstAvailableDoctorExcluding(
                    busyDoctorIds.isEmpty() ? List.of(UUID.randomUUID()) : busyDoctorIds,
                    specialty,
                    pageRequest
            );

            System.out.println(">>> Số bác sĩ rảnh: " + availableDoctors.size());
            availableDoctors.forEach(doc -> System.out.println("Rảnh: " + doc.getId() + " - " + doc.getFullName()));

            if (availableDoctors.isEmpty()) {
                return ResponseEntity.badRequest().body("Không có bác sĩ phù hợp rảnh vào giờ này.");
            }
            assignedDoctorId = availableDoctors.get(0).getId();
        }

        // Tạo lịch hẹn
        createAppointment(assignedDoctorId, userId, preferredTime, request.getRequestId());

        // Trả kết quả
        User doctor = userRepository.findById(assignedDoctorId).orElse(null);
        Map<String, Object> response = new HashMap<>();
        response.put("id", request.getRequestId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());
        response.put("address", user.getAddress());
        response.put("service", Map.of("id", dto.getServiceId(), "name", specialty));
        response.put("doctor", doctor != null ? Map.of("id", doctor.getId(), "name", doctor.getFullName()) : null);
        response.put("appointmentDate", preferredTime != null ? preferredTime.toLocalDate() : null);
        response.put("appointmentTime", preferredTime != null ? preferredTime.toLocalTime() : null);
        response.put("notes", dto.getNote());
        response.put("status", request.getStatus());
        response.put("createdAt", request.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    private void createAppointment(UUID doctorId, UUID customerId, LocalDateTime time, UUID requestId) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(UUID.randomUUID());
        appointment.setCustomerId(customerId);
        appointment.setDoctorId(doctorId);
        appointment.setAppointmentTime(time);
        appointment.setRequestId(requestId);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setCheckInStatus("Pending");
        appointmentRepository.save(appointment);
    }

    private String getServiceNameById(UUID serviceId) {
        return serviceRepository.findById(serviceId)
                .map(s -> s.getName())  // hoặc s -> ((com.ferticare...)s).getName()
                .orElse("Unknown Service");
    }
}