package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<DoctorScheduleDTO>> getDoctorsByService(@PathVariable UUID serviceId) {
        // Lấy tên chuyên môn (specialty) từ serviceId
        String specialty = serviceRepository.findById(serviceId)
                .map(s -> s.getName()) // getName là specialty
                .orElse(null);

        if (specialty == null) {
            return ResponseEntity.badRequest().body(List.of()); // hoặc trả lỗi
        }

        // Gọi repository lọc theo specialty
        List<Object[]> raw = userRepository.findDoctorWithScheduleBySpecialty(specialty);

        // Convert sang DTO
        List<DoctorScheduleDTO> result = raw.stream()
                .map(row -> new DoctorScheduleDTO(
                        (UUID) row[0],
                        (String) row[1],
                        (String) row[2]
                ))
                .toList();

        return ResponseEntity.ok(result);
    }
}
