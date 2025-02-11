package com.onedreamus.project.thisismoney.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onedreamus.project.thisismoney.model.dto.NewsRequest;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NewsRequestConverter implements AttributeConverter<NewsRequest, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(NewsRequest newsRequest) {
        if (newsRequest == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(newsRequest);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting NewsRequest to JSON string", e);
        }
    }

    @Override
    public NewsRequest convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, NewsRequest.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON string to NewsRequest", e);
        }
    }
}
