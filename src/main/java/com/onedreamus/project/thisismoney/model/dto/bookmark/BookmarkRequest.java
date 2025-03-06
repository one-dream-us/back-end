package com.onedreamus.project.thisismoney.model.dto.bookmark;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookmarkRequest {

    @NotNull(message = "dictionaryId는 필수 값입니다.")
    private Integer dictionaryId; // 북마크 할 용어 ID
}
