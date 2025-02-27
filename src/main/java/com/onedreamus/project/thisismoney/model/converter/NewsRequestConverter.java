package com.onedreamus.project.thisismoney.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onedreamus.project.thisismoney.model.dto.backOffice.NewsContent;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NewsRequestConverter implements AttributeConverter<NewsContent, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(NewsContent newsContent) {
        if (newsContent == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(newsContent);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting NewsRequest to JSON string", e);
        }
    }

    @Override
    public NewsContent convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, NewsContent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON string to NewsRequest", e);
        }
    }
}
