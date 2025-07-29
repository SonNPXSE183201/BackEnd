package com.ferticare.ferticareback.projectmanagementservice.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemSetting {

    @Id
    @Column(name = "setting_key", unique = true, nullable = false)
    private String key;
    
    @Column(name = "setting_value", nullable = false, columnDefinition = "TEXT")
    private String value;
    
    @Column(name = "setting_description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "setting_group")
    private String group;
    
    @Column(name = "setting_type")
    private String type;
    
    @Column(name = "is_system")
    private boolean isSystem = false;
    
    @Column(name = "is_encrypted")
    private boolean isEncrypted = false;
    
    @Column(name = "default_value", columnDefinition = "TEXT")
    private String defaultValue;
    
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON string
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
} 