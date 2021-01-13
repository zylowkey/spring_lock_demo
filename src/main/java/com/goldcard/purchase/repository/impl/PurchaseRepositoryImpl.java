package com.goldcard.purchase.repository.impl;

import com.goldcard.purchase.mapper.PurchaseRecordMapper;
import com.goldcard.purchase.pojo.PurchaseRecordPo;
import com.goldcard.purchase.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;
    @Override
    public int insertPurchaseRecord(PurchaseRecordPo pr) {
        return purchaseRecordMapper.insertPurchaseRecord(pr);
    }
}
