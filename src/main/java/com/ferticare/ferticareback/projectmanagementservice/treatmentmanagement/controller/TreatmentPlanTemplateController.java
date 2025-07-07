package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPlanTemplate;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository.TreatmentPlanTemplateRepository;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatment-plan-templates")
@RequiredArgsConstructor
@Slf4j
public class TreatmentPlanTemplateController {

    private final TreatmentPlanTemplateRepository templateRepository;

    /**
     * Lấy danh sách tất cả template đang hoạt động
     */
    @GetMapping
    @DoctorOnly
    public ResponseEntity<List<TreatmentPlanTemplate>> getAllActiveTemplates() {
        log.info("Getting all active treatment plan templates");

        List<TreatmentPlanTemplate> templates = templateRepository.findByIsActiveTrueOrderByTreatmentType();

        return ResponseEntity.ok(templates);
    }

    /**
     * Lấy template theo treatment type (IUI, IVF)
     */
    @GetMapping("/type/{treatmentType}")
    @DoctorOnly
    public ResponseEntity<TreatmentPlanTemplate> getTemplateByType(@PathVariable String treatmentType) {
        log.info("Getting template for treatment type: {}", treatmentType);

        TreatmentPlanTemplate template = templateRepository
                .findByTreatmentTypeIgnoreCaseAndIsActiveTrue(treatmentType);

        if (template != null) {
            return ResponseEntity.ok(template);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Preview template - trả về template dạng JSON để bác sĩ preview trước khi tạo treatment plan
     */
    @GetMapping("/preview/{treatmentType}")
    @DoctorOnly
    public ResponseEntity<TreatmentPlanTemplate> previewTemplate(@PathVariable String treatmentType) {
        log.info("Previewing template for treatment type: {}", treatmentType);

        TreatmentPlanTemplate template = templateRepository
                .findByTreatmentTypeIgnoreCaseAndIsActiveTrue(treatmentType);

        if (template != null) {
            log.info("Found template: {} for type: {}", template.getName(), treatmentType);
            return ResponseEntity.ok(template);
        } else {
            log.warn("No template found for treatment type: {}", treatmentType);
            return ResponseEntity.notFound().build();
        }
    }
}