package com.ferticare.ferticareback.projectmanagementservice.admin.repository;

import com.ferticare.ferticareback.projectmanagementservice.admin.entity.SystemSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, String> {
    Optional<SystemSetting> findByKey(String key);
    
    List<SystemSetting> findByGroup(String group);
    
    List<SystemSetting> findByIsSystem(boolean isSystem);
    
    Page<SystemSetting> findByKeyContainingIgnoreCase(String keyword, Pageable pageable);
    
    Page<SystemSetting> findByGroupContainingIgnoreCase(String groupName, Pageable pageable);
    
    List<SystemSetting> findByGroupOrderByDisplayOrderAsc(String group);
    
    boolean existsByKey(String key);
} 