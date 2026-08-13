package com.games.api.factory;

import com.games.api.enums.PaymentProvider;
import com.games.common.exception.BusinessException;
import com.games.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付工厂
 * 负责根据支付供应商类型创建对应的支付策略实例
 */
@Slf4j
@Component
public class PaymentFactory {
    
    @Autowired
    private List<PaymentStrategy> paymentStrategies;
    
    private final Map<String, PaymentStrategy> strategyMap = new HashMap<>();
    
    /**
     * 初始化支付策略映射
     */
    @PostConstruct
    public void initStrategies() {
        for (PaymentStrategy strategy : paymentStrategies) {
            String providerType = strategy.getProviderType();
            strategyMap.put(providerType, strategy);
            log.info(MessageUtils.message("payment.strategy.registered", providerType, strategy.getClass().getSimpleName()));
        }
        log.info(MessageUtils.message("payment.strategy.init.completed", strategyMap.size()));
    }
    
    /**
     * 根据支付供应商类型获取支付策略
     * @param providerType 支付供应商类型
     * @return 支付策略实例
     */
    public PaymentStrategy getPaymentStrategy(String providerType) {
        PaymentStrategy strategy = strategyMap.get(providerType);
        if (strategy == null) {
            throw BusinessException.api("payment.provider.not.supported");
        }
        return strategy;
    }
    
    /**
     * 根据支付供应商枚举获取支付策略
     * @param provider 支付供应商枚举
     * @return 支付策略实例
     */
    public PaymentStrategy getPaymentStrategy(PaymentProvider provider) {
        return getPaymentStrategy(provider.getCode());
    }
    
    /**
     * 检查是否支持指定的支付供应商
     * @param providerType 支付供应商类型
     * @return 是否支持
     */
    public boolean supports(String providerType) {
        return strategyMap.containsKey(providerType);
    }
    
    /**
     * 获取所有支持的支付供应商类型
     * @return 支持的支付供应商类型列表
     */
    public String[] getSupportedProviders() {
        return strategyMap.keySet().toArray(new String[0]);
    }
}
