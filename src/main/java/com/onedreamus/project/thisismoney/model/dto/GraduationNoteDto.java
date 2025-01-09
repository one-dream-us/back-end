package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryGraduationNote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GraduationNoteDto {

    private Long graduationNoteId;
    private LocalDateTime createdAt;
    private Dictionary dictionary;

    public static GraduationNoteDto from(DictionaryGraduationNote graduationNote) {
        return GraduationNoteDto.builder()
                .graduationNoteId(graduationNote.getId())
                .createdAt(graduationNote.getCreatedAt())
                .dictionary(graduationNote.getDictionary())
                .build();
    }

}
