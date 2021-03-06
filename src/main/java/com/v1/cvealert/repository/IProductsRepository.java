package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.ProductsDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductsRepository extends JpaRepository<ProductsDO, Long> {
    Optional<ProductsDO> findByVendorID(Long vendorID);
    Optional<ProductsDO> findByProductName(String productName);
    Page<ProductsDO> findByIdIn(List<Long> ids, Pageable pageable);
}
