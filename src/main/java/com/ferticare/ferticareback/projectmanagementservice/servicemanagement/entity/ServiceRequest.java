package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_request")
@Data
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "request_id", nullable = false, updatable = false)
    private UUID requestId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "preferred_datetime", nullable = false)
    private LocalDateTime preferredDatetime;

    @Column(name = "note")
    private String note;

    @Column(name = "doctor_selection")
    private String doctorSelection;

    @Column(name = "preferred_doctor_id")
    private UUID preferredDoctorId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}