package com.v1.opencve.repository;

import com.v1.opencve.domainobject.SubsVendorDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubsVendorRepository extends JpaRepository<SubsVendorDO, Long> {
    Optional<SubsVendorDO> findByVendorID(Long vendorID);
    Optional<SubsVendorDO> findByUserID(Long userID);
}
