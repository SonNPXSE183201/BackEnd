package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
public class DoctorResponseController {

    private final UserRepository userRepository;

    public DoctorResponseController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<DoctorScheduleDTO>> getDoctors() {
        List<Object[]> rows = userRepository.findDoctorWithSchedule();

        List<DoctorScheduleDTO> result = rows.stream()
                .map(row -> new DoctorScheduleDTO(
                        (UUID) row[0],
                        (String) row[1],
                        (String) row[2]
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

}