package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDTO {
    private UUID scheduleId;
    private UUID doctorId;
    private String doctorName;
    private String specialty;
    private Integer dayOfWeek;
    private String dayName; // Thứ 2, Thứ 3, ...
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private LocalDate date; // Ngày cụ thể (nếu có)
    private boolean isAvailable;
} 