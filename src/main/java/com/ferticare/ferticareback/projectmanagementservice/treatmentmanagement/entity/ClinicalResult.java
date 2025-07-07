package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity;

import com.ferticare.ferticareback.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clinical_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClinicalResult extends BaseEntity {

    @Id
    @Column(name = "result_id", nullable = false)
    private UUID resultId;

    @Column(name = "appointment_id", nullable = false)
    private UUID appointmentId;

    @Column(name = "result_type", nullable = false, length = 20)
    private String resultType;

    // Triệu chứng và chi tiết
    @Column(name = "symptoms", columnDefinition = "nvarchar(max)")
    private String symptoms; // JSON array hoặc string

    @Column(name = "symptoms_detail", columnDefinition = "nvarchar(max)")
    private String symptomsDetail;

    // File đính kèm và ghi chú
    @Column(name = "attached_file_url", length = 500)
    private String attachedFileUrl;

    @Column(name = "notes", columnDefinition = "nvarchar(max)")
    private String notes;

    // Thông tin cơ bản
    @Column(name = "patient_id")
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "examination_date")
    private LocalDateTime examinationDate;

    // Dấu hiệu sinh tồn
    @Column(name = "blood_pressure_systolic")
    private Integer bloodPressureSystolic;

    @Column(name = "blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;

    @Column(name = "temperature", precision = 4, scale = 1)
    private BigDecimal temperature;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "height", precision = 5, scale = 2)
    private BigDecimal height;

    @Column(name = "bmi", precision = 4, scale = 2)
    private BigDecimal bmi;

    // Kết quả xét nghiệm
    @Column(name = "blood_type", length = 10)
    private String bloodType;

    @Column(name = "hemoglobin", precision = 4, scale = 1)
    private BigDecimal hemoglobin;

    @Column(name = "white_blood_cell", precision = 6, scale = 2)
    private BigDecimal whiteBloodCell;

    @Column(name = "platelet_count")
    private Integer plateletCount;

    @Column(name = "glucose", precision = 4, scale = 1)
    private BigDecimal glucose;

    @Column(name = "creatinine", precision = 4, scale = 2)
    private BigDecimal creatinine;

    // Xét nghiệm nội tiết
    @Column(name = "fsh_level", precision = 6, scale = 2)
    private BigDecimal fshLevel; // FSH (mIU/mL)

    @Column(name = "lh_level", precision = 6, scale = 2)
    private BigDecimal lhLevel; // LH (mIU/mL)

    @Column(name = "estradiol_level", precision = 8, scale = 2)
    private BigDecimal estradiolLevel; // Estradiol (pg/mL)

    @Column(name = "testosterone_level", precision = 6, scale = 2)
    private BigDecimal testosteroneLevel; // Testosterone (ng/mL)

    @Column(name = "amh_level", precision = 6, scale = 2)
    private BigDecimal amhLevel; // AMH (ng/mL)

    @Column(name = "prolactin_level", precision = 6, scale = 2)
    private BigDecimal prolactinLevel; // Prolactin (ng/mL)

    // Kết quả siêu âm
    @Column(name = "ultrasound_findings", columnDefinition = "nvarchar(max)")
    private String ultrasoundFindings;

    @Column(name = "ovary_size_left", precision = 4, scale = 1)
    private BigDecimal ovarySizeLeft;

    @Column(name = "ovary_size_right", precision = 4, scale = 1)
    private BigDecimal ovarySizeRight;

    @Column(name = "follicle_count_left")
    private Integer follicleCountLeft;

    @Column(name = "follicle_count_right")
    private Integer follicleCountRight;

    @Column(name = "endometrial_thickness", precision = 4, scale = 1)
    private BigDecimal endometrialThickness;

    // Chẩn đoán và đánh giá
    @Column(name = "diagnosis", length = 500)
    private String diagnosis;

    @Column(name = "diagnosis_code", length = 20)
    private String diagnosisCode;

    @Column(name = "severity_level", length = 20)
    private String severityLevel;

    @Column(name = "infertility_duration_months")
    private Integer infertilityDurationMonths;

    @Column(name = "previous_treatments", columnDefinition = "nvarchar(max)")
    private String previousTreatments;

    // Khuyến nghị
    @Column(name = "recommendations", columnDefinition = "nvarchar(max)")
    private String recommendations;

    @Column(name = "treatment_priority", length = 20)
    private String treatmentPriority;

    @Column(name = "next_appointment_date")
    private LocalDateTime nextAppointmentDate;

    // Trạng thái
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;
}