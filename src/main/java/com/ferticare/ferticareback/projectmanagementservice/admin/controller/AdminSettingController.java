package com.ferticare.ferticareback.projectmanagementservice.admin.controller;

import com.ferticare.ferticareback.common.dto.GenericResponse;
import com.ferticare.ferticareback.common.dto.MessageDTO;
import com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting.SettingGroupDTO;
import com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting.SystemSettingDTO;
import com.ferticare.ferticareback.projectmanagementservice.admin.service.SystemSettingService;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.AdminOnly;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
@Tag(name = "Admin Settings", description = "APIs for system settings management")
public class AdminSettingController {

    private final SystemSettingService systemSettingService;

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{key}")
    @Operation(summary = "Get setting by key")
    public ResponseEntity<?> getSettingByKey(@PathVariable String key) {
        try {
            SystemSettingDTO setting = systemSettingService.getSetting(key);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy cài đặt thành công"), 
                    null, setting));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/group/{group}")
    @Operation(summary = "Get settings by group")
    public ResponseEntity<?> getSettingsByGroup(@PathVariable String group) {
        try {
            List<SystemSettingDTO> settings = systemSettingService.getSettingsByGroup(group);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy danh sách cài đặt theo nhóm thành công"), 
                    null, settings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/groups")
    @Operation(summary = "Get all setting groups")
    public ResponseEntity<?> getAllSettingGroups() {
        try {
            List<SettingGroupDTO> groups = systemSettingService.getAllSettingGroups();
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy danh sách nhóm cài đặt thành công"), 
                    null, groups));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @Operation(summary = "Save setting")
    public ResponseEntity<?> saveSetting(@Valid @RequestBody SystemSettingDTO setting) {
        try {
            SystemSettingDTO savedSetting = systemSettingService.saveSetting(setting);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lưu cài đặt thành công"), 
                    null, savedSetting));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{settingId}")
    @Operation(summary = "Update setting")
    public ResponseEntity<?> updateSetting(@PathVariable String settingId, 
                                          @Valid @RequestBody SystemSettingDTO setting) {
        try {
            SystemSettingDTO updatedSetting = systemSettingService.updateSetting(settingId, setting);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Cập nhật cài đặt thành công"), 
                    null, updatedSetting));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/batch")
    @Operation(summary = "Save multiple settings")
    public ResponseEntity<?> saveSettings(@Valid @RequestBody List<SystemSettingDTO> settings) {
        try {
            List<SystemSettingDTO> savedSettings = systemSettingService.saveSettings(settings);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lưu danh sách cài đặt thành công"), 
                    null, savedSettings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{key}")
    @Operation(summary = "Delete setting")
    public ResponseEntity<?> deleteSetting(@PathVariable String key) {
        try {
            systemSettingService.deleteSetting(key);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Xóa cài đặt thành công"), 
                    null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search")
    @Operation(summary = "Search settings")
    public ResponseEntity<?> searchSettings(@RequestParam String keyword, Pageable pageable) {
        try {
            Page<SystemSettingDTO> settings = systemSettingService.searchSettings(keyword, pageable);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Tìm kiếm cài đặt thành công"), 
                    null, settings));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/reset/{key}")
    @Operation(summary = "Reset setting to default")
    public ResponseEntity<?> resetToDefault(@PathVariable String key) {
        try {
            systemSettingService.resetToDefault(key);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Khôi phục cài đặt mặc định thành công"), 
                    null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @AdminOnly
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/reset-all")
    @Operation(summary = "Reset all settings to default")
    public ResponseEntity<?> resetAllToDefault() {
        try {
            systemSettingService.resetAllToDefault();
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Khôi phục tất cả cài đặt mặc định thành công"), 
                    null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }

    @GetMapping("/values")
    @Operation(summary = "Get multiple settings' values")
    public ResponseEntity<?> getMultipleSettings(@RequestParam List<String> keys) {
        try {
            Map<String, Object> values = systemSettingService.getMultipleSettings(keys);
            return ResponseEntity.ok(new GenericResponse<>(true, 
                    new MessageDTO("SUCCESS", "Lấy danh sách giá trị cài đặt thành công"), 
                    null, values));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>(false, 
                        new MessageDTO("ERROR", "Lỗi: " + e.getMessage()),
                        null, null));
        }
    }
}