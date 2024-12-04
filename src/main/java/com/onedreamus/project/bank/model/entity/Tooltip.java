package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Tooltip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Dictionary dictionary;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private ScriptParagraph scriptParagraph;

    private Integer startIdx;
    private Integer endIdx;
}
