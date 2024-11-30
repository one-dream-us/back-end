package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @OneToOne(fetch = FetchType.EAGER)
    private Tag tag;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Content content;

    private Integer sequence;
}
