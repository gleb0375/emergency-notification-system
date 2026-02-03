package com.hhnatsiuk.api_contact_service.impl.controller;

import com.hhnatsiuk.api_contact_if.api.generated.ProfilesApi;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileForAdminResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileUpdateRequestDTO;
import com.hhnatsiuk.api_contact_if.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController implements ProfilesApi {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<UserProfileForAdminResponseDTO> adminGetProfileByUuid(String profileUuid) {
        logger.debug("adminGetProfileByUuid called with profileUuid={}", profileUuid);

        UserProfileForAdminResponseDTO userProfileForAdminResponseDTO =
                profileService.getUserProfileByUuid(profileUuid);

        logger.info("Fetched user profile for admin by profileUuid={}", profileUuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userProfileForAdminResponseDTO);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<UserProfileForAdminResponseDTO> adminUpdateProfileByUuid(
            String profileUuid,
            UserProfileUpdateRequestDTO userProfileUpdateRequestDTO
    ) {
        logger.debug("adminUpdateProfileByUuid called with profileUuid={}", profileUuid);

        UserProfileForAdminResponseDTO userProfileForAdminResponseDTO =
                profileService.updateUserProfileByUuid(profileUuid, userProfileUpdateRequestDTO);

        logger.info("Updated user profile for admin by profileUuid={}", profileUuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userProfileForAdminResponseDTO);
    }

    @Override
    public ResponseEntity<UserProfileResponseDTO> getMyProfile() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authAccountUuid = jwt.getClaimAsString("uid");
        logger.debug("getMyProfile called for authAccountUuid={}", authAccountUuid);

        UserProfileResponseDTO userProfileResponseDTO =
                profileService.getMyUserProfile(authAccountUuid);

        logger.info("Fetched current user's profile for authAccountUuid={}", authAccountUuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userProfileResponseDTO);
    }

    @Override
    public ResponseEntity<UserProfileResponseDTO> updateMyProfile(UserProfileUpdateRequestDTO userProfileUpdateRequestDTO) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authAccountUuid = jwt.getClaimAsString("uid");
        logger.debug("updateMyProfile called for authAccountUuid={}", authAccountUuid);

        UserProfileResponseDTO userProfileResponseDTO =
                profileService.updateMyUserProfile(authAccountUuid, userProfileUpdateRequestDTO);

        logger.info("Updated current user's profile for authAccountUuid={}", authAccountUuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userProfileResponseDTO);
    }
}
