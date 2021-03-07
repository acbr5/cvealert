package com.v1.opencve.repository;

import com.v1.opencve.domainobject.SubsProductDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubsProductRepository extends JpaRepository<SubsProductDO, Long> {
    Optional<SubsProductDO> findByProductID(Long vendorID);
    Optional<SubsProductDO> findByUserID(Long userID);
}
