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


// removed unused import
import java.util.Date;

/**
 * 付款记录对象 emi_payment_history
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("emi_payment_history")
public class EmiPaymentHistory  {

// removed unused serialVersionUID


    /** id */
    @TableId(value = "id")
    private Long id;

    /** 付款时间 */
    @Excel(name = "付款时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 体现编号 */
    @Excel(name = "体现编号")
    private String withdrawalNumber;

    /** 收款银行 */
    @Excel(name = "收款银行")
    private String beneficiaryBank;

    /** 收款人 */
    @Excel(name = "收款人")
    private String payee;

    /** 收款账号 */
    @Excel(name = "收款账号")
    private String paymentAccountNumber;

    /** 付款金额 */
    @Excel(name = "付款金额")
    private String paymentAmount;

    /** 到账金额 */
    @Excel(name = "到账金额")
    private String amountReceived;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 查询开始时间（非表字段） */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** 查询结束时间（非表字段） */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

}
