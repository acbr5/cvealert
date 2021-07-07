package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.ProductsDO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface IProductsService {
    ProductsDO createProduct(ProductsDO productsDO);

    List<ProductsDO> getAllProducts();

    ProductsDO getProductByProductName(String product_name);

    ProductsDO getProductByProductID(Long product_id);

    Boolean isExist (String productName);

    Page<ProductsDO> listAll(int pageNum, List<Long> ids);
}
