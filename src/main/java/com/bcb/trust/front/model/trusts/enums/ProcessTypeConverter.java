package com.bcb.trust.front.model.trusts.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProcessTypeConverter implements AttributeConverter<ProcessTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProcessTypeEnum attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getIntValue();
    }

    @Override
    public ProcessTypeEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(ProcessTypeEnum.values())
            .filter(
                pte -> pte.getIntValue() == dbData
            ).findFirst().orElseThrow(IllegalArgumentException::new);
    }

}
