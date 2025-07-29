package com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingsDTO {
    
    // General Settings
    private GeneralSettings general;
    
    // Security Settings
    private SecuritySettings security;
    
    // Email Settings
    private EmailSettings email;
    
    // Backup Settings
    private BackupSettings backup;
    
    // Maintenance Settings
    private MaintenanceSettings maintenance;
    
    // System Health
    private SystemHealth health;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneralSettings {
        private String systemName;
        private String systemVersion;
        private String timezone;
        private String language;
        private String currency;
        private boolean maintenanceMode;
        private String supportEmail;
        private String supportPhone;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecuritySettings {
        private int passwordMinLength;
        private boolean requireSpecialChar;
        private boolean requireNumber;
        private boolean requireUppercase;
        private int sessionTimeout;
        private boolean twoFactorAuth;
        private int maxLoginAttempts;
        private int lockoutDuration;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailSettings {
        private String smtpHost;
        private int smtpPort;
        private String smtpUsername;
        private boolean smtpSsl;
        private String fromEmail;
        private String fromName;
        private boolean emailNotifications;
        private boolean appointmentReminders;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BackupSettings {
        private boolean autoBackup;
        private String backupFrequency;
        private int retentionDays;
        private String backupLocation;
        private LocalDateTime lastBackup;
        private String backupStatus;
        private long backupSize;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaintenanceSettings {
        private boolean scheduledMaintenance;
        private LocalDateTime nextMaintenance;
        private String maintenanceWindow;
        private String maintenanceMessage;
        private boolean notifyUsers;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemHealth {
        private String status;
        private double cpuUsage;
        private double memoryUsage;
        private double diskUsage;
        private long totalUsers;
        private long activeUsers;
        private long totalDoctors;
        private long totalAppointments;
        private String uptime;
        private LocalDateTime lastRestart;
    }
} 