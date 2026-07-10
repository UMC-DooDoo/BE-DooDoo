package com.umc.doodoo.domain.category.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ColorConverter implements AttributeConverter<Color, String> {

    @Override
    public String convertToDatabaseColumn(Color attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Color convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Color.fromValue(dbData);
    }
}
