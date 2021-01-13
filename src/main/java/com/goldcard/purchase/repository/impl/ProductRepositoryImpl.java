package com.goldcard.purchase.repository.impl;

import com.goldcard.purchase.mapper.ProductMapper;
import com.goldcard.purchase.pojo.ProductPo;
import com.goldcard.purchase.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @Autowired
    private ProductMapper productDao;

    @Override
    public ProductPo getProduct(Long id) {
        return productDao.getProduct(id);
    }

    @Override
    public int decreaseProduct(Long id, int quantity, int version) {
        return productDao.decreaseProduct(id,quantity,version);
    }

//    @Override
//    public int decreaseProduct(Long id, int quantity) {
//        return productDao.decreaseProduct(id,quantity);
//    }
}
