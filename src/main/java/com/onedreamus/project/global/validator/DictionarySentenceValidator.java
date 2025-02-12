package com.onedreamus.project.global.validator;

import com.onedreamus.project.thisismoney.model.dto.DictionarySentenceRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DictionarySentenceValidator implements ConstraintValidator<ValidDictionarySentence, DictionarySentenceRequest> {
    @Override
    public boolean isValid(DictionarySentenceRequest request, ConstraintValidatorContext context) {
        // 만약 객체 자체가 null이면 다른 제약으로 처리하거나 true로 반환
        if (request == null) {
            return true;
        }
        // dictionaryId가 존재하면 나머지 필드는 검사하지 않음
        if (request.getDictionaryId() != null) {
            return true;
        }

        // dictionaryId가 null인 경우, 세 필드가 반드시 존재해야 함
        boolean valid = true;
        context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화

        if (request.getDictionaryTerm() == null || request.getDictionaryTerm().trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate("dictionaryTerm은 필수 값입니다.")
                    .addPropertyNode("dictionaryTerm")
                    .addConstraintViolation();
            valid = false;
        }
        if (request.getDictionaryDefinition() == null || request.getDictionaryDefinition().trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate("dictionaryDefinition은 필수 값입니다.")
                    .addPropertyNode("dictionaryDefinition")
                    .addConstraintViolation();
            valid = false;
        }
        if (request.getDictionaryDescription() == null || request.getDictionaryDescription().trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate("dictionaryDescription은 필수 값입니다.")
                    .addPropertyNode("dictionaryDescription")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }
}
