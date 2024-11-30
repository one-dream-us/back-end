package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Content content;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    private Boolean isWatch;
}
