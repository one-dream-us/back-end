package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryKeyNote extends BaseEntity{

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

    private int correctCnt;

    public static DictionaryKeyNote from(Users user, Dictionary dictionary) {
        return DictionaryKeyNote.builder()
                .user(user)
                .dictionary(dictionary)
                .isGraduated(false)
                .correctCnt(0)
                .build();
    }

}
