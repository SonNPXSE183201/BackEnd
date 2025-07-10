package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity;

import com.ferticare.ferticareback.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "treatment_phase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TreatmentPhase extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "phase_id", nullable = false)
    private UUID phaseId;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "phase_name", nullable = false, length = 255)
    private String phaseName;

    @Column(name = "phase_order", nullable = false)
    private Integer phaseOrder;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    @Column(name = "expected_duration")
    private Integer expectedDuration;
} 