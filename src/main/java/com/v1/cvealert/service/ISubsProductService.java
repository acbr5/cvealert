package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.SubsProductDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface ISubsProductService {
    SubsProductDO createSubs(SubsProductDO subs);
    SubsProductDO getProductID(Long productID);
    SubsProductDO getUserID(Long userID);
    List<SubsProductDO> getAllSubs();
    Optional<SubsProductDO> getSubsByProductID(Long productID);
}
