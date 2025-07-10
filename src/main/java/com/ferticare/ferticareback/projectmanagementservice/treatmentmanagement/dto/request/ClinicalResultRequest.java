package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalResultRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    // === BAC SI ===
    private UUID doctorId; // Bác sĩ thực hiện khám

    // === THỜI GIAN KHÁM ===
    private LocalDateTime examinationDate; // Ngày giờ khám bệnh

    // === LOẠI KẾT QUẢ ===
    @Size(max = 20, message = "Result type must not exceed 20 characters")
    private String resultType;

    // === TRIỆU CHỨNG ===
    private String symptoms; // Triệu chứng (có thể là string hoặc JSON array)
    private String symptomsDetail; // Triệu chứng đã chọn (text)

    // === DẤU HIỆU LÂM SÀNG ===
    private Integer bloodPressureSystolic;   // Huyết áp tâm thu
    private Integer bloodPressureDiastolic;  // Huyết áp tâm trương  
    private BigDecimal temperature;          // Nhiệt độ (°C)
    private Integer heartRate;               // Nhịp tim (lần/phút)
    private BigDecimal weight;               // Cân nặng (kg)
    private BigDecimal height;               // Chiều cao (cm)

    // === XÉT NGHIỆM MÁU ===
    private BigDecimal fshLevel;            // FSH (mIU/mL)
    private BigDecimal lhLevel;             // LH (mIU/mL)  
    private BigDecimal estradiolLevel;      // Estradiol (pg/mL)
    private BigDecimal testosteroneLevel;   // Testosterone (ng/mL)
    private BigDecimal amhLevel;            // AMH (ng/mL)
    private BigDecimal prolactinLevel;      // Prolactin (ng/mL)
    
    // === XÉT NGHIỆM KHÁC ===
    private BigDecimal glucose;             // Glucose (mg/dL)
    private BigDecimal hemoglobin;          // Hemoglobin (g/dL)
    private BigDecimal creatinine;          // Creatinine (mg/dL)
    
    // === SIÊU ÂM CHI TIẾT ===
    private BigDecimal endometrialThickness; // Độ dày nội mạc tử cung (mm)
    private BigDecimal ovarySizeLeft;       // Kích thước buồng trứng trái (cm)
    private BigDecimal ovarySizeRight;      // Kích thước buồng trứng phải (cm)

    // === KẾT QUẢ SIÊU ÂM ===
    private String ultrasoundFindings;      // Mô tả kết quả siêu âm

    // === CHẨN ĐOÁN LÂM SÀNG ===
    @Size(max = 500, message = "Diagnosis must not exceed 500 characters")
    private String diagnosis;               // Nhập chuẩn đoán
    
    private String diagnosisCode;           // Mã chẩn đoán (ICD-10)
    private String severityLevel;           // Mức độ nghiêm trọng
    private Integer infertilityDurationMonths; // Thời gian vô sinh (tháng)
    private String previousTreatments;      // Điều trị trước đây

    // === KHUYẾN NGHỊ ĐIỀU TRỊ ===
    private String recommendations;         // Nhập khuyến nghị
    private String treatmentPriority;       // Mức độ ưu tiên điều trị

    // === FILE ĐÍNH KÈM ===
    private String attachedFileUrl;         // URL file kèm danh siêu âm, kết quả xét nghiệm

    // === GHI CHÚ ===
    private String notes;                   // Ghi chú thêm

    // === LỊCH HẸN TIẾP THEO ===
    private LocalDateTime nextAppointmentDate; // Lịch hẹn tái khám

    // === FIELD BỔ SUNG ĐỂ KHỚP DB ===
    private String bloodType;
    private BigDecimal bmi;
    private LocalDateTime completionDate;
    private Integer plateletCount;
    private BigDecimal whiteBloodCell;
    private Boolean isCompleted;
    private Integer follicleCountLeft;
    private Integer follicleCountRight;

    // === LIÊN KẾT VỚI APPOINTMENT ===
    private UUID appointmentId; // Liên kết với appointment
} 