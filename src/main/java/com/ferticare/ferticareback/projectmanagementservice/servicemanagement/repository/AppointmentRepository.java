package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // ✅ Kiểm tra bác sĩ có bị trùng lịch
    boolean existsByDoctorIdAndAppointmentTime(UUID doctorId, LocalDateTime appointmentTime);

    // ✅ Lấy danh sách appointment theo customerId
    List<Appointment> findByCustomerId(UUID customerId);

    // ✅ Lấy danh sách appointment theo doctorId
    List<Appointment> findByDoctorId(UUID doctorId);

    // ✅ Trả về danh sách bác sĩ bận ở thời điểm đó
    @Query("SELECT a.doctorId FROM Appointment a WHERE a.appointmentTime = :time")
    List<UUID> findDoctorIdsBusyAt(@Param("time") LocalDateTime time);

    // ✅ Lấy danh sách thời gian đã có lịch hẹn của bác sĩ trong một khoảng thời gian
    @Query("SELECT a.appointmentTime FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentTime BETWEEN :startTime AND :endTime")
    List<LocalDateTime> findAppointmentTimesByDoctorIdAndDateRange(
            @Param("doctorId") UUID doctorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}