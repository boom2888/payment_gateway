package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.util.Date;
import java.util.List;

/**
 * 订单管理对象 emi_order_management
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("emi_order_management")
public class EmiOrderManagement  {


    /** id */
    @TableId(value = "id")
    private Long id;

    /** 订单时间 */
    @Excel(name = "订单时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNumber;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private String orderAmount;

    /** 剩余额度 */
    @Excel(name = "剩余额度")
    private String remainingBalance;

    /** 国家 */
    @Excel(name = "国家")
    private String nation;

    /** 贸易方式 */
    @Excel(name = "贸易方式")
    private String tradeMethod;

    /** 买家名称 */
    @Excel(name = "买家名称")
    private String buyerName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    // --- 查询辅助字段，不入库 ---
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 付款人名称 */
    @Excel(name = "付款人名称")
    private String payerName;

    /** 付款账户名称 */
    @Excel(name = "付款账户名称")
    private String paymentAccountName;


    /** 付款账号 */
    @Excel(name = "付款账号")
    private String paymentAccount;

    /** 付款银行 */
    @Excel(name = "付款银行")
    private String paymentBank;

    /** 创建数据的卡号 */
    @Excel(name = "创建数据的卡号")
    private String account;

    // --- 查询辅助字段，不入库 ---
    @TableField(exist = false)
    private List<String> userCardNumbers;

}
