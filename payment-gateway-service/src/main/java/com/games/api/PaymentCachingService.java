package com.games.api;

import cn.hutool.core.util.StrUtil;
import com.games.api.bean.PayOrderResult;
import com.games.api.dto.RiskControlEntity;
import com.games.api.enums.PaymentMethod;
import com.games.api.enums.PaymentType;
import com.games.api.enums.RiskCacheName;
import com.games.api.vo.CreateOrderVo;
import com.games.api.vo.PayOrderVo;
import com.games.common.core.redis.RedisCache;
import com.games.payment.domain.Customer;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.service.ICustomerService;
import com.games.wynpay.WynPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PaymentCachingService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    ICustomerService customerService;

    @Autowired
    WynPayConfig wynPayConfig;

    private static RiskControlEntity getRiskControlEntity(Shop shop, CreateOrderVo vo, Order order) {
        RiskControlEntity riskControlEntity = new RiskControlEntity();
        riskControlEntity.setEmail(vo.getCustomerEmail());
        riskControlEntity.setUserId(vo.getCustomerId());
        riskControlEntity.setMerchantId(shop.getId());
        riskControlEntity.setOrderId(order.getSsoOrderId());
        riskControlEntity.setPayAmount(order.getTransferAmount());
        return riskControlEntity;
    }

    public void updateToCacheAfterCreate(Shop shop, Order order, CreateOrderVo vo) {
        if (!wynPayConfig.getCacheEnabled()) {
            System.out.println("写入缓存开关处于关闭状态");
            return;
        }
        RiskControlEntity riskControlEntity = getRiskControlEntity(shop, vo, order);
        this.updateToCacheAfterCreate(riskControlEntity);
    }

    public void updateToCacheAfterCreate(RiskControlEntity riskControlEntity) {
        try {
            long now = System.currentTimeMillis();
            String userId = riskControlEntity.getUserId();
            // 判断是否是新用户
            Integer isNewUser = redisCache.getCacheObject("isNewUser:" + userId);
            if (isNewUser == null || isNewUser == 0) {
                Customer customer = customerService.getBySsoCustomerId(riskControlEntity.getMerchantId(), riskControlEntity.getUserId());
                if (customer != null) {
                    Date createdAt = customer.getCreatedAt();
                    long diff = now - createdAt.getTime();
                    if (diff < 24 * 60 * 60 * 1000) {
                        redisCache.setCacheObject("isNewUser:" + userId, 1);
                        riskControlEntity.setIsNewUser(true);
                    } else {
                        redisCache.setCacheObject("isNewUser:" + userId, 2);
                        riskControlEntity.setIsNewUser(false);
                    }
                }
            }
            else if (isNewUser == 1){
                riskControlEntity.setIsNewUser(true);
            }
            else if (isNewUser == 2) {
                riskControlEntity.setIsNewUser(false);
            }
            else {
                log.error("The field “isNewUser” is invalid");
            }
            redisCache.setCacheObject(RiskCacheName.RISK_ENTITY.getName() + riskControlEntity.getOrderId(), riskControlEntity);
        } catch (Exception e) {
            log.error(StrUtil.format("write to cache error:{}", e.getMessage()));
        }
    }

    public void updateToCacheAfterPayAsync(PayOrderVo vo, PayOrderResult payResult) {
        if (!wynPayConfig.getCacheEnabled()) {
            System.out.println("写入缓存开关处于关闭状态");
            return;
        }
        RiskControlEntity riskControlEntity = new RiskControlEntity();
        riskControlEntity.setPaymentType(vo.getPaymentType());
        riskControlEntity.setIs3DSAuthenticated(payResult.getIs3DSAuthenticated());
        riskControlEntity.setIpAddress(vo.getDeviceDetail().getIpAddress());
        updateToCacheAfterPayAsync(riskControlEntity);
    }

    public void updateToCacheAfterPayAsync(RiskControlEntity riskControlEntity) {
        CompletableFuture.runAsync(() -> updateToCacheAfterPay(riskControlEntity));
    }

    public void updateToCacheAfterPay(RiskControlEntity riskControlEntity) {
        try {
            // 存储 卡支付 交易的数据和时间
            updatePayInfo(riskControlEntity, PaymentType.CARD.getValue(), RiskCacheName.RISK_NUVEI_CARD_3DS.getName(),
                    RiskCacheName.RISK_NUVEI_CARD_2DS.getName());

            // 存储 apple pay 交易的数据和时间
            updatePayInfo(riskControlEntity, PaymentType.APPLE_PAY.getValue(),
                    RiskCacheName.RISK_NUVEI_APPLE_PAY_3DS.getName(), RiskCacheName.RISK_NUVEI_APPLE_PAY_2DS.getName());
        } catch (Exception e) {
            log.error(StrUtil.format("write to cache error:{}", e.getMessage()));
        }
        //
    }

    public void updateEmailInfo(RiskControlEntity riskControlEntity) {
        if (PaymentMethod.NUVEI.getName().equals(riskControlEntity.getPaymentMethod())) {
//            redisCache.
        }
    }

    public void updatePayInfo(RiskControlEntity riskControlEntity, String paymentType, String keyThreeDs, String keyTwoDs) {
        String email = riskControlEntity.getEmail();
        BigDecimal payAmount = riskControlEntity.getPayAmount();
        long now = System.currentTimeMillis();
        if (PaymentMethod.NUVEI.getName().equals(riskControlEntity.getPaymentMethod()) && StrUtil.equals(riskControlEntity.getPaymentType(), paymentType)) {
            if (riskControlEntity.getIs3DSAuthenticated()) {
                redisCache.zSetCacheObject(keyThreeDs, payAmount, now);
                redisCache.zSetCacheObject(keyThreeDs + email, payAmount, now);
            } else {
                redisCache.zSetCacheObject(keyTwoDs, payAmount, now);
                redisCache.zSetCacheObject(keyTwoDs + email, payAmount, now);
            }
            String cardKey = StrUtil.isNotBlank(riskControlEntity.getCardFinger())
                    ? riskControlEntity.getCardFinger()
                    : riskControlEntity.getCardNumber();
            if (StrUtil.isNotBlank(cardKey)) {
                redisCache.zSetCacheObject(RiskCacheName.RISK_CARD_NUM.getName(), cardKey, now);
            }
        }
    }

}
