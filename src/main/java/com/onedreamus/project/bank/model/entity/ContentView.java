package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime viewedAt;
    private Integer viewCount;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Content content;
}
