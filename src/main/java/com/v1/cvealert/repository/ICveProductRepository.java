package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.CVEProductDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICveProductRepository extends JpaRepository<CVEProductDO, Long> {
    List<CVEProductDO> findByCveID(Long cveID);
    Optional<CVEProductDO> findByProductID(Long productID);
    Optional<CVEProductDO> findByCveName(String cveName);
    Optional<CVEProductDO> findByProductName(String productName);
    List<CVEProductDO> findByIdIn(List<Long> ids);
}