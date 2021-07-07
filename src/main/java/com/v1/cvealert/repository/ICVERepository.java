package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.CVEDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICVERepository extends CrudRepository<CVEDO, Long> {
    Optional<CVEDO> findByCveid(String cve_id);

    Optional<CVEDO> findById(Long id);

    Page<CVEDO> findAll(Pageable pageable);

    List<CVEDO> findAll();

    Page<CVEDO> findByOrderByIdDesc(Pageable pageable);
}
