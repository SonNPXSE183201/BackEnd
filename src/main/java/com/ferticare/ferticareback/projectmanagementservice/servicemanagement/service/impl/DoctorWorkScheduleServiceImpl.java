package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.impl;

import com.ferticare.ferticareback.common.exception.AppException;
import com.ferticare.ferticareback.common.utils.DateTimeUtil;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DepartmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DoctorWorkScheduleRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.DoctorWorkScheduleService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorWorkScheduleServiceImpl implements DoctorWorkScheduleService {

    private final DoctorWorkScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<DoctorScheduleDTO> getDoctorSchedule(UUID doctorId) {
        List<DoctorWorkSchedule> schedules = scheduleRepository.findByDoctorId(doctorId);
        return schedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorScheduleDTO createSchedule(DoctorWorkSchedule schedule) {
        if (checkScheduleConflict(schedule)) {
            throw new AppException("SCHEDULE_CONFLICT", "Có xung đột lịch trình trong khoảng thời gian này");
        }
        if (schedule.getStatus() == null) {
            schedule.setStatus("pending");
        }
        if (schedule.getMaxAppointments() == null) {
            schedule.setMaxAppointments(5);
        }
        if (schedule.getAppointmentCount() == null) {
            schedule.setAppointmentCount(0);
        }
        DoctorWorkSchedule savedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(savedSchedule);
    }

    @Override
    @Transactional
    public DoctorScheduleDTO updateSchedule(String id, DoctorWorkSchedule updateData) {
        DoctorWorkSchedule existingSchedule = findScheduleById(id);
        existingSchedule.setStartTime(updateData.getStartTime());
        existingSchedule.setEndTime(updateData.getEndTime());
        existingSchedule.setRoom(updateData.getRoom());
        existingSchedule.setEffectiveFrom(updateData.getEffectiveFrom());
        existingSchedule.setEffectiveTo(updateData.getEffectiveTo());
        existingSchedule.setMaxAppointments(updateData.getMaxAppointments());
        existingSchedule.setDepartment(updateData.getDepartment());
        existingSchedule.setNote(updateData.getNote());
        if (checkScheduleConflict(existingSchedule)) {
            throw new AppException("SCHEDULE_CONFLICT", "Có xung đột lịch trình trong khoảng thời gian này");
        }
        DoctorWorkSchedule savedSchedule = scheduleRepository.save(existingSchedule);
        return convertToDTO(savedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(String id) {
        DoctorWorkSchedule schedule = findScheduleById(id);
        if (schedule.getAppointmentCount() != null && schedule.getAppointmentCount() > 0) {
            throw new AppException("SCHEDULE_HAS_APPOINTMENTS", "Không thể xóa lịch làm việc đã có lịch hẹn");
        }
        scheduleRepository.delete(schedule);
    }

    @Override
    public Page<DoctorScheduleDTO> getSchedulesByDepartment(Long departmentId, Pageable pageable) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException("DEPARTMENT_NOT_FOUND", "Không tìm thấy phòng ban"));
        Page<Profile> profiles = profileRepository.findByDepartment(department, pageable);
        List<UUID> doctorIds = profiles.getContent().stream()
                .map(profile -> profile.getUser().getId())
                .collect(Collectors.toList());
        List<DoctorScheduleDTO> schedules = new ArrayList<>();
        for (UUID doctorId : doctorIds) {
            schedules.addAll(getDoctorSchedule(doctorId));
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), schedules.size());
        return new PageImpl<>(schedules.subList(start, end), pageable, schedules.size());
    }

    @Override
    public List<DoctorScheduleDTO> findAvailableSchedules(UUID doctorId, LocalDate date, UUID departmentId) {
        int dayOfWeek = date.getDayOfWeek().getValue() + 1;
        List<DoctorWorkSchedule> schedules = scheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
        if (departmentId != null) {
            schedules = schedules.stream()
                    .filter(s -> s.getDepartment() != null && s.getDepartment().getId().equals(departmentId.toString()))
                    .collect(Collectors.toList());
        }
        return schedules.stream()
                .filter(s -> isScheduleAvailable(s, date))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkScheduleConflict(DoctorWorkSchedule schedule) {
        List<DoctorWorkSchedule> existingSchedules = scheduleRepository.findByDoctorIdAndDayOfWeek(
                schedule.getDoctorId(), schedule.getDayOfWeek());
        existingSchedules = existingSchedules.stream()
                .filter(s -> !s.getId().equals(schedule.getId()))
                .collect(Collectors.toList());
        for (DoctorWorkSchedule existing : existingSchedules) {
            if ((schedule.getStartTime().compareTo(existing.getStartTime()) >= 0 && 
                 schedule.getStartTime().compareTo(existing.getEndTime()) < 0) ||
                (schedule.getEndTime().compareTo(existing.getStartTime()) > 0 && 
                 schedule.getEndTime().compareTo(existing.getEndTime()) <= 0) ||
                (schedule.getStartTime().compareTo(existing.getStartTime()) <= 0 && 
                 schedule.getEndTime().compareTo(existing.getEndTime()) >= 0)) {
                if (!isDateRangesOverlap(
                        schedule.getEffectiveFrom(), schedule.getEffectiveTo(), 
                        existing.getEffectiveFrom(), existing.getEffectiveTo())) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public DoctorScheduleDTO getScheduleById(String id) {
        DoctorWorkSchedule schedule = findScheduleById(id);
        return convertToDTO(schedule);
    }

    @Override
    @Transactional
    public DoctorScheduleDTO approveSchedule(String id) {
        DoctorWorkSchedule schedule = findScheduleById(id);
        if ("pending".equals(schedule.getStatus())) {
            schedule.setStatus("active");
            scheduleRepository.save(schedule);
        }
        return convertToDTO(schedule);
    }

    @Override
    @Transactional
    public void updateAppointmentCount(String scheduleId, int change) {
        DoctorWorkSchedule schedule = findScheduleById(scheduleId);
        int currentCount = schedule.getAppointmentCount() != null ? schedule.getAppointmentCount() : 0;
        int newCount = currentCount + change;
        newCount = Math.max(0, newCount);
        if (schedule.getMaxAppointments() != null) {
            newCount = Math.min(newCount, schedule.getMaxAppointments());
        }
        schedule.setAppointmentCount(newCount);
        scheduleRepository.save(schedule);
    }

    private DoctorWorkSchedule findScheduleById(String id) {
        return scheduleRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException("SCHEDULE_NOT_FOUND", "Không tìm thấy lịch làm việc"));
    }

    private DoctorScheduleDTO convertToDTO(DoctorWorkSchedule schedule) {
        User doctor = userRepository.findById(schedule.getDoctorId()).orElse(null);
        Profile profile = doctor != null ? 
                profileRepository.findByUser(doctor).orElse(null) : null;
        DoctorScheduleDTO dto = new DoctorScheduleDTO();
        dto.setScheduleId(schedule.getId());
        dto.setDoctorId(schedule.getDoctorId());
        dto.setDoctorName(doctor != null ? doctor.getFullName() : "Unknown");
        dto.setSpecialty(profile != null ? profile.getSpecialty() : null);
        dto.setDayOfWeek(schedule.getDayOfWeek());
        dto.setDayName(getDayName(schedule.getDayOfWeek()));
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setRoom(schedule.getRoom());
        // dto.setDate(...) nếu có logic ngày cụ thể
        // dto.setAvailable(...) nếu có logic kiểm tra
        return dto;
    }

    private String getDayName(Integer dayOfWeek) {
        if (dayOfWeek == null) return "Unknown";
        return switch (dayOfWeek) {
            case 2 -> "Thứ 2";
            case 3 -> "Thứ 3";
            case 4 -> "Thứ 4";
            case 5 -> "Thứ 5";
            case 6 -> "Thứ 6";
            case 7 -> "Thứ 7";
            case 8 -> "Chủ nhật";
            default -> "Unknown";
        };
    }

    private boolean isScheduleAvailable(DoctorWorkSchedule schedule, LocalDate date) {
        if (!"active".equals(schedule.getStatus())) {
            return false;
        }
        if (schedule.getMaxAppointments() != null && 
            schedule.getAppointmentCount() != null && 
            schedule.getAppointmentCount() >= schedule.getMaxAppointments()) {
            return false;
        }
        if (schedule.getEffectiveFrom() != null && date.isBefore(schedule.getEffectiveFrom())) {
            return false;
        }
        if (schedule.getEffectiveTo() != null && date.isAfter(schedule.getEffectiveTo())) {
            return false;
        }
        return true;
    }

    private boolean isDateRangesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return true;
        }
        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }
} 