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
<<<<<<< HEAD
    
    // Triệu chứng
    private String symptoms;
    private String symptomsDetail;
    
=======

    // Triệu chứng
    private String symptoms;
    private String symptomsDetail;

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Thông tin cơ bản
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime examinationDate;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Dấu hiệu sinh tồn
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private BigDecimal temperature;
    private Integer heartRate;
    private BigDecimal weight;
    private BigDecimal height;
    private BigDecimal bmi;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Kết quả xét nghiệm
    private String bloodType;
    private BigDecimal hemoglobin;
    private BigDecimal whiteBloodCell;
    private Integer plateletCount;
    private BigDecimal glucose;
    private BigDecimal creatinine;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Xét nghiệm nội tiết
    private BigDecimal fshLevel;
    private BigDecimal lhLevel;
    private BigDecimal estradiolLevel;
    private BigDecimal testosteroneLevel;
    private BigDecimal amhLevel;
    private BigDecimal prolactinLevel;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Kết quả siêu âm
    private String ultrasoundFindings;
    private BigDecimal ovarySizeLeft;
    private BigDecimal ovarySizeRight;
    private Integer follicleCountLeft;
    private Integer follicleCountRight;
    private BigDecimal endometrialThickness;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Chẩn đoán và đánh giá
    private String diagnosis;
    private String diagnosisCode;
    private String severityLevel;
    private Integer infertilityDurationMonths;
    private String previousTreatments;
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Khuyến nghị
    private String recommendations;
    private String treatmentPriority;
    private LocalDateTime nextAppointmentDate;
<<<<<<< HEAD
    
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
=======

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
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
