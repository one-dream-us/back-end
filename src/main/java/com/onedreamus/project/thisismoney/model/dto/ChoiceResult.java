package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.constant.DictionaryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChoiceResult {

    private String term;
    private Boolean isCorrect;
    private Integer correctCnt;
    private Integer wrongCnt;
    private DictionaryStatus status; // 오답 노트/ 졸업노트 둘 중 어디에 위치하는지 => 단어장 이동 시 필요.
}
