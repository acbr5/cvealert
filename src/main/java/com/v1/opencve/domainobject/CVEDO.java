package com.v1.opencve.domainobject;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cves")
public class CVEDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String cveid;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 15)
    private Double cvssv2BaseScore;

    @Column(length = 15)
    private String cvssv2Severity;

    @Column(length = 15)
    private Double cvssv3BaseScore;

    @Column(length = 15)
    private String cvssv3Severity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(length = 20)
    private Date publishedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(length = 20)
    private Date lastModifiedDate;
}
