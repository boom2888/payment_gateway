package com.games.pay.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 企业客户公司对象 shop
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ShopAuditVo implements Serializable {

    @NotNull(message = "{merchant.id.not.empty}")
    private Long id;

    @NotNull(message = "{audit.status.not.empty}")
    private Integer businessProfileStatus;

    private BigDecimal onRampMin;

    private BigDecimal onRampMax;

    private BigDecimal offRampMin;

    private BigDecimal offRampMax;

    private Long merchantFeeModelId;

    private Long settlementCurrency;

    private Long is3dsOn;

    private String remark;
    private String station;
    private String descriptor;
    private String code;

    /** 支付渠道来源：NUVEI, WYNPAY, Shift4 */
    private String fromSource;
}
