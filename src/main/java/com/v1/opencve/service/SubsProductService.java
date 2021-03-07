package com.v1.opencve.service;

import com.v1.opencve.domainobject.SubsProductDO;
import com.v1.opencve.repository.ISubsProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubsProductService implements ISubsProductService{
    @Autowired
    private ISubsProductRepository subsRepository;

    @Override
    public SubsProductDO createSubs(SubsProductDO subs) {
        return subsRepository.save(subs);
    }

    @Override
    public SubsProductDO getProductID(Long productID) {
        Optional<SubsProductDO> currentSub = subsRepository.findByProductID(productID);
        if(currentSub.isPresent()){
            return currentSub.get();
        }
        return null;
    }

    @Override
    public SubsProductDO getUserID(Long userID) {
        Optional<SubsProductDO> currentSub = subsRepository.findByUserID(userID);
        if(currentSub.isPresent()){
            return currentSub.get();
        }
        return null;
    }

    @Override
    public List<SubsProductDO> getAllSubs() {
        return subsRepository.findAll();
    }
}
