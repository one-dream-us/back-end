package com.onedreamus.project.thisismoney.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TermsType {
    TERMS_OF_USE("이용 약관 동의"),
    PRIVACY_POLICY("개인정보 처리 동의");

    private final String name;
}
