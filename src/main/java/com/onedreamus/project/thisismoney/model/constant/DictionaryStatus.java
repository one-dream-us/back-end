package com.onedreamus.project.thisismoney.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DictionaryStatus {

    KEY_NOTE,
    WRONG_ANSWER_NOTE,
    GRADUATION_NOTE,
    SCRAP,
    NONE; // 스크랩, 핵심, 오답, 졸업 어디에도 속하지 않는 상태

}
