package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.ClinicalResultRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.ClinicalResultResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service.ClinicalResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/clinical-results")
@RequiredArgsConstructor
public class ClinicalResultController {

    private final ClinicalResultService clinicalResultService;
    private final JwtUtil jwtUtil;

    // Lấy chi tiết kết quả khám
    @GetMapping("/{resultId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ClinicalResultResponse> getById(@PathVariable("resultId") String resultId, Authentication authentication) {
        String userId = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(clinicalResultService.getClinicalResultByIdWithAccessCheck(UUID.fromString(resultId), userId, role));
    }

    // Cập nhật kết quả khám
    @PutMapping("/{resultId}")
    @DoctorOnly
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ClinicalResultResponse> update(@PathVariable("resultId") String resultId, @Validated @RequestBody ClinicalResultRequest request, Authentication authentication) {
        String userId = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(clinicalResultService.updateClinicalResult(resultId, request, userId, role));
    }

    // Lấy danh sách kết quả khám theo bệnh nhân
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ClinicalResultResponse>> getByPatient(@PathVariable("patientId") UUID patientId, Authentication authentication) {
        String userId = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(clinicalResultService.getClinicalResultsByPatientWithAccessCheck(patientId, userId, role));
    }

<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
