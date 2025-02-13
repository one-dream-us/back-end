package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name="dictionary")
@Entity(name="Dictionary")
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String term;
    private String definition;
    private String description;

    public static Dictionary from(String term, String definition, String description) {
        return Dictionary.builder()
            .term(term)
            .definition(definition)
            .description(description)
            .build();
    }
}
