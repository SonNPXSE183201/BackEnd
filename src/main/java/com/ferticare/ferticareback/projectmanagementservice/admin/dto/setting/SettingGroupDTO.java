package com.ferticare.ferticareback.projectmanagementservice.admin.dto.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingGroupDTO {
    private String groupId;
    private String groupName;
    private String description;
    private int displayOrder;
    private String icon;
    private boolean isCollapsible;
    private boolean isEditable;
    private List<SystemSettingDTO> settings;
    private Map<String, Object> additionalInfo;
} 