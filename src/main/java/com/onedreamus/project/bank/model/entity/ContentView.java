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
@Table(name = "content_view")
public class ContentView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "view_date")
    private LocalDateTime viewDate;

    @Column(name = "view_count")
    private Integer viewCount;

    @JoinColumn(name = "content_id", insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Content content;
}
