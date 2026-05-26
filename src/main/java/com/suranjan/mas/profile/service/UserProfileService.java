package com.suranjan.mas.profile.service;

import com.suranjan.mas.auth.entity.User;
import com.suranjan.mas.auth.repository.UserRepository;
import com.suranjan.mas.profile.dto.UserProfileRequest;
import com.suranjan.mas.profile.dto.UserProfileResponse;
import com.suranjan.mas.profile.entity.UserProfile;
import com.suranjan.mas.profile.repository.UserProfileRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(
            UserProfileRepository userProfileRepository,
            UserRepository userRepository
    ) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileResponse getProfile() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        UserProfile profile =
                userProfileRepository
                        .findByUser(user)
                        .orElse(null);

        if (profile == null) {
            return new UserProfileResponse();
        }

        UserProfileResponse response =
                new UserProfileResponse();

        response.setName(profile.getName());
        response.setDob(profile.getDob());
        response.setGender(profile.getGender());

        return response;
    }

    public String saveProfile(
            UserProfileRequest request
    ) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        UserProfile profile =
                userProfileRepository
                        .findByUser(user)
                        .orElse(new UserProfile());

        profile.setName(request.getName());
        profile.setDob(request.getDob());
        profile.setGender(request.getGender());
        profile.setUser(user);

        userProfileRepository.save(profile);

        return "Profile updated successfully";
    }
}