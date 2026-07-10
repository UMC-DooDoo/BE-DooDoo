package com.umc.doodoo.domain.todo.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PriorityConverter implements AttributeConverter<Priority, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Priority attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Priority convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Priority.fromValue(dbData);
    }
}
