package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.DictionaryBookmark;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookmarkResponse {

    private Integer bookmarkCount;
    private List<BookmarkDto> bookmarkList;

    public static BookmarkResponse from(List<DictionaryBookmark> bookmarks) {

        return BookmarkResponse.builder()
                .bookmarkCount(bookmarks.size())
                .bookmarkList(bookmarks.stream()
                        .map(BookmarkDto::from)
                        .toList())
                .build();
    }

}
