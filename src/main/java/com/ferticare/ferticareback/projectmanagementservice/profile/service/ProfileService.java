package com.ferticare.ferticareback.projectmanagementservice.profile.service;

import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateCustomerProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateDoctorProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateManagerAdminProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.CustomerProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.DoctorProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.ManagerAdminProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProfileService {
    
    // ==================== GET PROFILE BY ROLE ====================
    Object getProfileByUserId(UUID userId);
    DoctorProfileResponse getDoctorProfile(UUID userId);
    CustomerProfileResponse getCustomerProfile(UUID userId);
    ManagerAdminProfileResponse getManagerAdminProfile(UUID userId);
    
    // ==================== UPDATE PROFILE BY ROLE ====================
    DoctorProfileResponse updateDoctorProfile(UUID userId, UpdateDoctorProfileRequest request);
    CustomerProfileResponse updateCustomerProfile(UUID userId, UpdateCustomerProfileRequest request);
    ManagerAdminProfileResponse updateManagerAdminProfile(UUID userId, UpdateManagerAdminProfileRequest request);
    
    // ==================== COMMON OPERATIONS ====================
    String updateAvatar(UUID userId, MultipartFile avatarFile);
}