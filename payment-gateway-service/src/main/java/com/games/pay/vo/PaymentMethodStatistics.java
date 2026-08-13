package com.games.pay.vo;

import lombok.Data;

@Data
public class PaymentMethodStatistics {
    /**
     * 支付类型或卡类型
     */
    private String type;

    /**
     * 数量
     */
    private Integer count;
}
