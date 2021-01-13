package com.goldcard.purchase.controller;

import com.goldcard.purchase.common.Result;
import com.goldcard.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/purchase")
    public Result purchase(Long userId, Long productId, Integer quantity) {
        boolean success = purchaseService.purchase(userId, productId, quantity);
        String message = success ? "抢购成功" : "抢购失败";
        Result result = new Result(success, message);
        return result;
    }
}
