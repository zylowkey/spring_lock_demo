package com.goldcard.purchase.repository;

import com.goldcard.purchase.pojo.PurchaseRecordPo;

public interface PurchaseRepository {
    int insertPurchaseRecord(PurchaseRecordPo pr);
}
