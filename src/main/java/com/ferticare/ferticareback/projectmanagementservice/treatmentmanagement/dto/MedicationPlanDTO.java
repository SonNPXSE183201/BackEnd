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
    }
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
