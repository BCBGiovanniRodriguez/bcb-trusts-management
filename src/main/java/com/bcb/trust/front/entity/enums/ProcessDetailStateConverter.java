package com.bcb.trust.front.entity.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProcessDetailStateConverter implements AttributeConverter<ProcessDetailStateEnum, Integer>{

    @Override
    public Integer convertToDatabaseColumn(ProcessDetailStateEnum attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getIntValue();
    }

    @Override
    public ProcessDetailStateEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(ProcessDetailStateEnum.values())
            .filter(
                pds -> pds.getIntValue() == dbData
            ).findFirst().orElseThrow(IllegalArgumentException::new);
    }

}
