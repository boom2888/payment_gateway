package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * 出金客户钱包对象 off_ramping_customer_wallet
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("off_ramping_customer_wallet")
public class OffRampingCustomerWallet  {

private static final long serialVersionUID=1L;


    /** 订单ID */
    @TableId(value = "id")
    private Long id;

    /** 加密货币ID */
    @Excel(name = "加密货币ID", langKey = "payment.offRampingCustomerWallet.excel.cryptoId")
    private Long cryptoId;

    /** 钱包地址 */
    @Excel(name = "钱包地址", langKey = "payment.offRampingCustomerWallet.excel.walletAddress")
    private String walletAddress;

    /** SaaS客户ID */
    @Excel(name = "SaaS客户ID", langKey = "payment.offRampingCustomerWallet.excel.customerId")
    private Long customerId;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间", langKey = "payment.offRampingCustomerWallet.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID", langKey = "payment.offRampingCustomerWallet.excel.createdBy")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间", langKey = "payment.offRampingCustomerWallet.excel.deletedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID", langKey = "payment.offRampingCustomerWallet.excel.deletedBy")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)", langKey = "payment.offRampingCustomerWallet.excel.deleted")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述", langKey = "payment.offRampingCustomerWallet.excel.remark")
    private String remark;

    /** 钱包名称 */
    @Excel(name = "钱包名称", langKey = "payment.offRampingCustomerWallet.excel.walletName")
    private String walletName;

}
