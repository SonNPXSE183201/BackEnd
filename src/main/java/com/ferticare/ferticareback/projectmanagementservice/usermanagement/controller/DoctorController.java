package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @DoctorOnly
    @GetMapping("/schedule")
    public ResponseEntity<?> getSchedule() {
        return ResponseEntity.ok("Welcome Doctor! Here is your schedule.");
    }
}