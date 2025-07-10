package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "treatment_plan_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentPlanTemplate {

    @Id
    @Column(name = "template_id")
    private UUID templateId;

    // Thông tin cơ bản template
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    @Column(name = "treatment_type", length = 20)
    private String treatmentType; // 'IUI', 'IVF', 'ICSI'

    // Thông tin phác đồ mẫu
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

    // Đánh giá và tiên lượng mẫu
    @Column(name = "success_probability", precision = 4, scale = 2)
    private BigDecimal successProbability; // Tỷ lệ thành công dự kiến

    @Column(name = "risk_factors", columnDefinition = "nvarchar(max)")
    private String riskFactors;

    @Column(name = "contraindications", columnDefinition = "nvarchar(max)")
    private String contraindications;

    // Thông tin chu kỳ điều trị mẫu
    @Column(name = "treatment_cycle")
    private Integer treatmentCycle; // Số chu kỳ điều trị mặc định

    // Note: Template chỉ cần thông tin mẫu cơ bản
    // Các thông tin tiến độ, kết quả, phê duyệt không cần thiết cho template

    // Thông tin tạo và cập nhật
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_active")
    private Boolean isActive = true; // Template có đang hoạt động không
} 