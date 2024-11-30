package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScriptParagraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalTime timestamp;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private ScriptFull scriptFull;

    private String paragraphText;
}
