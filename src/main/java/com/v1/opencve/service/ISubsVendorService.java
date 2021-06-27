package com.v1.opencve.service;

import com.v1.opencve.domainobject.SubsVendorDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface ISubsVendorService {
    SubsVendorDO createSubs(SubsVendorDO subs);
    SubsVendorDO getVendorID(Long vendorID);
    SubsVendorDO getUserID(Long userID);
    List<SubsVendorDO> getAllSubs();
}
