package com.games.nuvei;

import com.games.payment.mapper.OrderMapper;
import com.games.system.service.impl.SysConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Service
public class CardPaymentLimitService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SysConfigServiceImpl sysConfigService;

    /**
     * 检查用户是否超限（返回 true 表示超限，需要切换 token）
     *
     * @param customerId 用户ID
     * @param currentAmount 当前订单金额
     * @return true-超限，false-未超限
     */
    public boolean isOverLimit(Long customerId, BigDecimal currentAmount) {
        // 查询近7天该用户的累计支付金额
        BigDecimal last7DaysAmount = orderMapper.sumAmountByCustomerLast7Days(String.valueOf(customerId));

        if (last7DaysAmount == null) {
            last7DaysAmount = BigDecimal.ZERO;
        }

        // 判断：历史累计 + 当前金额 > 1000
        BigDecimal totalAmount = last7DaysAmount.add(currentAmount);
        BigDecimal limitAmount = BigDecimal.valueOf(sysConfigService.getLimitAmount());
        boolean overLimit = totalAmount.compareTo(limitAmount) > 0;

        if (overLimit) {
            log.info("用户 {} 近7天累计 {} + 当前 {} = {} 超过限额 {}",
                    customerId, last7DaysAmount, currentAmount, totalAmount, limitAmount);
        }

        return overLimit;
    }
}
