package com.hhnatsiuk.api_contact_service.impl.service;

import com.hhnatsiuk.api_contact_adapter_db.mapper.UserProfileMapper;
import com.hhnatsiuk.api_contact_adapter_db.repository.UserProfileRepository;
import com.hhnatsiuk.api_contact_core.domain.entity.UserProfileEntity;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileForAdminResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileUpdateRequestDTO;
import com.hhnatsiuk.api_contact_if.service.ProfileService;
import com.hhnatsiuk.api_contact_service.exception.profile.ProfileNotFoundException;
import com.hhnatsiuk.api_contact_service.exception.validation.EmptyBodyValidationException;
import com.hhnatsiuk.api_contact_service.exception.validation.InvalidUuidValidationException;
import com.hhnatsiuk.api_contact_service.exception.validation.MissingFieldValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public ProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDTO getMyUserProfile(String authAccountUuid) {
        validateAuthAccountUuid(authAccountUuid);

        UserProfileEntity entity = userProfileRepository
                .findByAuthAccountUuid(authAccountUuid)
                .orElseThrow(() -> {
                    logger.warn("User profile not found for authAccountUuid={}", authAccountUuid);
                    return new ProfileNotFoundException("User profile not found");
                });

        logger.info("Fetched user profile for authAccountUuid={}", authAccountUuid);
        return userProfileMapper.toUserProfileResponse(entity);
    }

    @Override
    @Transactional
    public UserProfileResponseDTO updateMyUserProfile(String authAccountUuid, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO) {
        validateAuthAccountUuid(authAccountUuid);
        if (userProfileUpdateRequestDTO == null) {
            logger.warn("Validation failed: request body is null");
            throw new EmptyBodyValidationException();
        }

        UserProfileEntity userProfile = userProfileRepository
                .findByAuthAccountUuid(authAccountUuid)
                .orElseThrow(() -> {
                    logger.warn("User profile not found for authAccountUuid={}", authAccountUuid);
                    return new ProfileNotFoundException("User profile not found");
                });

        userProfileMapper.applyUpdate(userProfile, userProfileUpdateRequestDTO);
        userProfileRepository.save(userProfile);

        logger.info("Updated user profile for authAccountUuid={}", authAccountUuid);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileForAdminResponseDTO getUserProfileByUuid(String userProfileUuid) {
        validateUserProfileUuid(userProfileUuid);

        UserProfileEntity userProfile = userProfileRepository
                .findByUserProfileUuid(userProfileUuid)
                .orElseThrow(() -> {
                    logger.warn("User profile not found for userProfileUuid={}", userProfileUuid);
                    return new ProfileNotFoundException("User profile not found");
                });

        logger.info("Fetched user profile for admin by userProfileUuid={}", userProfileUuid);
        return userProfileMapper.toAdminResponse(userProfile);
    }

    @Override
    @Transactional
    public UserProfileForAdminResponseDTO updateUserProfileByUuid(String userProfileUuid, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO) {
        validateUserProfileUuid(userProfileUuid);
        if (userProfileUpdateRequestDTO == null) {
            logger.warn("Validation failed: request body is null");
            throw new EmptyBodyValidationException();
        }

        UserProfileEntity userProfile = userProfileRepository
                .findByUserProfileUuid(userProfileUuid)
                .orElseThrow(() -> {
                    logger.warn("User profile not found for userProfileUuid={}", userProfileUuid);
                    return new ProfileNotFoundException("User profile not found");
                });

        userProfileMapper.applyUpdate(userProfile, userProfileUpdateRequestDTO);
        userProfileRepository.save(userProfile);

        logger.info("Updated user profile for admin by userProfileUuid={}", userProfileUuid);
        return userProfileMapper.toAdminResponse(userProfile);
    }




    private void validateAuthAccountUuid(String authAccountUuid) {
        if (authAccountUuid == null || authAccountUuid.isBlank()) {
            logger.warn("Validation failed: authAccountUuid is null or blank");
            throw new MissingFieldValidationException("authAccountUuid");
        }
        try {
            UUID.fromString(authAccountUuid);
        } catch (Exception e) {
            logger.warn("Invalid UUID format for authAccountUuid={}", authAccountUuid);
            throw new InvalidUuidValidationException("authAccountUuid", authAccountUuid);
        }
    }

    private void validateUserProfileUuid(String userProfileUuid) {
        if (userProfileUuid == null || userProfileUuid.isBlank()) {
            logger.warn("Validation failed: userProfileUuid is null or blank");
            throw new MissingFieldValidationException("userProfileUuid");
        }
        try {
            UUID.fromString(userProfileUuid);
        } catch (Exception e) {
            logger.warn("Invalid UUID format for userProfileUuid={}", userProfileUuid);
            throw new InvalidUuidValidationException("userProfileUuid", userProfileUuid);
        }
    }
}
