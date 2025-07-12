package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PhaseStatusResponse {
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    private UUID statusId;
    private UUID treatmentPlanId;
    private UUID phaseId;
    private String phaseName;
    private Integer phaseOrder;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String notes;
    private String description;
    private Integer expectedDuration;
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
