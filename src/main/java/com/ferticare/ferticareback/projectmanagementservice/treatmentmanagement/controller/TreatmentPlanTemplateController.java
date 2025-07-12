package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.TreatmentStepDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MedicationPlanDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MonitoringScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.TreatmentPlanTemplateResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPlanTemplate;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository.TreatmentPlanTemplateRepository;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/treatment-plan-templates")
@RequiredArgsConstructor
@Slf4j
public class TreatmentPlanTemplateController {

    private final TreatmentPlanTemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Lấy danh sách template theo specialty của bác sĩ
     */
    @GetMapping
    @DoctorOnly
    public ResponseEntity<List<TreatmentPlanTemplateResponse>> getAllActiveTemplates(HttpServletRequest request) {
        log.info("Getting all active treatment plan templates for doctor");
        
        // Lấy thông tin bác sĩ hiện tại
        String doctorId = getCurrentDoctorId(request);
        String doctorSpecialty = getDoctorSpecialty(doctorId);
        
        log.info("Doctor specialty: {}", doctorSpecialty);
        
        // Lọc template theo specialty của bác sĩ
        List<TreatmentPlanTemplate> allTemplates = templateRepository.findByIsActiveTrueOrderByTreatmentType();
        List<TreatmentPlanTemplate> filteredTemplates = allTemplates.stream()
                .filter(template -> canDoctorAccessTemplate(doctorSpecialty, template.getTreatmentType()))
                .collect(Collectors.toList());
        
        List<TreatmentPlanTemplateResponse> responses = filteredTemplates.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        log.info("Returning {} templates for doctor with specialty: {}", responses.size(), doctorSpecialty);
        return ResponseEntity.ok(responses);
    }

