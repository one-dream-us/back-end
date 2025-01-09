package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.DictionaryWrongAnswerNote;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WrongAnswerNoteResponse {

    private Integer wrongAnswerNoteSize;
    private List<WrongAnswerNoteDto> wrongAnswerNotes;

    public static WrongAnswerNoteResponse from(List<DictionaryWrongAnswerNote> wrongAnswerNotes) {
        return WrongAnswerNoteResponse.builder()
                .wrongAnswerNoteSize(wrongAnswerNotes.size())
                .wrongAnswerNotes(wrongAnswerNotes.stream()
                        .map(WrongAnswerNoteDto::from)
                        .toList())
                .build();
    }

}
