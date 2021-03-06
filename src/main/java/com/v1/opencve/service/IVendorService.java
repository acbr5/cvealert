package com.v1.opencve.service;

import com.v1.opencve.domainobject.VendorDO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface IVendorService{
    VendorDO createVendor(VendorDO vendor);

    List<VendorDO> getAllVendors();

    VendorDO getVendorById(Long id);

    VendorDO getVendorByName(String vendor_name);

    Boolean isExist (String vendorName);

    Page<VendorDO> listAll(int pageNum);
}
