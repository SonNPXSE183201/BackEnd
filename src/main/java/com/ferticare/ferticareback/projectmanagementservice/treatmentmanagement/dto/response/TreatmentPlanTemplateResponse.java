package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.TreatmentStepDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MedicationPlanDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MonitoringScheduleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentPlanTemplateResponse {
    private UUID templateId;
    private String name;
    private String description;
    private String treatmentType;
    private String planName;
    private String planDescription;
    private Integer estimatedDurationDays;
    private BigDecimal estimatedCost;
    private List<TreatmentStepDTO> treatmentSteps;
    private List<MedicationPlanDTO> medicationPlan;
    private List<MonitoringScheduleDTO> monitoringSchedule;
    private BigDecimal successProbability;
    private String riskFactors;
    private String contraindications;
    private Integer treatmentCycle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Boolean isActive;
} 