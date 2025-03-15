//package com.onedreamus.project.thisismoney.model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Builder
//public class NewsTag {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @JoinColumn
//    @ManyToOne(fetch = FetchType.LAZY)
//    private News news;
//
//    @JoinColumn
//    @ManyToOne(fetch = FetchType.EAGER)
//    private Tag tag;
//
//}
