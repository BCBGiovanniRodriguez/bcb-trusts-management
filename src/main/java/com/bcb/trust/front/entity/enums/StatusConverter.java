package com.bcb.trust.front.entity.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<StatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StatusEnum attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getIntValue();
    }

    @Override
    public StatusEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(StatusEnum.values())
            .filter(
                se -> se.getIntValue() == dbData
            )
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
