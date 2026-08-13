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
 * 客户地址对象 customer_address
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("customer_address")
public class CustomerAddress  {

private static final long serialVersionUID=1L;


    /** 客户地址ID */
    @TableId(value = "id")
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID", langKey = "payment.customerAddress.excel.customerId")
    private Long customerId;

    /** 地址ID */
    @Excel(name = "地址ID", langKey = "payment.customerAddress.excel.addressId")
    private Long addressId;

    /** 激活状态(0-激活，1-停用) */
    @Excel(name = "激活状态(0-激活，1-停用)", langKey = "payment.customerAddress.excel.activated")
    private Long activated;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间", langKey = "payment.customerAddress.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID", langKey = "payment.customerAddress.excel.createdBy")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间", langKey = "payment.customerAddress.excel.deletedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID", langKey = "payment.customerAddress.excel.deletedBy")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)", langKey = "payment.customerAddress.excel.deleted")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述", langKey = "payment.customerAddress.excel.remark")
    private String remark;

}
