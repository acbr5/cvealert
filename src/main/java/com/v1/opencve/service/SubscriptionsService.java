package com.v1.opencve.service;

import com.v1.opencve.domainobject.SubscriptionsDO;
import com.v1.opencve.repository.ISubscriptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubscriptionsService implements ISubscriptionsService{
    @Autowired
    private ISubscriptionsRepository subsRepository;

    @Override
    public SubscriptionsDO createSubs(SubscriptionsDO subs) {
        return subsRepository.save(subs);
    }

    @Override
    public SubscriptionsDO getVendorID(Long vendorID) {
        Optional<SubscriptionsDO> currentSub = subsRepository.findByVendorID(vendorID);
        if(currentSub.isPresent()){
            return currentSub.get();
        }
        return null;
    }

    @Override
    public SubscriptionsDO getUserID(Long userID) {
        Optional<SubscriptionsDO> currentSub = subsRepository.findByUserID(userID);
        if(currentSub.isPresent()){
            return currentSub.get();
        }
        return null;
    }

    @Override
    public List<SubscriptionsDO> getAllSubs() {
        return subsRepository.findAll();
    }
}
