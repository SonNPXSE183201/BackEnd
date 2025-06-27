package com.ferticare.ferticareback.projectmanagementservice.profile.service;

import java.util.UUID;

public interface ProfileService {
    Object getProfileByUserId(UUID userId);
}