package com.v1.opencve.repository;

import com.v1.opencve.domainobject.SubscriptionsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubscriptionsRepository extends JpaRepository<SubscriptionsDO, Long> {
    Optional<SubscriptionsDO> findByVendorID(Long vendorID);
    Optional<SubscriptionsDO> findByUserID(Long userID);
}
