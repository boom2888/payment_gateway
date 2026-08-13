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
 * 订单用户信息对象 order_user_info
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_user_info")
public class OrderUserInfo  {

private static final long serialVersionUID=1L;


    /** 订单用户ID */
    @TableId(value = "id")
    private Long id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 电子邮件 */
    @Excel(name = "电子邮件")
    private String email;

    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 地址栏 */
    @Excel(name = "地址栏")
    private String addressLine1;

    /** 邮编 */
    @Excel(name = "邮编")
    private String zip;

}
