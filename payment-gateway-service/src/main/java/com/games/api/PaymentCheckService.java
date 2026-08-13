package com.games.api;

import com.games.api.dto.RiskControlEntity;
import com.games.api.enums.PaymentMethod;
import com.games.api.enums.RiskCacheName;
import com.games.common.core.redis.RedisCache;
import com.games.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PaymentCheckService {

    @Autowired
    RedisCache redisCache;

    /**
     *
     * @param times 统计times个小时内的数据
     * @param email 用户的邮箱
     * @param payMethod 支付方式，目前只有NUVEI
     * @param payAmountSum 验证支付的总金额，如果该字段不为null，需要统计对应时间段的数据，如果大于payAmountSum，代表需要风控
     * @param transactionCount 交易次数，如果该字段不为null，需要统计对应时间段的数据，如果大于transactionCount，代表需要风控
     * @param is3DSAuthenticated 是否是3ds认证，如果为true，代表只统计3ds的数据
     * @param riskControlEntity riskControlEntity
     * @return
     * @throws BusinessException
     */
    public boolean check(Integer times, String email, String payMethod, String payAmountSum, Integer transactionCount,
                         Integer is3DSAuthenticated, RiskControlEntity riskControlEntity) throws BusinessException {
        long now = System.currentTimeMillis();
        if (PaymentMethod.NUVEI.getName().equals(payMethod)) {
            BigDecimal payAmount = riskControlEntity.getPayAmount();
            List<String> keys;
            if (is3DSAuthenticated == 1) keys = Collections.singletonList(RiskCacheName.RISK_NUVEI_CARD_3DS.getName() + email);
            else if (is3DSAuthenticated == 0) keys = Collections.singletonList(RiskCacheName.RISK_NUVEI_CARD_2DS.getName() + email);
            else {
                keys = new ArrayList<>();
                keys.add(RiskCacheName.RISK_NUVEI_CARD_3DS.getName());
                keys.add(RiskCacheName.RISK_NUVEI_CARD_2DS.getName());
            }
            long min = now - times * 60 * 60 * 1000;

            if (payAmount != null && payAmountSum != null) {
                Double sum = 0D;
                for (String key : keys) {
                    sum += redisCache.sumZSetScoresInRange(key, min, now);
                }
                BigDecimal total = payAmount.add(BigDecimal.valueOf(sum));
                if (total.compareTo(BigDecimal.valueOf(Double.parseDouble(payAmountSum))) > 0) {
                    throw new BusinessException("The payment amount has exceeded the limit. Please try again later.");
                }
            }

            if (transactionCount != null) {
                Long count = 0L;
                for (String key : keys) {
                    count += redisCache.countZSetValuesInRange(key, min, now);
                }
                if (count >= transactionCount) {
                    throw new BusinessException("The transaction count has exceeded the limit. Please try again later.");
                }
            }
        }
        return true;
    }
}
