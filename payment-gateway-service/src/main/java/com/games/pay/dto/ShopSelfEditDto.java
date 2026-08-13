package com.games.pay.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import com.games.common.core.domain.entity.SysDictData;
import com.games.payment.domain.Ubo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 企业客户公司对象 shop
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ShopSelfEditDto implements Serializable {

    private Long id;

    /** 商户订单状态webhook URL */
    private String notifyOrderStatusEndpoint;

    private String backOpen;

    private Integer is3dsOn;

    private String threeDSuccessUrl;
    private String threeDFailureUrl;
    private String defaultRedirectSuccessUrl;
    private String defaultRedirectFailureUrl;
}
