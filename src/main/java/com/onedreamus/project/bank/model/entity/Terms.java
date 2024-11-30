package com.onedreamus.project.bank.model.entity;

import com.onedreamus.project.bank.model.constant.TermsType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TermsType type;

    private String value;
    private Boolean isRequired;
}
