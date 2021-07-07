package com.v1.cvealert.domainobject;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "reports")
public class ReportsDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cveID;

    @Column(nullable = false)
    private String cvename;

    @Column(nullable = false)
    private String cvedetails;

    @Column(nullable = false)
    private String subscribes;

    @Column(nullable = false)
    private Long userID;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(length = 20)
    private Date reportedDate;
}
