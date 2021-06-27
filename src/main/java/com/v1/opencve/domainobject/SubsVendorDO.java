package com.v1.opencve.domainobject;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "subsVendors")
public class SubsVendorDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long vendorID;
}
