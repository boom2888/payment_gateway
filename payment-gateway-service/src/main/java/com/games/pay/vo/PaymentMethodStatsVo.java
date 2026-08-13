package com.games.pay.vo;

import lombok.Data;
import java.util.List;

@Data
public class PaymentMethodStatsVo {
    private List<PaymentMethodVo> methods;
}
