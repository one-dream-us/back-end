package com.onedreamus.project.global.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DictionarySentenceValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDictionarySentence {
    String message() default "dictionaryId가 없으면 dictionaryTerm, dictionaryDefinition, dictionaryDescription은 필수입니다.";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
