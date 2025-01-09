package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryWrongAnswerNote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WrongAnswerNoteDto {

    private Long wrongAnswerNoteId;
    private LocalDateTime createdAt;
    private Dictionary dictionary;

    public static WrongAnswerNoteDto from(DictionaryWrongAnswerNote wrongAnswerNote) {
        return WrongAnswerNoteDto.builder()
                .wrongAnswerNoteId(wrongAnswerNote.getId())
                .createdAt(wrongAnswerNote.getCreatedAt())
                .dictionary(wrongAnswerNote.getDictionary())
                .build();
    }

}
