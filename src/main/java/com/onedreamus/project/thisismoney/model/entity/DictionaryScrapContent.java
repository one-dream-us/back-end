package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryScrapContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Content content;

    @JoinColumn
    @OneToOne(fetch = FetchType.EAGER)
    private DictionaryScrap dictionaryScrap;

    public static DictionaryScrapContent from(DictionaryScrap newDictionaryScrap, Content content) {
        return DictionaryScrapContent.builder()
            .content(content)
            .dictionaryScrap(newDictionaryScrap)
            .build();
    }
}
