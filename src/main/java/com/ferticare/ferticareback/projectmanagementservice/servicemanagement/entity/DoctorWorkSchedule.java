package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "doctor_work_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorWorkSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schedule_id")
    private UUID id;
    
    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;
    
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 2=Thứ 2, 3=Thứ 3, ..., 8=Chủ nhật
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "room", nullable = false)
    private String room;
    
    @Column(name = "effective_from")
    private LocalDate effectiveFrom;
    
    @Column(name = "effective_to")
    private LocalDate effectiveTo;
    
    @Column(name = "status")
    private String status; // active, inactive, approved, pending
    
    @Column(name = "max_appointments")
    private Integer maxAppointments; // Số lượng cuộc hẹn tối đa cho ca làm việc
    
    @Column(name = "appointment_count")
    private Integer appointmentCount; // Số cuộc hẹn đã đặt
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @Column(name = "note")
    private String note;
    
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
} 