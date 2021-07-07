package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.SubsVendorDO;
import com.v1.cvealert.repository.ISubsVendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubsVendorService implements ISubsVendorService{
    @Autowired
    private ISubsVendorRepository subsVendorRepository;

    @Override
    public SubsVendorDO createSubs(SubsVendorDO subs) {
        return subsVendorRepository.save(subs);
    }

    @Override
    public SubsVendorDO getVendorID(Long vendorID) {
        Optional<SubsVendorDO> currentSub = subsVendorRepository.findByVendorID(vendorID);
        if(currentSub.isPresent()){
            return currentSub.get();
        }
        return null;
    }

    @Override
    public SubsVendorDO getUserID(Long userID) {
        Optional<SubsVendorDO> currentSub = subsVendorRepository.findByUserID(userID);
        if(currentSub.isPresent()){
            return currentSub.get();
        }
        return null;
    }

    @Override
    public List<SubsVendorDO> getAllSubs() {
        return subsVendorRepository.findAll();
    }

    @Override
    public Optional<SubsVendorDO> getSubsByVendorID(Long vendorID) {
        return subsVendorRepository.findByVendorID(vendorID);
    }
}
