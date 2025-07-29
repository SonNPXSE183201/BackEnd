package com.ferticare.ferticareback.projectmanagementservice.admin.service;

import com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting.SettingGroupDTO;
import com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting.SystemSettingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SystemSettingService {
    /**
     * Lấy cài đặt theo key
     */
    SystemSettingDTO getSetting(String key);
    
    /**
     * Lấy danh sách cài đặt theo nhóm
     */
    List<SystemSettingDTO> getSettingsByGroup(String group);
    
    /**
     * Lấy tất cả các nhóm cài đặt
     */
    List<SettingGroupDTO> getAllSettingGroups();
    
    /**
     * Lưu/cập nhật cài đặt
     */
    SystemSettingDTO saveSetting(SystemSettingDTO setting);
    
    /**
     * Cập nhật cài đặt theo key
     */
    SystemSettingDTO updateSetting(String key, SystemSettingDTO setting);
    
    /**
     * Lưu/cập nhật nhiều cài đặt cùng lúc
     */
    List<SystemSettingDTO> saveSettings(List<SystemSettingDTO> settings);
    
    /**
     * Xóa cài đặt
     */
    void deleteSetting(String key);
    
    /**
     * Tìm kiếm cài đặt
     */
    Page<SystemSettingDTO> searchSettings(String keyword, Pageable pageable);
    
    /**
     * Khôi phục cài đặt mặc định
     */
    void resetToDefault(String key);
    
    /**
     * Khôi phục tất cả cài đặt mặc định
     */
    void resetAllToDefault();
    
    /**
     * Lấy giá trị cài đặt dưới dạng kiểu dữ liệu cụ thể
     */
    <T> T getSettingValue(String key, Class<T> type);
    
    /**
     * Lấy nhiều giá trị cài đặt cùng lúc
     */
    Map<String, Object> getMultipleSettings(List<String> keys);
    
    /**
     * Kiểm tra xem key có tồn tại
     */
    boolean exists(String key);
} 