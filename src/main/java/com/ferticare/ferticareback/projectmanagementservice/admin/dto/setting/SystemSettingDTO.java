package com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingDTO {
    @NotBlank(message = "Mã cài đặt không được để trống")
    private String key;
    
    @NotNull(message = "Giá trị cài đặt không được để trống")
    private Object value;
    
    private String description;
    
    private String group;
    
    private String type; // STRING, NUMBER, BOOLEAN, JSON, etc.
    
    private boolean isSystem; // Cài đặt hệ thống hay cài đặt người dùng
    
    private boolean isEncrypted; // Dữ liệu có được mã hóa không
    
    // Metadata cho UI
    private Map<String, Object> metadata;
} 