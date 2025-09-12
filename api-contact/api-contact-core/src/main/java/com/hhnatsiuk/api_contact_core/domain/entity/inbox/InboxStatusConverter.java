package com.hhnatsiuk.api_contact_core.domain.entity.inbox;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InboxStatusConverter implements AttributeConverter<InboxStatus, String> {
    @Override
    public String convertToDatabaseColumn(InboxStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }
    @Override
    public InboxStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : InboxStatus.valueOf(dbData.toUpperCase());
    }
}
