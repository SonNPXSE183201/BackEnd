package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class ScheduleCreateRequest {
    
    @NotNull(message = "Bác sĩ ID không được để trống")
    private UUID doctorId;
    
    @NotNull(message = "Ngày trong tuần không được để trống")
    @Min(value = 2, message = "Ngày trong tuần phải từ 2 (Thứ 2) đến 8 (Chủ nhật)")
    @Max(value = 8, message = "Ngày trong tuần phải từ 2 (Thứ 2) đến 8 (Chủ nhật)")
    private Integer dayOfWeek;
    
    @NotNull(message = "Giờ bắt đầu không được để trống")
    private LocalTime startTime;
    
    @NotNull(message = "Giờ kết thúc không được để trống")
    private LocalTime endTime;
    
    @NotNull(message = "Phòng khám không được để trống")
    private String room;
    
    // Khoảng thời gian hiệu lực
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    
    // Số lượng cuộc hẹn tối đa
    private Integer maxAppointments;
    
    // Thông tin phòng ban
    private UUID departmentId;
    
    private String note;
    
    // Trạng thái (mặc định là pending)
    private String status;
} 