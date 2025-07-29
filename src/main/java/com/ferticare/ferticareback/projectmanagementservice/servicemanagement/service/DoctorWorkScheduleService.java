package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DoctorWorkScheduleService {
    /**
     * Lấy lịch làm việc của một bác sĩ
     */
    List<DoctorScheduleDTO> getDoctorSchedule(UUID doctorId);
    
    /**
     * Tạo lịch làm việc mới cho bác sĩ
     */
    DoctorScheduleDTO createSchedule(DoctorWorkSchedule schedule);
    
    /**
     * Cập nhật lịch làm việc
     */
    DoctorScheduleDTO updateSchedule(String id, DoctorWorkSchedule schedule);
    
    /**
     * Xóa lịch làm việc
     */
    void deleteSchedule(String id);
    
    /**
     * Lấy lịch làm việc theo phòng ban với phân trang
     */
    Page<DoctorScheduleDTO> getSchedulesByDepartment(Long departmentId, Pageable pageable);
    
    /**
     * Tìm lịch làm việc khả dụng theo bác sĩ, ngày và phòng ban
     */
    List<DoctorScheduleDTO> findAvailableSchedules(UUID doctorId, LocalDate date, UUID departmentId);
    
    /**
     * Kiểm tra xung đột lịch làm việc
     * @return true nếu có xung đột, false nếu không
     */
    boolean checkScheduleConflict(DoctorWorkSchedule schedule);
    
    /**
     * Lấy một lịch làm việc theo ID
     */
    DoctorScheduleDTO getScheduleById(String id);
    
    /**
     * Phê duyệt một lịch làm việc
     */
    DoctorScheduleDTO approveSchedule(String id);
    
    /**
     * Cập nhật số lượng cuộc hẹn đã đặt
     */
    void updateAppointmentCount(String scheduleId, int change);
} 