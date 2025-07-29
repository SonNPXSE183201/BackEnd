package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStatusUpdateRequest {
    
    @NotBlank(message = "Trạng thái không được để trống")
    private String status; // 'scheduled', 'completed', 'cancelled', 'no-show'
    
    private String notes; // Ghi chú khi cập nhật trạng thái
    
    private LocalDateTime completedAt; // Thời gian hoàn thành (nếu status = 'completed')
} 