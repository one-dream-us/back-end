package com.onedreamus.project.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onedreamus.project.global.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterException {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void throwException(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = objectMapper.writeValueAsString(ErrorResponse.builder()
            .errorCode(errorCode)
            .errorMessage(errorCode.getMessage())
            .build());

        response.getWriter().write(json);
    }
}
