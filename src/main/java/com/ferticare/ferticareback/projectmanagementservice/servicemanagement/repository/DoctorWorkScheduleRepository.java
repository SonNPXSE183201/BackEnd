package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorWorkScheduleRepository extends JpaRepository<DoctorWorkSchedule, UUID> {
    
    // Lấy lịch làm việc của bác sĩ theo ngày trong tuần
    List<DoctorWorkSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, Integer dayOfWeek);
    
    // Lấy tất cả lịch làm việc của bác sĩ
    List<DoctorWorkSchedule> findByDoctorId(UUID doctorId);
    
    // Lấy lịch làm việc có sẵn trong khoảng thời gian
    @Query("SELECT dws FROM DoctorWorkSchedule dws " +
           "WHERE dws.doctorId = :doctorId " +
           "AND dws.dayOfWeek = :dayOfWeek " +
           "AND dws.startTime <= CAST(:time AS time) " +
           "AND dws.endTime > CAST(:time AS time)")
    List<DoctorWorkSchedule> findAvailableSlots(@Param("doctorId") UUID doctorId, 
                                               @Param("dayOfWeek") Integer dayOfWeek, 
                                               @Param("time") LocalTime time);
    
    // Lấy lịch làm việc theo chuyên ngành (tạm thời comment để tránh lỗi)
    /*
    @Query("SELECT dws FROM DoctorWorkSchedule dws " +
           "JOIN Profile p ON dws.doctorId = p.userId " +
           "WHERE p.specialty = :specialty " +
           "AND dws.dayOfWeek = :dayOfWeek")
    List<DoctorWorkSchedule> findBySpecialtyAndDayOfWeek(@Param("specialty") String specialty, 
                                                        @Param("dayOfWeek") Integer dayOfWeek);
    */
} 