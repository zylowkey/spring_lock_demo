package com.goldcard.purchase.service.impl;

import com.goldcard.purchase.pojo.ProductPo;
import com.goldcard.purchase.pojo.PurchaseRecordPo;
import com.goldcard.purchase.repository.ProductRepository;
import com.goldcard.purchase.repository.PurchaseRepository;
import com.goldcard.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 乐观锁是一种不使用数据库锁的机制，并且不会造成线程的阻塞，只是采用多版本号机制来实现。
 * 因为版本的冲突造成了请求失败的概率剧增，需要引入重入机制将请求失败的概率降低
 * 多次的重入会带来过多执行SQL的问题，可以按时间戳或者限制重入次数的办法
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

//    @Override
//    //启用Spring事务
//    @Transactional
//    public boolean purchase(Long userId, Long productId, int quantity) {
//        //获取产品
//        ProductPo product = productRepository.getProduct(productId);
//        //比较库存数量和购买数量
//        if (product.getStock() < quantity) {
//            //库存不足
//            return false;
//        }
//        //扣减库存
//        productRepository.decreaseProduct(productId, quantity);
//        //初始化购买记录
//        PurchaseRecordPo pr = initPurchaseRecord(userId, product, quantity);
//        //插入购买记录
//        purchaseRepository.insertPurchaseRecord(pr);
//        return true;
//    }

    /**
     * 使用乐观锁解决并发导致的超卖问题
     * 加入版本号判断，导致部分请求失败，导致请求结束后，还有库存存在
     * @param userId    用户编号
     * @param productId 产品编号
     * @param quantity  购买数量
     * @return
     */
//    @Override
//    //启用Spring事务,隔离级别设为读写提交
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    public boolean purchase(Long userId, Long productId, int quantity) {
//        //获取产品
//        ProductPo product = productRepository.getProduct(productId);
//        //比较库存数量和购买数量
//        if (product.getStock() < quantity) {
//            //库存不足
//            return false;
//        }
//        //获取当前版本号
//        int version = product.getVersion();
//        //扣减库存,同时将当前版本号发送给后台比较
//        int result = productRepository.decreaseProduct(productId, quantity,version);
//        //如果更新数据失败，说明数据在多线程中被其他线程修改，导致失败返回
//        if(result == 0){
//            return  false;
//        }
//        //初始化购买记录
//        PurchaseRecordPo pr = initPurchaseRecord(userId, product, quantity);
//        //插入购买记录
//        purchaseRepository.insertPurchaseRecord(pr);
//        return true;
//    }
    /**
     * 使用乐观锁解决并发导致的超卖问题
     * 加入版本号判断，导致部分请求失败，导致请求结束后，还有库存存在
     * 引入乐观锁重入机制，一旦更新失败，就再更新做一次，所以有时候乐观锁也称之为可重入锁。
     * 其原理是一旦发现版本号被更新，不是结束请求，而是重新做一次乐观锁流程，直至成功为止
     * 但是这个流程的重入会带来一个问题,那就是可能造成大量的SQL被执行。
     *例如,原本一个请求需要执行3条SQL,如果需要重入4次才能成功,那么就会有十几条SQL被执行,在高并发场景下,会给数据库带来很大的压力。
     *一般会考虑使用限制时间或者重入次数的办法,以压制过多的SQL被执行
     * 采用时间限制 弊端系统会随着自身的繁忙而大大减少重入的次数
     * @param userId    用户编号
     * @param productId 产品编号
     * @param quantity  购买数量
     * @return
     */
//    @Override
//    //启用Spring事务,隔离级别设为读写提交
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    public boolean purchase(Long userId, Long productId, int quantity) {
//        //当前时间
//        long start = System.currentTimeMillis();
//        //循环直至成功
//        while (true){
//            //循环时间
//            long end = System.currentTimeMillis();
//            //如果循环时间大于100ms，返回，终止循环
//            if(end - start > 100){
//                return false;
//            }
//        //获取产品
//        ProductPo product = productRepository.getProduct(productId);
//        //比较库存数量和购买数量
//        if (product.getStock() < quantity) {
//            //库存不足
//            return false;
//        }
//        //获取当前版本号
//        int version = product.getVersion();
//        //扣减库存,同时将当前版本号发送给后台比较
//        int result = productRepository.decreaseProduct(productId, quantity,version);
//        //如果更新数据失败，说明数据在多线程中被其他线程修改，导致失败返回
//        //导致失败，则通过循环重入尝试购买商品
//        if(result == 0){
////            return  false;
//            continue;
//        }
//        //初始化购买记录
//        PurchaseRecordPo pr = initPurchaseRecord(userId, product, quantity);
//        //插入购买记录
//        purchaseRepository.insertPurchaseRecord(pr);
//        return true;
//        }
//    }

    /**
     * 按次数重入
     *
     * @param userId    用户编号
     * @param productId 产品编号
     * @param quantity  购买数量
     * @return
     */
    @Override
    //启用Spring事务,隔离级别设为读写提交
    @Transactional
    public boolean purchase(Long userId, Long productId, int quantity) {
        //限定循环3次
        for (int i = 0; i < 3; i++) {
            //获取产品
            ProductPo product = productRepository.getProduct(productId);
            //比较库存数量和购买数量
            if (product.getStock() < quantity) {
                //库存不足
                return false;
            }
            //获取当前版本号
            int version = product.getVersion();
            //扣减库存,同时将当前版本号发送给后台比较
            int result = productRepository.decreaseProduct(productId, quantity, version);
            //如果更新数据失败，说明数据在多线程中被其他线程修改，导致失败返回
            if (result == 0) {
//            return  false;
                continue;
            }
            //初始化购买记录
            PurchaseRecordPo pr = initPurchaseRecord(userId, product, quantity);
            //插入购买记录
            purchaseRepository.insertPurchaseRecord(pr);
            return true;
        }
        return false;
    }

    /**
     * 初始化购买信息
     *
     * @param userId   用户编号
     * @param product  产品编号
     * @param quantity 购买数量
     * @return 购买信息
     */
    private PurchaseRecordPo initPurchaseRecord(Long userId, ProductPo product, int quantity) {
        PurchaseRecordPo pr = new PurchaseRecordPo();
        pr.setNote("购买日志，时间：" + System.currentTimeMillis());
        pr.setPrice(product.getPrice());
        pr.setProductId(product.getId());
        pr.setQuantity(quantity);
        double sum = product.getPrice() * quantity;
        pr.setSum(sum);
        pr.setUserId(userId);
        return pr;
    }
}
