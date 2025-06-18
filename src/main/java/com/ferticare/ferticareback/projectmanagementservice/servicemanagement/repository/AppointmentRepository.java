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


    // ✅ Trả về danh sách bác sĩ bận ở thời điểm đó
    @Query("SELECT a.doctorId FROM Appointment a WHERE a.appointmentTime = :time")
    List<UUID> findDoctorIdsBusyAt(@Param("time") LocalDateTime time);
}