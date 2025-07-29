package com.ferticare.ferticareback.projectmanagementservice.admin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferticare.ferticareback.common.exception.AppException;
import com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting.SettingGroupDTO;
import com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting.SystemSettingDTO;
import com.ferticare.ferticareback.projectmanagementservice.admin.entity.SystemSetting;
import com.ferticare.ferticareback.projectmanagementservice.admin.repository.SystemSettingRepository;
import com.ferticare.ferticareback.projectmanagementservice.admin.service.SystemSettingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

// @PostConstruct  // Tạm thời comment để chạy app sau khi fix database
@Service
@RequiredArgsConstructor
@Slf4j
public class SystemSettingServiceImpl implements SystemSettingService {

    private final SystemSettingRepository systemSettingRepository;
    private final ObjectMapper objectMapper;

    public void init() {
        initializeDefaultSettings();
    }

    @Override
    public SystemSettingDTO getSetting(String key) {
        SystemSetting setting = systemSettingRepository.findByKey(key)
                .orElseThrow(() -> new AppException("SETTING_NOT_FOUND", "Không tìm thấy cài đặt với khóa: " + key));
        return convertToDTO(setting);
    }

    @Override
    public List<SystemSettingDTO> getSettingsByGroup(String group) {
        return systemSettingRepository.findByGroupOrderByDisplayOrderAsc(group).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SettingGroupDTO> getAllSettingGroups() {
        // Lấy danh sách cài đặt và nhóm theo group
        Map<String, List<SystemSetting>> settingsByGroup = systemSettingRepository.findAll().stream()
                .collect(Collectors.groupingBy(SystemSetting::getGroup));
        
        List<SettingGroupDTO> groups = new ArrayList<>();
        
        // Chuyển đổi từng nhóm thành DTO
        settingsByGroup.forEach((groupName, settings) -> {
            if (groupName != null && !groupName.isEmpty()) {
                SettingGroupDTO group = SettingGroupDTO.builder()
                        .groupId(groupName)
                        .groupName(formatGroupName(groupName))
                        .description("Nhóm cài đặt " + formatGroupName(groupName))
                        .displayOrder(getDisplayOrderForGroup(groupName))
                        .isCollapsible(true)
                        .isEditable(isGroupEditable(groupName))
                        .settings(settings.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList()))
                        .build();
                
                groups.add(group);
            }
        });
        
        // Sắp xếp nhóm theo displayOrder
        groups.sort(Comparator.comparingInt(SettingGroupDTO::getDisplayOrder));
        
        return groups;
    }

