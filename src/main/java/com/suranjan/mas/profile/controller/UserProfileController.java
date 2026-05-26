package com.suranjan.mas.profile.controller;

import com.suranjan.mas.profile.dto.UserProfileRequest;
import com.suranjan.mas.profile.dto.UserProfileResponse;
import com.suranjan.mas.profile.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(
            UserProfileService userProfileService
    ) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public UserProfileResponse getProfile() {

        return userProfileService.getProfile();
    }

    @PutMapping
    public String updateProfile(
            @RequestBody
            UserProfileRequest request
    ) {

        return userProfileService
                .saveProfile(request);
    }
}