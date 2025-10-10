package com.netcarat.modal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class KaratTypeConverter implements AttributeConverter<KaratType, String> {

    @Override
    public String convertToDatabaseColumn(KaratType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public KaratType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        return KaratType.fromValue(dbData.trim());
    }
}
