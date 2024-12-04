package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "script_full")
@Entity(name="ScriptFull")
public class ScriptFull {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Content content;

}
