package com.v1.cvealert.domainobject;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CveProduct")
public class CVEProductDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long productID;

    @Column(nullable = false)
    private Long cveID;

    @Column(nullable = false)
    private String cveName;
}
