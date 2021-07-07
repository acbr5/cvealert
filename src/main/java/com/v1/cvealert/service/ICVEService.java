package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.CVEDO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface ICVEService {
    CVEDO createCVE(CVEDO cve);

    CVEDO getCVEById(Long id);

    CVEDO updateCVE(CVEDO cve);

    Iterable<CVEDO> list();

    Iterable<CVEDO> save(List<CVEDO> cves);

    Boolean isExist (String cve_id);

    Page<CVEDO> getAllCVEs(int pageNum);

    List<CVEDO> getAllCVEs(Integer pageNo, Integer pageSize, String sortBy);

    Iterable<CVEDO> getAllCVEs();

    Page<CVEDO> listAll(int pageNum);
}
