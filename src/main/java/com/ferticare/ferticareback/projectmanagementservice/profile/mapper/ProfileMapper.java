package com.ferticare.ferticareback.projectmanagementservice.profile.mapper;

import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.CustomerProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.DoctorProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.ManagerAdminProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;

public class ProfileMapper {

    public static DoctorProfileResponse toDoctorResponse(User user, Profile profile) {
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
    }

    public static CustomerProfileResponse toCustomerResponse(User user, Profile profile) {
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
    }

    public static ManagerAdminProfileResponse toManagerAdminResponse(User user, Profile profile) {
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
    }
}
