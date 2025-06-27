package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reminderLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderLog {
    @Id
    @Column(name = "reminder_id")
    private UUID reminderId;

    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "reminder_time")
    private LocalDateTime reminderTime;

    @Column(name = "channel")
    private String channel; // "EMAIL", "SMS",...

    @Column(name = "status")
    private String status; // "PENDING", "SENT", "FAILED"
} 