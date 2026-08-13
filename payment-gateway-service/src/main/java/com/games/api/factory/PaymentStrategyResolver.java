package com.games.api.factory;

import com.games.api.enums.PaymentProvider;
import com.games.payment.domain.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 支付策略解析器
 * 负责根据商户信息解析并返回对应的支付策略
 */
@Slf4j
@Component
public class PaymentStrategyResolver {

    @Autowired
    private PaymentFactory paymentFactory;

    /**
     * 根据商户信息获取支付策略
     * @param shop 商户信息
     * @return 支付策略实例
     */
    public PaymentStrategy resolve(Shop shop) {
        if (shop == null) {
            throw new IllegalArgumentException("Shop cannot be null");
        }

        PaymentProvider provider = resolveProvider(shop.getFromSource());
        String providerType = provider.getCode();

        log.debug("Resolving payment strategy for shop: {}, rawFromSource: {}, provider: {}, providerType: {}",
                shop.getId(), shop.getFromSource(), provider, providerType);

        return paymentFactory.getPaymentStrategy(providerType);
    }

    /**
     * 尝试从原始字符串解析支付供应商，兼容 code 与 枚举名
     */
    private PaymentProvider resolveProvider(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            log.warn("raw fromSource is blank, using default NUVEI");
            return PaymentProvider.NUVEI;
        }
        String s = raw.trim();
        // 先按 code 匹配（大小写不敏感）
        for (PaymentProvider p : PaymentProvider.values()) {
            if (p.getCode().equalsIgnoreCase(s)) {
                return p;
            }
        }
        // 再按枚举名匹配（大小写不敏感）
        try {
            return PaymentProvider.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException ex) {
            log.warn("Unknown provider '{}', using default NUVEI", raw);
            return PaymentProvider.NUVEI;
        }
    }

    /**
     * 检查是否支持指定来源字符串（code 或 枚举名）
     */
    public boolean supports(String rawFromSource) {
        PaymentProvider provider = resolveProvider(rawFromSource);
        return paymentFactory.supports(provider.getCode());
    }
}
