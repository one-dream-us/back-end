package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "dictionary_scrap")
@Entity(name = "DictionaryScrap")
public class DictionaryScrap extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Dictionary dictionary;

    public static DictionaryScrap make(Users user, Dictionary dictionary) {
        return DictionaryScrap.builder()
            .user(user)
            .dictionary(dictionary)
            .build();
    }
}
