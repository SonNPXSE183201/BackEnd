package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TreatmentScheduleRequest {
    private UUID planId;
    private UUID phaseId; // hoặc stepNumber nếu cần
    private Integer stepNumber;
    private String stepName;
    private UUID doctorId;
    private UUID patientId;
    private LocalDateTime scheduledDate;
    private String roomId;
    private String notes;
    private String treatmentType;
    private LocalDateTime deadline;
    private Integer gracePeriodDays;
} 