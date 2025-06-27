package com.ferticare.ferticareback.projectmanagementservice.profile.controller;

import com.ferticare.ferticareback.projectmanagementservice.profile.response.*;
import com.ferticare.ferticareback.projectmanagementservice.profile.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        Object response = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }
}