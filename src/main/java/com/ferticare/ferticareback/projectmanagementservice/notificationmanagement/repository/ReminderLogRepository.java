package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity.ReminderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderLogRepository extends JpaRepository<ReminderLog, UUID> {
    List<ReminderLog> findByStatusAndChannelAndReminderTimeBefore(String status, String channel, LocalDateTime time);
    List<ReminderLog> findByStatus(String status);
    List<ReminderLog> findByAppointmentId(UUID appointmentId);
} 