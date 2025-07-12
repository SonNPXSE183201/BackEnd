package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity;

import com.ferticare.ferticareback.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "treatment_phase_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TreatmentPhaseStatus extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "status_id", nullable = false)
    private UUID statusId;

    @Column(name = "treatment_plan_id", nullable = false)
    private UUID treatmentPlanId;

    @Column(name = "phase_id", nullable = false)
    private UUID phaseId;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // 'Pending', 'In Progress', 'Completed', 'Cancelled'

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "notes", columnDefinition = "nvarchar(max)")
    private String notes;
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
