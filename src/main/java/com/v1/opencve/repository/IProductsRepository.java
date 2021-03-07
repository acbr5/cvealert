package com.v1.opencve.repository;

import com.v1.opencve.domainobject.ProductsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductsRepository extends JpaRepository<ProductsDO, Long> {
    Optional<ProductsDO> findByVendorID(Long vendorID);
    Optional<ProductsDO> findByProductName(String productName);
}
