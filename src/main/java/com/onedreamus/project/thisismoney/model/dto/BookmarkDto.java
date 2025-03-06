package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryBookmark;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookmarkDto {

    private Long bookmarkId;
    private LocalDateTime createdAt;
    private Dictionary dictionary;

    public static BookmarkDto from(DictionaryBookmark bookmark) {
        return BookmarkDto.builder()
                .bookmarkId(bookmark.getId())
                .createdAt(bookmark.getCreatedAt())
                .dictionary(bookmark.getDictionary())
                .build();
    }

}
