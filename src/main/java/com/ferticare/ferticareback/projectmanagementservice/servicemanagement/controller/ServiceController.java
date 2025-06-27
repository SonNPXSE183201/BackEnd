package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceResponse;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        try {
            List<ServiceResponse> services = serviceService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}