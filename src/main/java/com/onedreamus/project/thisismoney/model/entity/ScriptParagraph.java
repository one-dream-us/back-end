package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "script_paragraph")
@Entity(name="ScriptParagraph")
public class ScriptParagraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalTime timestamp;

    @Column(name = "paragraph_text")
    private String paragraphText;

    @JoinColumn(name = "script_full_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ScriptFull scriptFull;

}
