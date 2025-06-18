package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.ManagerOnly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @ManagerOnly
    @GetMapping("/report")
    public ResponseEntity<?> viewReport() {
        return ResponseEntity.ok("Welcome Manager! Here is your report.");
    }
}