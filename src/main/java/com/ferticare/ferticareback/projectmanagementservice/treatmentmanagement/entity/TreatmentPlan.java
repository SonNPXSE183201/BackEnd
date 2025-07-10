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
@Table(name = "treatment_plan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TreatmentPlan extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "plan_id", nullable = false)
    private UUID planId;

    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    // Thông tin cơ bản
    @Column(name = "patient_id")
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "treatment_type", length = 20)
    private String treatmentType; // 'IUI', 'IVF', 'ICSI'

    @Column(name = "treatment_cycle")
    private Integer treatmentCycle; // Số chu kỳ điều trị

    // Thông tin phác đồ
    @Column(name = "plan_name", length = 255)
    private String planName;

    @Column(name = "plan_description", columnDefinition = "nvarchar(max)")
    private String planDescription;

    @Column(name = "estimated_duration_days")
    private Integer estimatedDurationDays;

    @Column(name = "estimated_cost", precision = 12, scale = 2)
    private BigDecimal estimatedCost;

    // Các bước điều trị chi tiết (JSON)
    @Column(name = "treatment_steps", columnDefinition = "nvarchar(max)")
    private String treatmentSteps; // JSON array của các bước

    @Column(name = "medication_plan", columnDefinition = "nvarchar(max)")
    private String medicationPlan; // JSON của thuốc men

    @Column(name = "monitoring_schedule", columnDefinition = "nvarchar(max)")
    private String monitoringSchedule; // JSON của lịch theo dõi

    // Đánh giá và tiên lượng
    @Column(name = "success_probability", precision = 4, scale = 2)
    private BigDecimal successProbability; // Tỷ lệ thành công dự kiến

    @Column(name = "risk_factors", columnDefinition = "nvarchar(max)")
    private String riskFactors;

    @Column(name = "contraindications", columnDefinition = "nvarchar(max)")
    private String contraindications;

    // Trạng thái và tiến độ
    @Column(name = "status", length = 20)
    private String status = "draft"; // 'draft', 'active', 'completed', 'cancelled'

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "current_phase")
    private UUID currentPhase;

    // Note: progressPercentage được tính toán từ treatment_schedule
    // Note: finalOutcome, outcomeDate, outcomeNotes được lưu trong treatment_result  
    // Note: approval workflow không cần thiết cho MVP
    // Note: createdBy, updatedBy, createdDate, updatedDate đã có trong BaseEntity
} 