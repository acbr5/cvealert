package com.v1.cvealert.domainobject;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "blog")
public class BlogDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}