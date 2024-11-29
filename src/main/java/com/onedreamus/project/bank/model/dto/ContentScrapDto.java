package com.onedreamus.project.bank.model.dto;

import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentScrapDto {

    private Integer scrapId;
    private Content content;

    public static ContentScrapDto from(ContentScrap contentScrap){
        return ContentScrapDto.builder()
            .scrapId(contentScrap.getId())
            .content(contentScrap.getContent())
            .build();
    }
}
