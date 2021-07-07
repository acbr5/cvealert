package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.VendorDO;
import com.v1.cvealert.repository.IVendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VendorService implements IVendorService{
    @Autowired
    private IVendorRepository vendorRepository;

    @Override
    public VendorDO createVendor(VendorDO vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public List<VendorDO> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public VendorDO getVendorById(Long id) {
        Optional<VendorDO> currentVendor = vendorRepository.findById(id);
        if(currentVendor.isPresent()){
            return currentVendor.get();
        }
        return null;
    }

    @Override
    public VendorDO getVendorByVendorID(Long id) {
        Optional<VendorDO> currentVendor = vendorRepository.findById(id);
        if(currentVendor.isPresent()){
            return currentVendor.get();
        }
        return null;
    }

    @Override
    public VendorDO getVendorByName(String vendor_name) {
        Optional<VendorDO> currentVendor = vendorRepository.findByVendorName(vendor_name);
        if(currentVendor.isPresent()){
            return currentVendor.get();
        }
        return null;
    }

    @Override
    public Boolean isExist(String vendorName) {
        Optional<VendorDO> currentVendor = vendorRepository.findByVendorName(vendorName);
        if(currentVendor.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public Page<VendorDO> listAll(int pageNum) {
        int pageSize = 25;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return vendorRepository.findAll(pageable);
    }
}
