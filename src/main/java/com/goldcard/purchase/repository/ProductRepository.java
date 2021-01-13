package com.goldcard.purchase.repository;

import com.goldcard.purchase.pojo.ProductPo;
import org.apache.ibatis.annotations.Param;

public interface ProductRepository {
    ProductPo getProduct(Long id);
//    int decreaseProduct(@Param("id") Long id, @Param("quantity") int quantity);

    /**
     * 使用乐观锁解决并发导致的超卖问题
     * @param id
     * @param quantity
     * @param version
     * @return
     */
    int decreaseProduct(@Param("id") Long id, @Param("quantity") int quantity,@Param("version") int version);
}
