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
 * 收款记录对象 emi_collection_record
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("emi_collection_record")
public class EmiCollectionRecord  {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id")
    private Long id;

    /** 收款时间 */
    @Excel(name = "收款时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectionTime;

    /** 收款编号 */
    @Excel(name = "收款编号")
    private String receiptNumber;

    /** 申请金额 */
    @Excel(name = "申请金额")
    private String applicationAmount;

    /** 付款金额 */
    @Excel(name = "付款金额")
    private String paymentAmount;

    /** 入账金额 */
    @Excel(name = "入账金额")
    private String amountEntered;

    /** $column.columnComment */
    @Excel(name = "入账金额")
    private String paymentStatus;

}
