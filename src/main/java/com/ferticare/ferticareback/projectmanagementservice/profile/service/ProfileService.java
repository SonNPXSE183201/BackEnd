package com.ferticare.ferticareback.projectmanagementservice.profile.service;

import com.ferticare.ferticareback.projectmanagementservice.profile.dto.BaseProfileResponse;

import java.util.UUID;

public interface ProfileService {
    BaseProfileResponse getProfileByUserId(UUID userId);
}