package com.v1.opencve.service;

import com.v1.opencve.domainobject.SubscriptionsDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface ISubscriptionsService {
    SubscriptionsDO createSubs(SubscriptionsDO subs);
    SubscriptionsDO getVendorID(Long vendorID);
    SubscriptionsDO getUserID(Long userID);
    List<SubscriptionsDO> getAllSubs();
}
