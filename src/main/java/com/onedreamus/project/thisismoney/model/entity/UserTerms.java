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
public class UserTerms {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Terms terms;

    private Boolean isApproved;
    private LocalDateTime createdAt;
}
