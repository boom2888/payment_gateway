package com.games.pay.service;

import cn.hutool.core.util.StrUtil;
import com.games.common.constant.Constants;
import com.games.payment.domain.Shop;
import com.games.payment.domain.Order;
import com.games.payment.domain.ShopLimit;
import com.games.payment.service.BackService;
import com.games.payment.service.IShopLimitService;
import com.games.payment.service.IShopService;
import com.games.payment.service.IOrderService;
import com.games.system.service.ISysConfigService;
import com.games.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskService {

    @Autowired
    private BackService backService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IShopService businessCustomerCorporationService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    IShopLimitService shopLimitService;


    public void reloadTradeCallBack() {
        List<Shop> apiBackOpenShops = businessCustomerCorporationService.getApiBackOpenShops();
        if (apiBackOpenShops.isEmpty()) {
            log.info(MessageUtils.message("task.no.merchant.to.sync"));
            return;
        }
        List<Long> shopIds = new ArrayList<>();
        Shop shop = apiBackOpenShops.get(0);
        System.out.println("开始回掉商户:" + shop.getBusinessName());

        if (StrUtil.isNotBlank(shop.getNotifyOrderStatusEndpoint())) {
            shopIds.add(shop.getId());
            String value = configService.selectConfigByKey(Constants.TRADE_MAX_CALLBACK_TIMES);
            int callBackTimes = 2;
            if (StrUtil.isNotBlank(value)) {
                callBackTimes = Integer.parseInt(value);
            }
            List<Order> tradeList = orderService.getNeedCallBackOrders(shopIds, callBackTimes);
            try{
                Thread.sleep(600);
            }catch (Exception e){}
            log.info("同步商户交易回掉,商户号={},记录数量={}", shop.getBusinessName(), tradeList.size());
            tradeList.forEach(order -> backService.orderCallBack(shop, order, shop.getNotifyOrderStatusEndpoint()));
        }
        shop.setUpdateAt(new Date());
        businessCustomerCorporationService.updateById(shop);
    }

    /**
     * 重置商户限额任务
     * 每天0点清空商户限额值
     */
    public void resetShopLimit(){
       List<ShopLimit> shopLimitList = shopLimitService.list();
        for (ShopLimit shopLimit : shopLimitList) {
            shopLimit.setValue(BigDecimal.ZERO);
            shopLimit.setUpdateTime(new Date());
        }
        shopLimitService.updateBatchById(shopLimitList);
    }
}
