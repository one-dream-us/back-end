package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryKeyNote;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeyNoteDto {

    private Long keyNoteId;
    private LocalDateTime createdAt;
    private Dictionary dictionary;

    public static KeyNoteDto from(DictionaryKeyNote keyNote) {
        return KeyNoteDto.builder()
                .keyNoteId(keyNote.getId())
                .createdAt(keyNote.getCreatedAt())
                .dictionary(keyNote.getDictionary())
                .build();
    }

}
