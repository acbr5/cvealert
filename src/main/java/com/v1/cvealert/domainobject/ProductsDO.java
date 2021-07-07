package com.v1.cvealert.domainobject;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class ProductsDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long vendorID;
}
