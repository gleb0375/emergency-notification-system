package com.hhnatsiuk.api_contact_service.impl.service;

import com.hhnatsiuk.api_contact_adapter_db.mapper.UserProfileMapper;
import com.hhnatsiuk.api_contact_adapter_db.repository.UserProfileRepository;
import com.hhnatsiuk.api_contact_core.domain.entity.UserProfileEntity;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileForAdminResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileUpdateRequestDTO;
import com.hhnatsiuk.api_contact_if.service.ProfileService;
import com.hhnatsiuk.api_contact_service.exception.global.ValidationException;
import com.hhnatsiuk.api_contact_service.exception.profile.ProfileNotFoundException;
import com.hhnatsiuk.api_contact_service.exception.validation.EmptyBodyValidationException;
import com.hhnatsiuk.api_contact_service.exception.validation.InvalidUuidValidationException;
import com.hhnatsiuk.api_contact_service.exception.validation.MissingFieldValidationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public ProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public UserProfileResponseDTO getMyUserProfile(String authAccountUuid) {
        if (authAccountUuid == null || authAccountUuid.isBlank()) {
            throw new ValidationException("authAccountUuid must be provided");
        }

        UserProfileEntity entity = userProfileRepository
                .findByAuthAccountUuid(authAccountUuid)
                .orElseThrow(() ->
                        new ProfileNotFoundException("User profile not found"));

        return userProfileMapper.toUserProfileResponse(entity);
    }

    @Override
    public UserProfileResponseDTO updateMyUserProfile(String authAccountUuid,
                                                      UserProfileUpdateRequestDTO userProfileUpdateRequestDTO) {
        if (authAccountUuid == null || authAccountUuid.isBlank()) {
            throw new MissingFieldValidationException("authAccountUuid");
        }
        try {
            UUID.fromString(authAccountUuid);
        } catch (Exception e) {
            throw new InvalidUuidValidationException("authAccountUuid", authAccountUuid);
        }
        if (userProfileUpdateRequestDTO == null) {
            throw new EmptyBodyValidationException();
        }

        UserProfileEntity userProfile = userProfileRepository
                .findByAuthAccountUuid(authAccountUuid)
                .orElseThrow(() -> new ProfileNotFoundException("User profile not found"));

        userProfileMapper.applyUpdate(userProfile, userProfileUpdateRequestDTO);
        userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileForAdminResponseDTO getUserProfileByUuid(String profileUuid) {
        return null;
    }

    @Override
    public UserProfileForAdminResponseDTO updateUserProfileByUuid(String profileUuid, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO) {
        return null;
    }
}
