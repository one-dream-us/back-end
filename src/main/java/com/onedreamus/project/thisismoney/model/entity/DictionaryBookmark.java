package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryBookmark extends BaseEntity{

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

    public static DictionaryBookmark from(Users user, Dictionary dictionary) {
        return DictionaryBookmark.builder()
                .user(user)
                .dictionary(dictionary)
                .isGraduated(false)
                .correctCnt(0)
                .build();
    }

}
