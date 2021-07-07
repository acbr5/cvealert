package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.CVEProductDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface ICveProductService {
    CVEProductDO createRow(CVEProductDO cveProductDO);

    List<CVEProductDO> getAllRows();

    List<CVEProductDO> getAllById(List<Long> ids);

    CVEProductDO getRowByProductName(String product_name);

    CVEProductDO getRowByProductID(Long product_id);

    CVEProductDO getRowByCveName(String cve_name);

    public Boolean isCVEExist(Long cveID, Long productID);
}
