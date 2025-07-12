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
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
