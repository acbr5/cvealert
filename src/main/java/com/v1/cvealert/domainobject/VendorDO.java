package com.v1.cvealert.domainobject;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vendors")
public class VendorDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vendorName;
}
