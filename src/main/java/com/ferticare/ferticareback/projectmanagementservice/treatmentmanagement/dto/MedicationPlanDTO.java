package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class MedicationPlanDTO {
    private String phase;
    private List<MedicationDTO> medications;

    @Data
    public static class MedicationDTO {
        private String name;
        private String dosage;
        private String frequency;
        private String duration;
        private String route;
    }
} 