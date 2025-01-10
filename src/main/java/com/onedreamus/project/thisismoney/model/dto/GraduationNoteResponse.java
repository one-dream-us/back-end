package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.DictionaryGraduationNote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GraduationNoteResponse {

    private Integer graduationNoteSize;
    private List<GraduationNoteDto> graduationNotes;

    public static GraduationNoteResponse from(List<DictionaryGraduationNote> graduationNotes) {
        return GraduationNoteResponse.builder()
                .graduationNoteSize(graduationNotes.size())
                .graduationNotes(graduationNotes.stream()
                        .map(GraduationNoteDto::from)
                        .toList())
                .build();
    }

}
