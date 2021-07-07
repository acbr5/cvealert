package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.ReportsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReportsRepository extends JpaRepository<ReportsDO, Long> {
    List<ReportsDO> findByCveID(Long cveID);
    List<ReportsDO> findByUserID(Long userID);
}
