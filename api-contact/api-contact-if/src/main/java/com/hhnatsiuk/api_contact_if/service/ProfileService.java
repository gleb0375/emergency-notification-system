package com.hhnatsiuk.api_contact_if.service;

import com.hhnatsiuk.api_contact_if.model.generated.UserProfileForAdminResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileUpdateRequestDTO;

public interface ProfileService {

    UserProfileResponseDTO getMyUserProfile(String authAccountUuid);

    UserProfileResponseDTO updateMyUserProfile(String authAccountUuid, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO);

    UserProfileForAdminResponseDTO getUserProfileByUuid(String profileUuid);

    UserProfileForAdminResponseDTO updateUserProfileByUuid(String profileUuid, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO);
}
