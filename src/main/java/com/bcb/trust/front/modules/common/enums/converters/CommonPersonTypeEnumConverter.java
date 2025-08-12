package com.bcb.trust.front.modules.common.enums.converters;

import java.util.stream.Stream;

import com.bcb.trust.front.modules.common.enums.CommonPersonTypeEnum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CommonPersonTypeEnumConverter implements AttributeConverter<CommonPersonTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CommonPersonTypeEnum attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getIntValue();
    }

    @Override
    public CommonPersonTypeEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(CommonPersonTypeEnum.values()).filter(
            pse -> pse.getIntValue() == dbData
        ).findFirst().orElseThrow(IllegalArgumentException::new);
    }

}
