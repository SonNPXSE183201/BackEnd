package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity;

import com.ferticare.ferticareback.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "treatment_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TreatmentSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schedule_id")
    private UUID scheduleId;

    @Column(name = "plan_id")
    private UUID planId;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "treatment_type")
    private String treatmentType;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "status")
    private String status;

    @Column(name = "notes")
    private String notes;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "patient_id")
    private UUID patientId;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "grace_period_days")
    private Integer gracePeriodDays;

    @Column(name = "step_name")
    private String stepName;

    @Column(name = "step_number")
    private Integer stepNumber;
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
