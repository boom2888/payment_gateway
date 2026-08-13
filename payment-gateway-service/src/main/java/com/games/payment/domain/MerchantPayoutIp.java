package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户支付IP对象 merchant_payout_ip
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_payout_ip")
public class MerchantPayoutIp  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private String id;

    /** 商户ID */
    @Excel(name = "商户ID")
    private String merchantId;

    /** IP白名单 */
    @Excel(name = "IP白名单")
    private String payoutIps;

    /** 记录插入时间 */
    @Excel(name = "记录插入时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