    /**
     * Lấy template theo treatment type (IUI, IVF, ICSI) - có kiểm tra quyền
     */
    @GetMapping("/type/{treatmentType}")
    @DoctorOnly
    public ResponseEntity<TreatmentPlanTemplateResponse> getTemplateByType(
            @PathVariable String treatmentType, 
            HttpServletRequest request) {
        log.info("Getting template for treatment type: {} by doctor", treatmentType);
        
        // Kiểm tra quyền truy cập
        if (!canDoctorAccessTreatmentType(request, treatmentType)) {
            log.warn("Doctor not authorized to access treatment type: {}", treatmentType);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }
        
        TreatmentPlanTemplate template = templateRepository.findByTreatmentTypeIgnoreCaseAndIsActiveTrue(treatmentType);
        if (template != null) {
            return ResponseEntity.ok(convertToResponse(template));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Preview template - trả về template dạng JSON để bác sĩ preview trước khi tạo treatment plan
     */
    @GetMapping("/preview/{treatmentType}")
    @DoctorOnly
    public ResponseEntity<TreatmentPlanTemplateResponse> previewTemplate(
            @PathVariable String treatmentType, 
            HttpServletRequest request) {
        log.info("Previewing template for treatment type: {} by doctor", treatmentType);
        
        // Kiểm tra quyền truy cập
        if (!canDoctorAccessTreatmentType(request, treatmentType)) {
            log.warn("Doctor not authorized to preview treatment type: {}", treatmentType);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }
        
        TreatmentPlanTemplate template = templateRepository.findByTreatmentTypeIgnoreCaseAndIsActiveTrue(treatmentType);
        if (template != null) {
            log.info("Found template: {} for type: {}", template.getName(), treatmentType);
            return ResponseEntity.ok(convertToResponse(template));
        } else {
            log.warn("No template found for treatment type: {}", treatmentType);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lấy thông tin bác sĩ hiện tại từ JWT token
     */
    private String getCurrentDoctorId(HttpServletRequest request) {
        String jwt = jwtUtil.extractJwtFromRequest(request);
        return jwtUtil.extractUserId(jwt);
    }

    /**
     * Lấy specialty của bác sĩ từ profile
     */
    private String getDoctorSpecialty(String doctorId) {
        try {
            UUID doctorUuid = UUID.fromString(doctorId);
            User doctor = userRepository.findById(doctorUuid)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            
            return profileRepository.findByUser_Id(doctorUuid)
                    .map(profile -> profile.getSpecialty())
                    .orElse("UNKNOWN");
        } catch (Exception e) {
            log.error("Error getting doctor specialty: {}", e.getMessage());
            return "UNKNOWN";
        }
    }

    /**
     * Kiểm tra bác sĩ có quyền truy cập treatment type không
     */
    private boolean canDoctorAccessTreatmentType(HttpServletRequest request, String treatmentType) {
        String doctorId = getCurrentDoctorId(request);
        String doctorSpecialty = getDoctorSpecialty(doctorId);
        
        return canDoctorAccessTemplate(doctorSpecialty, treatmentType);
    }

    /**
     * Kiểm tra bác sĩ có quyền truy cập template không dựa trên specialty
     */
    private boolean canDoctorAccessTemplate(String doctorSpecialty, String treatmentType) {
        // Logic phân quyền:
        // - Bác sĩ IUI chỉ được truy cập template IUI
        // - Bác sĩ IVF chỉ được truy cập template IVF
        // - Bác sĩ có specialty khác hoặc UNKNOWN không được truy cập
        
        if (doctorSpecialty == null || doctorSpecialty.equals("UNKNOWN")) {
            return false;
        }
        
        String normalizedSpecialty = doctorSpecialty.toUpperCase().trim();
        String normalizedTreatmentType = treatmentType.toUpperCase().trim();
        
        // Mapping specialty với treatment type
        switch (normalizedSpecialty) {
            case "IUI":
                return "IUI".equals(normalizedTreatmentType);
            case "IVF":
                return "IVF".equals(normalizedTreatmentType);
            case "ICSI":
                return "ICSI".equals(normalizedTreatmentType);
            default:
                // Nếu specialty không khớp với treatment type nào, không cho phép
                return false;
        }
    }

    // Helper: Convert entity sang DTO, parse các trường JSON String sang List object
    private TreatmentPlanTemplateResponse convertToResponse(TreatmentPlanTemplate template) {
        try {
            List<TreatmentStepDTO> steps = template.getTreatmentSteps() != null && !template.getTreatmentSteps().isBlank()
                    ? objectMapper.readValue(template.getTreatmentSteps(), new TypeReference<List<TreatmentStepDTO>>() {})
                    : null;
            List<MedicationPlanDTO> meds = template.getMedicationPlan() != null && !template.getMedicationPlan().isBlank()
                    ? objectMapper.readValue(template.getMedicationPlan(), new TypeReference<List<MedicationPlanDTO>>() {})
                    : null;
            List<MonitoringScheduleDTO> monitoring = template.getMonitoringSchedule() != null && !template.getMonitoringSchedule().isBlank()
                    ? objectMapper.readValue(template.getMonitoringSchedule(), new TypeReference<List<MonitoringScheduleDTO>>() {})
                    : null;
            return new TreatmentPlanTemplateResponse(
                    template.getTemplateId(),
                    template.getName(),
                    template.getDescription(),
                    template.getTreatmentType(),
                    template.getPlanName(),
                    template.getPlanDescription(),
                    template.getEstimatedDurationDays(),
                    template.getEstimatedCost(),
                    steps,
                    meds,
                    monitoring,
                    template.getSuccessProbability(),
                    template.getRiskFactors(),
                    template.getContraindications(),
                    template.getTreatmentCycle(),
                    template.getCreatedAt(),
                    template.getUpdatedAt(),
                    template.getCreatedBy(),
                    template.getUpdatedBy(),
                    template.getIsActive()
            );
        } catch (Exception e) {
            log.error("Error parsing JSON fields in TreatmentPlanTemplate: {}", e.getMessage());
            throw new RuntimeException("Invalid template JSON fields", e);
        }
    }
} 