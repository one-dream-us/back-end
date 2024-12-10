package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ContentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "content_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Content content;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @Setter
    private Boolean isDeleted;

    @Builder
    public ContentHistory(Content content, Users user, boolean isDeleted) {
        this.content = content;
        this.user = user;
        this.isDeleted = isDeleted;
    }
}
