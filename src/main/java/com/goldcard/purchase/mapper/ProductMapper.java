package com.goldcard.purchase.mapper;

import com.goldcard.purchase.pojo.ProductPo;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper {
    /**
     * 获取产品
     *
     * @param id
     * @return
     */
    ProductPo getProduct(Long id);

    /**
     * 减库存，而@Param标明MyBatis参数传递给后台
     *
     * @param id
     * @param quantity
     * @return
     */
    int decreaseProduct(@Param("id") Long id, @Param("quantity") int quantity,@Param("version") int version);
}
