package com.onedreamus.project.thisismoney.model.entity;

import com.onedreamus.project.thisismoney.model.constant.TermsType;
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
