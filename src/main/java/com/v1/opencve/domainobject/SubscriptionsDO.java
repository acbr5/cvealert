package com.v1.opencve.domainobject;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "subscriptions")
public class SubscriptionsDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long vendorID;
}
