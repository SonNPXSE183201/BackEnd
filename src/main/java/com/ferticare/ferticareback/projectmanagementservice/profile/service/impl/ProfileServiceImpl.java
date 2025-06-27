package com.ferticare.ferticareback.projectmanagementservice.profile.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.profile.constant.ProfileConstant;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.mapper.ProfileMapper;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.CustomerProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.DoctorProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.ManagerAdminProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.service.ProfileService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
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
}