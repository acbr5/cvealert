package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.SubsProductDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubsProductRepository extends JpaRepository<SubsProductDO, Long> {
    Optional<SubsProductDO> findByProductID(Long vendorID);
    Optional<SubsProductDO> findByUserID(Long userID);
}
