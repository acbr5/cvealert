package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.CVEProductDO;
import com.v1.cvealert.repository.CveProductRepository;
import com.v1.cvealert.repository.ICveProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CveProductService implements ICveProductService{
    @Autowired
    ICveProductRepository productRepository = new CveProductRepository();

    @Override
    public CVEProductDO createRow(CVEProductDO cveProductDO) {
        return productRepository.save(cveProductDO);
    }

    @Override
    public List<CVEProductDO> getAllRows() {
        return productRepository.findAll();
    }

    @Override
    public List<CVEProductDO> getAllById(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }

    @Override
    public CVEProductDO getRowByProductName(String product_name) {
        Optional<CVEProductDO> currentProduct = productRepository.findByProductName(product_name);
        if(currentProduct.isPresent()){
            return currentProduct.get();
        }
        return null;
    }

    @Override
    public CVEProductDO getRowByProductID(Long product_id) {
        Optional<CVEProductDO> currentProduct = productRepository.findByProductID(product_id);
        if(currentProduct.isPresent()){
            return currentProduct.get();
        }
        return null;
    }

    @Override
    public CVEProductDO getRowByCveName(String cve_name) {
        Optional<CVEProductDO> currentProduct = productRepository.findByCveName(cve_name);
        if(currentProduct.isPresent()){
            return currentProduct.get();
        }
        return null;
    }

    @Override
    public Boolean isCVEExist(Long cveID, Long productID) {
        List<CVEProductDO> currentCVEs = productRepository.findByCveID(cveID);

        for (CVEProductDO cveProduct : currentCVEs){
                if(cveProduct.getCveID().equals(cveID) && cveProduct.getProductID().equals(productID))
                    return true;
                return false;
        }
        return false;
    }
}