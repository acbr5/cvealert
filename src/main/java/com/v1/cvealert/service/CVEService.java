package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.CVEDO;
import com.v1.cvealert.repository.ICVERepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CVEService implements ICVEService{

    @Autowired
    private ICVERepository cveRepository;

    @Override
    public CVEDO createCVE(CVEDO cve) {
        return cveRepository.save(cve);
    }

    public CVEDO getCVEById(Long id) {
        Optional<CVEDO> currentCVE = cveRepository.findById(id);
        if(currentCVE.isPresent()){
            return currentCVE.get();
        }
        return null;
    }

    @Override
    public CVEDO updateCVE(CVEDO cve) {
//        long cveID = cve.getId();
        String cveID = cve.getCveid();
        Optional<CVEDO> currentCVE = cveRepository.findByCveid(cveID);
        if(currentCVE.isPresent()){
            currentCVE.get().setCvssv2BaseScore(cve.getCvssv2BaseScore());
            currentCVE.get().setCvssv3BaseScore(cve.getCvssv3BaseScore());
            currentCVE.get().setCvssv2Severity(cve.getCvssv2Severity());
            currentCVE.get().setCvssv3Severity(cve.getCvssv3Severity());
            currentCVE.get().setLastModifiedDate(cve.getLastModifiedDate());
            currentCVE.get().setDescription(cve.getDescription());

            cveRepository.save(currentCVE.get());

            CVEDO cvedo = new ModelMapper().map(currentCVE.get(), CVEDO.class);

            return cvedo;
        }
        return null;
    }

    @Override
    public Page<CVEDO> getAllCVEs(int pageNum) {
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return cveRepository.findAll(pageable);
    }

    public List<CVEDO> getAllCVEs(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<CVEDO> pagedResult = cveRepository.findByOrderByIdDesc(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<CVEDO>();
        }
    }

    @Override
    public Iterable<CVEDO> getAllCVEs() {
        return cveRepository.findAll();
    }

    public Iterable<CVEDO> list() {
        return cveRepository.findAll();
    }

    @Override
    public Iterable<CVEDO> save(List<CVEDO> cves) {
        return null;
    }

    @Override
    public Boolean isExist(String cve_id) {
        Optional<CVEDO> currentCVE = cveRepository.findByCveid(cve_id);
        if(currentCVE.isPresent()){
            return true;
        }
        return false;
    }

    public CVEDO save(CVEDO cvedo){
        return cveRepository.save(cvedo);
    }

    @Override
    public Page<CVEDO> listAll(int pageNum) {
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        //return cveRepository.findAll(pageable);
        return cveRepository.findByOrderByIdDesc(pageable);
    }
}
