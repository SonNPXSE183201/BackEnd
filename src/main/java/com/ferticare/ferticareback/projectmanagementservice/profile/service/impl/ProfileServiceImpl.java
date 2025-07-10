package com.ferticare.ferticareback.projectmanagementservice.profile.service.impl;

import com.ferticare.ferticareback.common.service.FileService;
import com.ferticare.ferticareback.projectmanagementservice.profile.constant.ProfileConstant;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.mapper.ProfileMapper;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateCustomerProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateDoctorProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.request.UpdateManagerAdminProfileRequest;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.CustomerProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.DoctorProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.ManagerAdminProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.service.ProfileService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.Gender;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public ProfileServiceImpl(ProfileRepository profileRepository, 
                            RoleRepository roleRepository,
                            UserRepository userRepository,
                            FileService fileService) {
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Override
    public Object getProfileByUserId(UUID userId) {
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException(ProfileConstant.PROFILE_NOT_FOUND));

        Role role = roleRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = profile.getUser();

        return switch (role.getRoleType().toUpperCase()) {
            case "DOCTOR" -> ProfileMapper.toDoctorResponse(user, profile);
            case "CUSTOMER" -> ProfileMapper.toCustomerResponse(user, profile);
            case "MANAGER", "ADMIN" -> ProfileMapper.toManagerAdminResponse(user, profile);
            default -> throw new RuntimeException("Unsupported role: " + role.getRoleType());
        };
    }

    @Override
    public DoctorProfileResponse getDoctorProfile(UUID userId) {
        validateUserRole(userId, "DOCTOR");
        
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException(ProfileConstant.PROFILE_NOT_FOUND));
        
        User user = profile.getUser();
        return ProfileMapper.toDoctorResponse(user, profile);
    }

    @Override
    public CustomerProfileResponse getCustomerProfile(UUID userId) {
        validateUserRole(userId, "CUSTOMER");
        
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException(ProfileConstant.PROFILE_NOT_FOUND));
        
        User user = profile.getUser();
        return ProfileMapper.toCustomerResponse(user, profile);
    }

    @Override
    public ManagerAdminProfileResponse getManagerAdminProfile(UUID userId) {
        validateUserRole(userId, "MANAGER", "ADMIN");
        
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException(ProfileConstant.PROFILE_NOT_FOUND));
        
        User user = profile.getUser();
        return ProfileMapper.toManagerAdminResponse(user, profile);
    }

    @Override
    public DoctorProfileResponse updateDoctorProfile(UUID userId, UpdateDoctorProfileRequest request) {
        validateUserRole(userId, "DOCTOR");
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException(ProfileConstant.PROFILE_NOT_FOUND));

        // Update user fields
        updateCommonUserFields(user, request.getFullName(), request.getGender(), 
                              request.getDateOfBirth(), request.getPhone(), request.getAddress());

        // Update doctor-specific profile fields
        if (StringUtils.hasText(request.getSpecialty())) {
            profile.setSpecialty(request.getSpecialty());
        }
        if (StringUtils.hasText(request.getQualification())) {
            profile.setQualification(request.getQualification());
        }
        if (request.getExperienceYears() != null) {
            profile.setExperienceYears(request.getExperienceYears());
        }
        if (StringUtils.hasText(request.getNotes())) {
            profile.setNotes(request.getNotes());
        }
        if (StringUtils.hasText(request.getStatus())) {
            profile.setStatus(request.getStatus());
        }

        // Save entities
        userRepository.save(user);
        profileRepository.save(profile);

        return ProfileMapper.toDoctorResponse(user, profile);
    }

    @Override
    public CustomerProfileResponse updateCustomerProfile(UUID userId, UpdateCustomerProfileRequest request) {
        validateUserRole(userId, "CUSTOMER");
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException(ProfileConstant.PROFILE_NOT_FOUND));

        // Update user fields
        updateCommonUserFields(user, request.getFullName(), request.getGender(), 
                              request.getDateOfBirth(), request.getPhone(), request.getAddress());

        // Update customer-specific profile fields
        if (StringUtils.hasText(request.getMaritalStatus())) {
            profile.setMaritalStatus(request.getMaritalStatus());
        }
        if (StringUtils.hasText(request.getHealthBackground())) {
            profile.setHealthBackground(request.getHealthBackground());
        }

        // Save entities
        userRepository.save(user);
        profileRepository.save(profile);

        return ProfileMapper.toCustomerResponse(user, profile);
    }

    @Override
    public ManagerAdminProfileResponse updateManagerAdminProfile(UUID userId, UpdateManagerAdminProfileRequest request) {
        validateUserRole(userId, "MANAGER", "ADMIN");
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException(ProfileConstant.PROFILE_NOT_FOUND));

        // Update user fields
        updateCommonUserFields(user, request.getFullName(), request.getGender(), 
                              request.getDateOfBirth(), request.getPhone(), request.getAddress());

        // Update manager/admin-specific profile fields
        if (StringUtils.hasText(request.getAssignedDepartment())) {
            profile.setAssignedDepartment(request.getAssignedDepartment());
        }
        if (StringUtils.hasText(request.getExtraPermissions())) {
            profile.setExtraPermissions(request.getExtraPermissions());
        }

        // Save entities
        userRepository.save(user);
        profileRepository.save(profile);

        return ProfileMapper.toManagerAdminResponse(user, profile);
    }

    @Override
    public String updateAvatar(UUID userId, MultipartFile avatarFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete old avatar if exists
        if (StringUtils.hasText(user.getAvatarUrl())) {
            fileService.deleteFile(user.getAvatarUrl());
        }

        // Upload new avatar
        String avatarUrl = fileService.uploadFile(avatarFile, "avatars");
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);

        return avatarUrl;
    }

    // ==================== HELPER METHODS ====================
    
    private void validateUserRole(UUID userId, String... allowedRoles) {
        Role role = roleRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        String userRole = role.getRoleType().toUpperCase();
        for (String allowedRole : allowedRoles) {
            if (userRole.equals(allowedRole.toUpperCase())) {
                return;
            }
        }
        
        throw new RuntimeException("Access denied. Required role: " + String.join(" or ", allowedRoles));
    }
    
    private void updateCommonUserFields(User user, String fullName, String gender, 
                                       java.time.LocalDate dateOfBirth, String phone, String address) {
        if (StringUtils.hasText(fullName)) {
            user.setFullName(fullName);
        }
        if (StringUtils.hasText(gender)) {
            user.setGender(Gender.valueOf(gender.toUpperCase()));
        }
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth);
        }
        if (StringUtils.hasText(phone)) {
            user.setPhone(phone);
        }
        if (StringUtils.hasText(address)) {
            user.setAddress(address);
        }
    }
}