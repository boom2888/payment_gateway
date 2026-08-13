package com.games.pay.vo;

import lombok.Data;
import java.util.List;

@Data
public class PaymentMethodVo {
    private String name;
    private Double value;
    private List<PaymentMethodVo> children;
}
