package com.onedreamus.project.thisismoney.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onedreamus.project.thisismoney.model.dto.NewsRequest;
import com.onedreamus.project.thisismoney.model.dto.ScheduledNewsRequest;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NewsRequestConverter implements AttributeConverter<ScheduledNewsRequest, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ScheduledNewsRequest scheduledNewsRequest) {
        if (scheduledNewsRequest == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(scheduledNewsRequest);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting NewsRequest to JSON string", e);
        }
    }

    @Override
    public ScheduledNewsRequest convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, ScheduledNewsRequest.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON string to NewsRequest", e);
        }
    }
}
