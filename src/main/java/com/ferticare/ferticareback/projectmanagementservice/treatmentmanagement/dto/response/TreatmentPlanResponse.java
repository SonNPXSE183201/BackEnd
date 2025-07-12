package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.TreatmentStepDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MedicationPlanDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MonitoringScheduleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentPlanResponse {
<<<<<<< HEAD
      // Primary key from BaseEntity
    private UUID planId;
    private UUID templateId;
    
=======
    // Primary key from BaseEntity
    private UUID planId;
    private UUID templateId;

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Thông tin cơ bản
    private UUID patientId;
    private String doctorId;
    private String treatmentType;
    private Integer treatmentCycle;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Thông tin phác đồ
    private String planName;
    private String planDescription;
    private Integer estimatedDurationDays;
    private BigDecimal estimatedCost;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Các bước điều trị chi tiết (List)
    private List<TreatmentStepDTO> treatmentSteps;
    private List<MedicationPlanDTO> medicationPlan;
    private List<MonitoringScheduleDTO> monitoringSchedule;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Đánh giá và tiên lượng
    private BigDecimal successProbability;
    private String riskFactors;
    private String contraindications;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Trạng thái và tiến độ
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID currentPhase;
<<<<<<< HEAD
    
    // Note: progressPercentage được tính toán từ treatment_schedule
    // Note: finalOutcome, outcomeDate, outcomeNotes được lưu trong treatment_result  
    // Note: approval workflow không cần thiết cho MVP
    // Note: customizedSteps đã được thay thế bằng treatmentSteps JSON
    
=======

    // Note: progressPercentage được tính toán từ treatment_schedule
    // Note: finalOutcome, outcomeDate, outcomeNotes được lưu trong treatment_result
    // Note: approval workflow không cần thiết cho MVP
    // Note: customizedSteps đã được thay thế bằng treatmentSteps JSON

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Thông tin tạo và cập nhật
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
