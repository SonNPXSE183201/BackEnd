package com.ferticare.ferticareback.projectmanagementservice.profile.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.profile.dto.ManagerAdminProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.dto.BaseProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.dto.CustomerProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.dto.DoctorProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.profile.service.ProfileService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository, RoleRepository roleRepository) {
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public BaseProfileResponse getProfileByUserId(UUID userId) {
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Role role = roleRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = profile.getUser();

        switch (role.getRoleType().toUpperCase()) {
            case "DOCTOR":
                return DoctorProfileResponse.builder()
                        .avatarUrl(user.getAvatarUrl())
                        .fullName(user.getFullName())
                        .gender(user.getGender().name())
                        .dateOfBirth(user.getDateOfBirth())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .specialty(profile.getSpecialty())
                        .qualification(profile.getQualification())
                        .experienceYears(profile.getExperienceYears())
                        .build();

            case "CUSTOMER":
                return CustomerProfileResponse.builder()
                        .avatarUrl(user.getAvatarUrl())
                        .fullName(user.getFullName())
                        .gender(user.getGender().name())
                        .dateOfBirth(user.getDateOfBirth())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .maritalStatus(profile.getMaritalStatus())
                        .healthBackground(profile.getHealthBackground())
                        .build();

            case "MANAGER":
            case "ADMIN":
                return ManagerAdminProfileResponse.builder()
                        .avatarUrl(user.getAvatarUrl())
                        .fullName(user.getFullName())
                        .gender(user.getGender().name())
                        .dateOfBirth(user.getDateOfBirth())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .assignedDepartment(profile.getAssignedDepartment())
                        .extraPermissions(profile.getExtraPermissions())
                        .build();

            default:
                throw new RuntimeException("Unsupported role: " + role.getRoleType());
        }
    }
}