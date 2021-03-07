package com.v1.opencve.service;

import com.v1.opencve.domainobject.SubsProductDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface ISubsProductService {
    SubsProductDO createSubs(SubsProductDO subs);
    SubsProductDO getProductID(Long productID);
    SubsProductDO getUserID(Long userID);
    List<SubsProductDO> getAllSubs();
}
