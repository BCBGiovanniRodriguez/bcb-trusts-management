package com.bcb.trust.front.model.trusts.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProcessStateConverter implements AttributeConverter<ProcessStateEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProcessStateEnum attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getIntValue();
    }

    @Override
    public ProcessStateEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(ProcessStateEnum.values())
            .filter(
                pse -> pse.getIntValue() == dbData
            ).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
