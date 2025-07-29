package com.ferticare.ferticareback.projectmanagementservice.profile.mapper;

import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.CustomerProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.DoctorProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.profile.response.ManagerAdminProfileResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;

public class ProfileMapper {

    public static DoctorProfileResponse toDoctorResponse(User user, Profile profile) {
        return DoctorProfileResponse.builder()
                // User information (from users table)
                .avatarUrl(user.getAvatarUrl())
                .fullName(user.getFullName())
                .gender(user.getGender().name())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                
                // Doctor professional information (from profile table)
                .specialty(profile.getSpecialty())
                .qualification(profile.getQualification())
                .experienceYears(profile.getExperienceYears())
                .rating(profile.getRating())
                .caseCount(profile.getCaseCount())
                .notes(profile.getNotes())
                .status(profile.getStatus())
                .build();
    }

    public static CustomerProfileResponse toCustomerResponse(User user, Profile profile) {
        return CustomerProfileResponse.builder()
                // User information (from users table)
                .avatarUrl(user.getAvatarUrl())
                .fullName(user.getFullName())
                .gender(user.getGender().name())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                
                // Customer personal information (from profile table)
                .maritalStatus(profile.getMaritalStatus())
                .healthBackground(profile.getHealthBackground())
                .build();
    }

    public static ManagerAdminProfileResponse toManagerAdminResponse(User user, Profile profile) {
        return ManagerAdminProfileResponse.builder()
                // User information (from users table)
                .avatarUrl(user.getAvatarUrl())
                .fullName(user.getFullName())
                .gender(user.getGender().name())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                
                // Work information (from profile table)
                .assignedDepartment(profile.getAssignedDepartment())
                .extraPermissions(profile.getExtraPermissions())
                .build();
    }
}
