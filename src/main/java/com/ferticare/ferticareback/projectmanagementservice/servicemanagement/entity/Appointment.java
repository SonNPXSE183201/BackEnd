package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointment")
@Data
public class Appointment {
    @Id
    private UUID appointmentId;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Column(name = "check_in_status")
    private String checkInStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Column(name = "room")
    private String room;
}
