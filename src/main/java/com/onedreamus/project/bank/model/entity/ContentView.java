package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "content_view")
@Entity(name="ContentView")
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
