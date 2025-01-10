package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryWrongAnswerNote extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Dictionary dictionary;

    private boolean isGraduated;

    private int wrongCnt;
    private int correctCnt;

    public static DictionaryWrongAnswerNote from(Dictionary dictionary, Users user) {
        return DictionaryWrongAnswerNote.builder()
                .user(user)
                .dictionary(dictionary)
                .isGraduated(false)
                .wrongCnt(1)
                .build();
    }
}
