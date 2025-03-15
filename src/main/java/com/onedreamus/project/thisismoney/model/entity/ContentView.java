//package com.onedreamus.project.thisismoney.model.entity;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//import lombok.*;
//
//@Entity
//@Table(name = "content_view")
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class ContentView {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "content_id")
//    private Content content;
//
//    @Column(name = "view_count")
//    private Integer viewCount;
//
//    @Column(name = "view_date")
//    private LocalDateTime viewDate;
//
//    @Builder
//    public ContentView(Content content, Integer viewCount, LocalDateTime viewDate) {
//        this.content = content;
//        this.viewCount = viewCount;
//        this.viewDate = LocalDateTime.now();
//    }
//
//    public void incrementViewCount() {
//        this.viewCount = this.viewCount + 1;
//        this.viewDate = LocalDateTime.now();
//    }
//}
