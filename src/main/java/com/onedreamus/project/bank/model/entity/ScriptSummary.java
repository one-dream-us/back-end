package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScriptSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String summaryText;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Content content;
}
