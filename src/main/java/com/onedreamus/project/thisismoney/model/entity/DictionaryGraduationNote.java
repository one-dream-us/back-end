package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryGraduationNote{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Dictionary dictionary;

    private LocalDateTime createdAt;

    public static DictionaryGraduationNote from(Users user, Dictionary dictionary) {
        return DictionaryGraduationNote.builder()
                .user(user)
                .dictionary(dictionary)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
