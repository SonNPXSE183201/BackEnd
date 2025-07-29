package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.ServiceRequestService;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-request")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @PostMapping
    public ResponseEntity<?> submitRequest(
            @RequestBody ServiceRequestDTO dto,
            HttpServletRequest request
    ) {
        return serviceRequestService.handleRequestWithAuth(dto, request);
    }
    
    @GetMapping("/available-doctors/{serviceId}")
    public ResponseEntity<?> getAvailableDoctors(@PathVariable UUID serviceId) {
        try {
            return serviceRequestService.getAvailableDoctorsByService(serviceId);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Lỗi khi lấy danh sách bác sĩ: " + e.getMessage());
        }
    }
    
    @GetMapping("/doctor-available-times/{doctorId}")
    public ResponseEntity<?> getDoctorAvailableTimes(
            @PathVariable UUID doctorId,
            @RequestParam(required = false) LocalDate date) {
        try {
            // Nếu không truyền date, truyền null để service trả về slot 30 ngày tới
            return serviceRequestService.getDoctorAvailableTimes(doctorId, date);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Lỗi khi lấy thời gian rảnh: " + e.getMessage());
        }
    }
    
    @GetMapping("/available-dates/{doctorId}")
    public ResponseEntity<?> getAvailableDates(@PathVariable UUID doctorId) {
        try {
            return serviceRequestService.getAvailableDates(doctorId);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Lỗi khi lấy danh sách ngày: " + e.getMessage());
        }
    }

    /**
     * 🆕 API cho customer xem thông tin giới hạn lịch hẹn của mình
     */
    @GetMapping("/customer/{customerId}/appointment-limit")
    public ResponseEntity<?> getCustomerAppointmentLimit(@PathVariable UUID customerId) {
        try {
            return serviceRequestService.getCustomerAppointmentLimit(customerId);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Lỗi khi lấy thông tin giới hạn lịch hẹn: " + e.getMessage());
        }
    }
}