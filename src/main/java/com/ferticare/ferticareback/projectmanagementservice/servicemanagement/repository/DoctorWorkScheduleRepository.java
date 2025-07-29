package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorWorkScheduleRepository extends JpaRepository<DoctorWorkSchedule, UUID> {
    
    // Lấy lịch làm việc của bác sĩ theo ngày trong tuần
    List<DoctorWorkSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, Integer dayOfWeek);
    
    // Lấy tất cả lịch làm việc của bác sĩ
    List<DoctorWorkSchedule> findByDoctorId(UUID doctorId);
    
    // Lấy lịch làm việc theo department
    List<DoctorWorkSchedule> findByDepartment(Department department);
    
    // Lấy lịch làm việc theo status
    List<DoctorWorkSchedule> findByStatus(String status);
    
    // Lấy lịch làm việc có hiệu lực trong khoảng thời gian
    @Query("SELECT dws FROM DoctorWorkSchedule dws " +
           "WHERE (dws.effectiveFrom IS NULL OR dws.effectiveFrom <= :date) " +
           "AND (dws.effectiveTo IS NULL OR dws.effectiveTo >= :date)")
    List<DoctorWorkSchedule> findActiveSchedulesForDate(@Param("date") LocalDate date);
    
    // Lấy lịch làm việc có sẵn trong khoảng thời gian
    @Query("SELECT dws FROM DoctorWorkSchedule dws " +
           "WHERE dws.doctorId = :doctorId " +
           "AND dws.dayOfWeek = :dayOfWeek " +
           "AND dws.startTime <= CAST(:time AS time) " +
           "AND dws.endTime > CAST(:time AS time)")
    List<DoctorWorkSchedule> findAvailableSlots(@Param("doctorId") UUID doctorId, 
                                               @Param("dayOfWeek") Integer dayOfWeek, 
                                               @Param("time") LocalTime time);
    
    // Lấy lịch làm việc theo chuyên ngành và department
    @Query("SELECT dws FROM DoctorWorkSchedule dws " +
           "JOIN Profile p ON dws.doctorId = p.user.id " +
           "WHERE p.specialty = :specialty " +
           "AND dws.department.id = :departmentId " +
           "AND dws.dayOfWeek = :dayOfWeek")
    List<DoctorWorkSchedule> findBySpecialtyAndDepartmentAndDayOfWeek(
            @Param("specialty") String specialty, 
            @Param("departmentId") String departmentId,
            @Param("dayOfWeek") Integer dayOfWeek);
    
    // Kiểm tra xung đột lịch làm việc
    @Query("SELECT COUNT(dws) > 0 FROM DoctorWorkSchedule dws " +
           "WHERE dws.doctorId = :doctorId " +
           "AND dws.dayOfWeek = :dayOfWeek " +
           "AND dws.id <> :scheduleId " +
           "AND (" +
           "    (dws.startTime <= :startTime AND dws.endTime > :startTime) OR " +
           "    (dws.startTime < :endTime AND dws.endTime >= :endTime) OR " +
           "    (dws.startTime >= :startTime AND dws.endTime <= :endTime)" +
           ")")
    boolean existsConflictingSchedule(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") Integer dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("scheduleId") UUID scheduleId);
} 