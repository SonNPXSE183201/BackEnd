package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PhaseStatusResponse {
    
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
    private String clinicalResultId;
} 