package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.TreatmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TreatmentScheduleRepository extends JpaRepository<TreatmentSchedule, UUID> {
    // Lấy các slot đã book của bác sĩ trong ngày
    @Query("SELECT ts.scheduledDate FROM TreatmentSchedule ts WHERE ts.doctorId = :doctorId AND ts.scheduledDate BETWEEN :startOfDay AND :endOfDay")
    List<LocalDateTime> findBookedTimesByDoctorIdAndDateRange(@Param("doctorId") UUID doctorId,
                                                             @Param("startOfDay") LocalDateTime startOfDay,
                                                             @Param("endOfDay") LocalDateTime endOfDay);

    // Lấy các schedule cần nhắc nhở 24h trước (trong khoảng 24h-23h30)
    @Query("SELECT ts FROM TreatmentSchedule ts WHERE ts.status = 'scheduled' AND ts.scheduledDate BETWEEN :startTime AND :endTime")
    List<TreatmentSchedule> findSchedulesFor24HourReminder(@Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime);

    // Lấy các schedule cần nhắc nhở 2h trước (trong khoảng 2h-1h50)
    @Query("SELECT ts FROM TreatmentSchedule ts WHERE ts.status = 'scheduled' AND ts.scheduledDate BETWEEN :startTime AND :endTime")
    List<TreatmentSchedule> findSchedulesFor2HourReminder(@Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime);

    // Lấy các schedule quá hạn 30 phút (trong khoảng 30-40 phút)
    @Query("SELECT ts FROM TreatmentSchedule ts WHERE ts.status = 'scheduled' AND ts.scheduledDate BETWEEN :startTime AND :endTime")
    List<TreatmentSchedule> findOverdueSchedules(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    // Lấy các schedule quá hạn 1 ngày để hủy
    @Query("SELECT ts FROM TreatmentSchedule ts WHERE ts.status = 'scheduled' AND ts.scheduledDate < :cutoffTime")
    List<TreatmentSchedule> findSchedulesToCancel(@Param("cutoffTime") LocalDateTime cutoffTime);

    // 🆕 Lấy tất cả treatment schedule theo bệnh nhân
    List<TreatmentSchedule> findByPatientIdOrderByScheduledDateDesc(UUID patientId);

    // 🆕 Lấy treatment schedule theo bác sĩ
    List<TreatmentSchedule> findByDoctorIdOrderByScheduledDateDesc(UUID doctorId);
} 