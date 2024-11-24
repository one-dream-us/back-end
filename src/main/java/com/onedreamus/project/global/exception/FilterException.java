package com.onedreamus.project.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onedreamus.project.global.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * filter 계층에서는 @RestControllerAdvice 어노테이션이 적용되지 않음
 * 별도의 filter 게층에 대한 exception 처리 로직
 */
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

        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();

    }
}
