package com.v1.opencve.repository;

import com.v1.opencve.domainobject.VendorDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVendorRepository extends JpaRepository<VendorDO, Long> {
    Optional<VendorDO> findByVendorName(String vendor_name);
}
