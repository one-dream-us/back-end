package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name="ScriptParagraphDictionary")
@Table(name="script_paragraph_dictionary")
public class ScriptParagraphDictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private ScriptParagraph scriptParagraph;
    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Dictionary dictionary;
    private Integer startIdx;
    private Integer endIdx;
}
