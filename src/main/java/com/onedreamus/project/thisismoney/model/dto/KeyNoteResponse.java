package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.DictionaryKeyNote;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeyNoteResponse {

    private Integer keyNoteCount;
    private List<KeyNoteDto> keyNoteList;

    public static KeyNoteResponse from(List<DictionaryKeyNote> keyNotes) {

        return KeyNoteResponse.builder()
                .keyNoteCount(keyNotes.size())
                .keyNoteList(keyNotes.stream()
                        .map(KeyNoteDto::from)
                        .toList())
                .build();
    }

}
