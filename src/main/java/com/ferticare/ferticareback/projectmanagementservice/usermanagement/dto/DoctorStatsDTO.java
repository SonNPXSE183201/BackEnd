package com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorStatsDTO {
    private long total;           // Tổng số bác sĩ
    private long active;          // Đang hoạt động
    private long inactive;        // Tạm ngưng
    private double avgRating;     // Đánh giá trung bình
    // ✅ Đã bỏ hoàn toàn revenue/doanh thu
} 