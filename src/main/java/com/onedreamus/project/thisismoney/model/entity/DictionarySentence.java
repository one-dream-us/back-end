package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
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
public class DictionarySentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Dictionary dictionary;

    @JoinColumn
    @ManyToOne
    private Sentence sentence;

    public static DictionarySentence from(Dictionary dictionary, Sentence sentence) {
        return DictionarySentence.builder()
            .dictionary(dictionary)
            .sentence(sentence)
            .build();

    }
}
