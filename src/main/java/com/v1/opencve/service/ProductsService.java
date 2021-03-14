package com.v1.opencve.service;

import com.v1.opencve.domainobject.CVEDO;
import com.v1.opencve.domainobject.ProductsDO;
import com.v1.opencve.repository.IProductsRepository;
import com.v1.opencve.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductsService implements IProductsService{
    @Autowired
    IProductsRepository productsRepository = new ProductsRepository();

    @Override
    public ProductsDO createProduct(ProductsDO productsDO) {
        return productsRepository.save(productsDO);
    }

    @Override
    public List<ProductsDO> getAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    public ProductsDO getProductByProductName(String product_name) {
        Optional<ProductsDO> currentProduct = productsRepository.findByProductName(product_name);
        if(currentProduct.isPresent()){
            return currentProduct.get();
        }
        return null;
    }

    @Override
    public ProductsDO getProductByProductID(Long product_id) {
        Optional<ProductsDO> currentProduct = productsRepository.findById(product_id);
        if(currentProduct.isPresent()){
            return currentProduct.get();
        }
        return null;
    }

    @Override
    public Boolean isExist(String productName) {
        Optional<ProductsDO> currentProduct = productsRepository.findByProductName(productName);
        if(currentProduct.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public Page<ProductsDO> listAll(int pageNum, List<Long> ids) {
        int pageSize = 25;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return productsRepository.findByIdIn(ids, pageable);
    }
}