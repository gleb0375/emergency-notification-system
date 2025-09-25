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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
                        new ProfileNotFoundException(
                                HttpStatus.NOT_FOUND.value(), "User profile not found"));

        return userProfileMapper.toUserProfileResponse(entity);
    }

    @Override
    public UserProfileResponseDTO updateMyUserProfile(String authAccountUuid, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO) {
        return null;
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
