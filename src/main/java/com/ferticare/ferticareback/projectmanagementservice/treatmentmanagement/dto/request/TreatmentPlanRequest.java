package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TreatmentPlanRequest {

    // === THÔNG TIN CƠ BẢN ===
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID templateId;                    // ID của template phác đồ (optional)

    @NotNull(message = "Treatment type is required")
    @Size(max = 20, message = "Treatment type must not exceed 20 characters")
    private String treatmentType; // 'IUI', 'IVF'

    private Integer treatmentCycle;             // Số chu kỳ điều trị

    // === THÔNG TIN PHÁC ĐỒ ===
    @Size(max = 255, message = "Plan name must not exceed 255 characters")
    private String planName;                    // Tên phác đồ

    private String planDescription;             // Mô tả kế hoạch điều trị

    private Integer estimatedDurationDays;      // Thời gian ước tính (ngày)
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    private BigDecimal estimatedCost;           // Chi phí ước tính

    // === CÁC BƯỚC ĐIỀU TRỊ ===
    private List<TreatmentStepDTO> treatmentSteps;              // Các bước điều trị (object)
    private List<MedicationPlanDTO> medicationPlan;                        // Kế hoạch dùng thuốc (object)
    private List<MonitoringScheduleDTO> monitoringSchedule;                // Lịch theo dõi (object)

    // === ĐÁNH GIÁ VÀ TIÊN LƯỢNG ===
    private BigDecimal successProbability;      // Tỷ lệ thành công dự kiến (%)
<<<<<<< HEAD
    
    private String riskFactors;                 // Yếu tố nguy cơ
    
=======

    private String riskFactors;                 // Yếu tố nguy cơ

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    private String contraindications;           // Chống chỉ định

    // === LỊCH TRÌNH ===
    private LocalDateTime startDate;            // Ngày bắt đầu dự kiến

    private LocalDateTime endDate;              // Ngày kết thúc dự kiến

    // === TRẠNG THÁI ===
    private String status;                      // 'draft', 'active', 'completed', 'cancelled'

    // === GHI CHÚ ===
    private String notes;                       // Ghi chú thêm từ bác sĩ
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
