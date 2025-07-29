package com.ferticare.ferticareback.projectmanagementservice.usermanagement.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateRequest {
    
    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2-100 ký tự")
    private String fullName;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    
    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Giới tính phải là MALE, FEMALE hoặc OTHER")
    private String gender;
    
    private String address;
    
    @NotBlank(message = "Chuyên khoa không được để trống")
    private String specialization;
    
    // Alternative field name for specialty (same as specialization)
    private String specialty;
    
    @NotBlank(message = "Phòng ban không được để trống")
    private String department;
    
    // ID của phòng ban để liên kết với entity Department
    private String departmentId;
    
    @Min(value = 0, message = "Kinh nghiệm không được âm")
    @Max(value = 50, message = "Kinh nghiệm không được quá 50 năm")
    private Integer experience;
    
    private String avatar;
    
    @DecimalMin(value = "0.0", message = "Lương không được âm")
    private Double salary;
    
    @NotBlank(message = "Loại hợp đồng không được để trống")
    @Pattern(regexp = "^(full-time|part-time|contract)$", message = "Loại hợp đồng không hợp lệ")
    private String contractType;
    
    private LocalDateTime joinDate;
    
    // Schedule information
    private DoctorScheduleRequest schedule;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorScheduleRequest {
        private WeeklyScheduleRequest monday;
        private WeeklyScheduleRequest tuesday;
        private WeeklyScheduleRequest wednesday;
        private WeeklyScheduleRequest thursday;
        private WeeklyScheduleRequest friday;
        private WeeklyScheduleRequest saturday;
        private WeeklyScheduleRequest sunday;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WeeklyScheduleRequest {
            private boolean morning;
            private boolean afternoon;
            private boolean evening;
        }
    }
} 