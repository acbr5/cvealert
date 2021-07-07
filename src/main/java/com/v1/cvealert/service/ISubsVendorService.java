package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.SubsVendorDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface ISubsVendorService {
    SubsVendorDO createSubs(SubsVendorDO subs);
    SubsVendorDO getVendorID(Long vendorID);
    SubsVendorDO getUserID(Long userID);
    List<SubsVendorDO> getAllSubs();
    Optional<SubsVendorDO> getSubsByVendorID(Long vendorID);
}
