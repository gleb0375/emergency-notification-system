package com.hhnatsiuk.api_contact_adapter_db.mapper;

import com.hhnatsiuk.api_contact_core.domain.entity.UserProfileEntity;
import com.hhnatsiuk.api_contact_if.model.generated.PreferredChannelDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileForAdminResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserProfileUpdateRequestDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class UserProfileMapper {

    /** Entity -> DTO для self-ответов (/profiles/me) */
    public UserProfileResponseDTO toUserProfileResponse(UserProfileEntity e) {
        if (e == null) return null;

        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setSurname(e.getSurname());
        dto.setName(e.getName());
        dto.setEmail(e.getEmail());
        dto.setTelegramUsername(e.getTelegramUsername());
        dto.setPhoneNumber(e.getPhoneNumber());
        dto.setCity(e.getCity());
        dto.setCountryCode(e.getCountryCode());
        dto.setPreferredChannel(toPreferredChannel(e.getPreferredChannel()));
        dto.setCreatedAt(toOffsetUtc(e.getCreatedAt()));
        dto.setUpdatedAt(toOffsetUtc(e.getUpdatedAt()));
        return dto;
    }

    /** Entity -> DTO для админских ответов (/profiles/{profileUuid}) */
    public UserProfileForAdminResponseDTO toAdminResponse(UserProfileEntity e) {
        if (e == null) return null;

        UserProfileForAdminResponseDTO dto = new UserProfileForAdminResponseDTO();
        //
        dto.setId(e.getId());
        dto.setUserProfileUuid(e.getUserProfileUuid());
        dto.setAuthAccountUuid(e.getAuthAccountUuid());
        dto.setSurname(e.getSurname());
        dto.setName(e.getName());
        dto.setEmail(e.getEmail());
        dto.setTelegramUsername(e.getTelegramUsername());
        dto.setPhoneNumber(e.getPhoneNumber());
        dto.setCity(e.getCity());
        dto.setCountryCode(e.getCountryCode());
        dto.setPreferredChannel(toPreferredChannel(e.getPreferredChannel()));
        dto.setCreatedAt(toOffsetUtc(e.getCreatedAt()));
        dto.setUpdatedAt(toOffsetUtc(e.getUpdatedAt()));
        return dto;
    }

    /**
     * Частичное обновление Entity из запроса PATCH.
     * Никакой валидации форматов тут не делаем — это задача слоя @Valid/сервиса.
     */
    public void applyUpdate(UserProfileEntity e, UserProfileUpdateRequestDTO req) {
        if (e == null || req == null) return;

        if (req.getSurname() != null)            e.setSurname(trimToNull(req.getSurname()));
        if (req.getName() != null)               e.setName(trimToNull(req.getName()));
        if (req.getTelegramUsername() != null)   e.setTelegramUsername(trimToNull(req.getTelegramUsername()));
        if (req.getPhoneNumber() != null)        e.setPhoneNumber(trimToNull(req.getPhoneNumber()));
        if (req.getCity() != null)               e.setCity(trimToNull(req.getCity()));
        if (req.getCountryCode() != null)        e.setCountryCode(trimToNull(req.getCountryCode()));
        if (req.getPreferredChannel() != null)   e.setPreferredChannel(fromPreferredChannel(req.getPreferredChannel()));
        // email умышленно не трогаем — пусть меняется через отдельный флоу (верификация), если вообще разрешено
    }

    // ===== Helpers =====

    /** Entity хранит канал как String/enum домена; приводим к API-энуму */
    private PreferredChannelDTO toPreferredChannel(Object value) {
        if (value == null) return null;
        if (value instanceof PreferredChannelDTO pc) return pc;
        String s = String.valueOf(value).toLowerCase();
        return switch (s) {
            case "email"    -> PreferredChannelDTO.EMAIL;
            case "sms"      -> PreferredChannelDTO.SMS;
            case "telegram" -> PreferredChannelDTO.TELEGRAM;
            default         -> null; // или бросить IllegalArgumentException, если хотите строгий мэппинг
        };
    }

    /** Обратно: из API-энима в формат Entity (строка "email|sms|telegram") */
    private String fromPreferredChannel(PreferredChannelDTO pc) {
        if (pc == null) return null;
        return pc.getValue() != null ? pc.getValue() : pc.name().toLowerCase();
    }

    /** Приводим пустые строки к null, чтобы не хранить "" в БД */
    private String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private OffsetDateTime toOffsetUtc(LocalDateTime ldt) {
        return ldt == null ? null : ldt.atOffset(ZoneOffset.UTC);
    }

}
