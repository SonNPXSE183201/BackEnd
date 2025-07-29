package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 🆕 Configuration cho giới hạn lịch hẹn của customer
 * Có thể cấu hình trong application.properties
 */
@Component
@ConfigurationProperties(prefix = "appointment.limit")
@Data
public class AppointmentLimitConfig {
    
    /**
     * Tổng số lịch hẹn tối đa cho mỗi customer
     */
    private int maxAppointmentsPerCustomer = 2;
    
    /**
     * Số lịch hẹn pending tối đa cho mỗi customer
     */
    private int maxPendingAppointments = 1;
    
    /**
     * Số lịch hẹn trong tương lai tối đa cho mỗi customer
     */
    private int maxFutureAppointments = 2;
    
    /**
     * Có bật tính năng giới hạn lịch hẹn không
     */
    private boolean enabled = true;
    
    /**
     * Thời gian chờ (phút) sau khi hủy lịch hẹn mới được đặt lại
     */
    private int cooldownMinutesAfterCancel = 30;
} 