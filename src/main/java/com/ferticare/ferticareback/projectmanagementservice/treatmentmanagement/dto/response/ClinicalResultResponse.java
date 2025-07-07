package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalResultResponse {

    private UUID resultId;      // Primary key
    private String resultType;

    // Triệu chứng
    private String symptoms;
    private String symptomsDetail;

    // Thông tin cơ bản
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime examinationDate;

    // Dấu hiệu sinh tồn
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private BigDecimal temperature;
    private Integer heartRate;
    private BigDecimal weight;
    private BigDecimal height;
    private BigDecimal bmi;

    // Kết quả xét nghiệm
    private String bloodType;
    private BigDecimal hemoglobin;
    private BigDecimal whiteBloodCell;
    private Integer plateletCount;
    private BigDecimal glucose;
    private BigDecimal creatinine;

    // Xét nghiệm nội tiết
    private BigDecimal fshLevel;
    private BigDecimal lhLevel;
    private BigDecimal estradiolLevel;
    private BigDecimal testosteroneLevel;
    private BigDecimal amhLevel;
    private BigDecimal prolactinLevel;

    // Kết quả siêu âm
    private String ultrasoundFindings;
    private BigDecimal ovarySizeLeft;
    private BigDecimal ovarySizeRight;
    private Integer follicleCountLeft;
    private Integer follicleCountRight;
    private BigDecimal endometrialThickness;

    // Chẩn đoán và đánh giá
    private String diagnosis;
    private String diagnosisCode;
    private String severityLevel;
    private Integer infertilityDurationMonths;
    private String previousTreatments;

    // Khuyến nghị
    private String recommendations;
    private String treatmentPriority;
    private LocalDateTime nextAppointmentDate;

    // File đính kèm và ghi chú
    private String attachedFileUrl;
    private String notes;

    // Trạng thái
    private Boolean isCompleted;
    private LocalDateTime completionDate;

    // Thông tin tạo và cập nhật
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}