    @Override
    @Transactional
    public SystemSettingDTO saveSetting(SystemSettingDTO dto) {
        SystemSetting setting = systemSettingRepository.findByKey(dto.getKey())
                .orElse(new SystemSetting());
                
        setting.setKey(dto.getKey());
        setting.setValue(convertValueToString(dto.getValue()));
        setting.setDescription(dto.getDescription());
        setting.setGroup(dto.getGroup());
        setting.setType(dto.getType());
        setting.setSystem(dto.isSystem());
        setting.setEncrypted(dto.isEncrypted());
        
        // Lưu metadata nếu có
        if (dto.getMetadata() != null) {
            try {
                setting.setMetadata(objectMapper.writeValueAsString(dto.getMetadata()));
            } catch (JsonProcessingException e) {
                log.error("Error serializing metadata for setting {}: {}", dto.getKey(), e.getMessage());
                setting.setMetadata("{}");
            }
        } else {
            setting.setMetadata("{}");
        }
        
        SystemSetting saved = systemSettingRepository.save(setting);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public SystemSettingDTO updateSetting(String key, SystemSettingDTO dto) {
        SystemSetting setting = systemSettingRepository.findByKey(key)
                .orElseThrow(() -> new AppException("SETTING_NOT_FOUND", "Không tìm thấy cài đặt với khóa: " + key));
                
        // Chỉ cập nhật giá trị và mô tả, không thay đổi key
        setting.setValue(convertValueToString(dto.getValue()));
        if (dto.getDescription() != null) {
            setting.setDescription(dto.getDescription());
        }
        
        // Lưu metadata nếu có
        if (dto.getMetadata() != null) {
            try {
                setting.setMetadata(objectMapper.writeValueAsString(dto.getMetadata()));
            } catch (JsonProcessingException e) {
                log.error("Error serializing metadata for setting {}: {}", key, e.getMessage());
            }
        }
        
        SystemSetting saved = systemSettingRepository.save(setting);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public List<SystemSettingDTO> saveSettings(List<SystemSettingDTO> settings) {
        List<SystemSettingDTO> savedSettings = new ArrayList<>();
        for (SystemSettingDTO dto : settings) {
            savedSettings.add(saveSetting(dto));
        }
        return savedSettings;
    }

    @Override
    @Transactional
    public void deleteSetting(String key) {
        SystemSetting setting = systemSettingRepository.findByKey(key)
                .orElseThrow(() -> new AppException("SETTING_NOT_FOUND", "Không tìm thấy cài đặt với khóa: " + key));
                
        if (setting.isSystem()) {
            throw new AppException("CANNOT_DELETE_SYSTEM_SETTING", "Không thể xóa cài đặt hệ thống");
        }
        
        systemSettingRepository.delete(setting);
    }

    @Override
    public Page<SystemSettingDTO> searchSettings(String keyword, Pageable pageable) {
        return systemSettingRepository.findByKeyContainingIgnoreCase(keyword, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public void resetToDefault(String key) {
        SystemSetting setting = systemSettingRepository.findByKey(key)
                .orElseThrow(() -> new AppException("SETTING_NOT_FOUND", "Không tìm thấy cài đặt với khóa: " + key));
        
        if (setting.getDefaultValue() != null) {
            setting.setValue(setting.getDefaultValue());
            systemSettingRepository.save(setting);
        }
    }

    @Override
    @Transactional
    public void resetAllToDefault() {
        List<SystemSetting> settings = systemSettingRepository.findAll();
        for (SystemSetting setting : settings) {
            if (setting.getDefaultValue() != null) {
                setting.setValue(setting.getDefaultValue());
            }
        }
        systemSettingRepository.saveAll(settings);
    }

    @Override
    public <T> T getSettingValue(String key, Class<T> type) {
        SystemSetting setting = systemSettingRepository.findByKey(key)
                .orElseThrow(() -> new AppException("SETTING_NOT_FOUND", "Không tìm thấy cài đặt với khóa: " + key));
        
        try {
            if (type == String.class) {
                return type.cast(setting.getValue());
            } else if (type == Boolean.class) {
                return type.cast(Boolean.valueOf(setting.getValue()));
            } else if (type == Integer.class) {
                return type.cast(Integer.valueOf(setting.getValue()));
            } else if (type == Double.class) {
                return type.cast(Double.valueOf(setting.getValue()));
            } else if (type == Long.class) {
                return type.cast(Long.valueOf(setting.getValue()));
            } else {
                // Cho các loại phức tạp hơn, sử dụng Jackson
                return objectMapper.readValue(setting.getValue(), type);
            }
        } catch (Exception e) {
            log.error("Error converting setting value for key {}: {}", key, e.getMessage());
            throw new AppException("SETTING_VALUE_CONVERSION_ERROR", 
                    "Lỗi chuyển đổi giá trị cài đặt: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getMultipleSettings(List<String> keys) {
        Map<String, Object> result = new HashMap<>();
        
        for (String key : keys) {
            SystemSetting setting = systemSettingRepository.findByKey(key)
                    .orElse(null);
            
            if (setting != null) {
                result.put(key, parseSettingValue(setting));
            }
        }
        
        return result;
    }

    @Override
    public boolean exists(String key) {
        return systemSettingRepository.existsByKey(key);
    }

    // Helper methods

    private SystemSettingDTO convertToDTO(SystemSetting entity) {
        Map<String, Object> metadata = new HashMap<>();
        
        if (entity.getMetadata() != null && !entity.getMetadata().isEmpty()) {
            try {
                metadata = objectMapper.readValue(entity.getMetadata(), Map.class);
            } catch (JsonProcessingException e) {
                log.error("Error deserializing metadata for setting {}: {}", entity.getKey(), e.getMessage());
            }
        }
        
        return SystemSettingDTO.builder()
                .key(entity.getKey())
                .value(parseSettingValue(entity))
                .description(entity.getDescription())
                .group(entity.getGroup())
                .type(entity.getType())
                .isSystem(entity.isSystem())
                .isEncrypted(entity.isEncrypted())
                .metadata(metadata)
                .build();
    }

    private Object parseSettingValue(SystemSetting setting) {
        if (setting.getValue() == null) {
            return null;
        }
        
        try {
            switch (setting.getType() == null ? "STRING" : setting.getType().toUpperCase()) {
                case "BOOLEAN":
                    return Boolean.valueOf(setting.getValue());
                case "NUMBER":
                case "INTEGER":
                    try {
                        return Integer.valueOf(setting.getValue());
                    } catch (NumberFormatException e) {
                        return Double.valueOf(setting.getValue());
                    }
                case "JSON":
                case "OBJECT":
                case "ARRAY":
                    return objectMapper.readValue(setting.getValue(), Object.class);
                case "STRING":
                default:
                    return setting.getValue();
            }
        } catch (Exception e) {
            log.warn("Failed to parse setting value for key {}: {}", setting.getKey(), e.getMessage());
            return setting.getValue(); // Return as string if parsing fails
        }
    }

    private String convertValueToString(Object value) {
        if (value == null) {
            return "";
        }
        
        if (value instanceof String) {
            return (String) value;
        }
        
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Error serializing value: {}", e.getMessage());
            return value.toString();
        }
    }

    private String formatGroupName(String group) {
        if (group == null || group.isEmpty()) {
            return "Mặc định";
        }
        
        // Chuyển đổi snake_case hoặc camelCase thành tiếng Việt có nghĩa
        String formatted = group;
        
        // Xử lý các trường hợp đặc biệt
        switch (group.toLowerCase()) {
            case "general":
            case "general_settings":
                return "Cài đặt chung";
            case "email":
            case "email_settings":
                return "Cài đặt email";
            case "notification":
            case "notifications":
                return "Thông báo";
            case "security":
                return "Bảo mật";
            case "appearance":
                return "Giao diện";
            case "system":
                return "Hệ thống";
            case "appointment":
            case "appointments":
                return "Lịch hẹn";
            default:
                // Xử lý camelCase/snake_case
                formatted = formatted.replaceAll("_", " ")
                        .replaceAll("([a-z])([A-Z])", "$1 $2");
                return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
        }
    }

    private int getDisplayOrderForGroup(String group) {
        // Xác định thứ tự hiển thị cho các nhóm cài đặt
        switch (group.toLowerCase()) {
            case "general":
            case "general_settings":
                return 1;
            case "appearance":
                return 2;
            case "email":
            case "email_settings":
                return 3;
            case "notification":
            case "notifications":
                return 4;
            case "appointment":
            case "appointments":
                return 5;
            case "security":
                return 6;
            case "system":
                return 100; // Luôn hiển thị cuối cùng
            default:
                return 50; // Mặc định giữa
        }
    }

    private boolean isGroupEditable(String group) {
        // Xác định nhóm có thể chỉnh sửa hay không
        if (group.toLowerCase().equals("system")) {
            return false; // Không cho phép chỉnh sửa nhóm system
        }
        return true;
    }

    private void initializeDefaultSettings() {
        // Khởi tạo các cài đặt mặc định nếu chưa có trong DB
        List<SystemSetting> defaultSettings = createDefaultSettings();
        
        for (SystemSetting defaultSetting : defaultSettings) {
            if (!systemSettingRepository.existsByKey(defaultSetting.getKey())) {
                systemSettingRepository.save(defaultSetting);
                log.info("Created default setting: {}", defaultSetting.getKey());
            }
        }
    }

    private List<SystemSetting> createDefaultSettings() {
        List<SystemSetting> defaults = new ArrayList<>();
        
        // Cài đặt chung
        defaults.add(createSetting(
            "site.title", "FertiCare - Trung tâm hỗ trợ sinh sản", "Tiêu đề trang web", 
            "general", "STRING", true, false, 1
        ));
        
        defaults.add(createSetting(
            "site.description", "Chuyên hỗ trợ và điều trị vô sinh hiếm muộn", "Mô tả trang web", 
            "general", "STRING", true, false, 2
        ));
        
        defaults.add(createSetting(
            "site.contact.email", "contact@ferticare.com", "Email liên hệ", 
            "general", "STRING", true, false, 3
        ));
        
        defaults.add(createSetting(
            "site.contact.phone", "+84 123 456 789", "Số điện thoại liên hệ", 
            "general", "STRING", true, false, 4
        ));
        
        // Cài đặt giao diện
        defaults.add(createSetting(
            "appearance.theme", "light", "Chủ đề mặc định (light/dark)", 
            "appearance", "STRING", false, false, 1
        ));
        
        defaults.add(createSetting(
            "appearance.primary.color", "#3498db", "Màu chủ đạo", 
            "appearance", "STRING", false, false, 2
        ));
        
        // Cài đặt email
        defaults.add(createSetting(
            "email.sender", "noreply@ferticare.com", "Địa chỉ email gửi thư", 
            "email", "STRING", true, false, 1
        ));
        
        defaults.add(createSetting(
            "email.sender.name", "FertiCare System", "Tên hiển thị người gửi email", 
            "email", "STRING", true, false, 2
        ));
        
        // Cài đặt thông báo
        defaults.add(createSetting(
            "notification.appointment.reminder", "true", "Gửi thông báo nhắc lịch hẹn", 
            "notification", "BOOLEAN", false, false, 1
        ));
        
        defaults.add(createSetting(
            "notification.reminder.hours", "24", "Số giờ gửi nhắc trước lịch hẹn", 
            "notification", "NUMBER", false, false, 2
        ));
        
        // Cài đặt lịch hẹn
        defaults.add(createSetting(
            "appointment.default.duration", "30", "Thời lượng mặc định của buổi hẹn (phút)", 
            "appointment", "NUMBER", false, false, 1
        ));
        
        defaults.add(createSetting(
            "appointment.slots.interval", "15", "Khoảng thời gian giữa các slot lịch hẹn (phút)", 
            "appointment", "NUMBER", false, false, 2
        ));
        
        defaults.add(createSetting(
            "appointment.max.per.day", "12", "Số lượng lịch hẹn tối đa mỗi ngày cho một bác sĩ", 
            "appointment", "NUMBER", false, false, 3
        ));
        
        // Cài đặt hệ thống
        defaults.add(createSetting(
            "system.maintenance.mode", "false", "Chế độ bảo trì", 
            "system", "BOOLEAN", true, false, 1
        ));
        
        defaults.add(createSetting(
            "system.version", "1.0.0", "Phiên bản hệ thống", 
            "system", "STRING", true, false, 2
        ));
        
        return defaults;
    }

    private SystemSetting createSetting(String key, String value, String description, 
                                       String group, String type, boolean isSystem, 
                                       boolean isEncrypted, int displayOrder) {
        SystemSetting setting = new SystemSetting();
        setting.setKey(key);
        setting.setValue(value);
        setting.setDefaultValue(value);
        setting.setDescription(description);
        setting.setGroup(group);
        setting.setType(type);
        setting.setSystem(isSystem);
        setting.setEncrypted(isEncrypted);
        setting.setDisplayOrder(displayOrder);
        setting.setMetadata("{}");
        return setting;
    }
} 