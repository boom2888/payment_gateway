package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.core.domain.entity.SysDictData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 客户钱包对象 customer_wallet
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("customer_wallet")
public class CustomerWallet  {

    public static CustomerWallet init(Long customerId, Long cryptoId, String walletAddress){
        CustomerWallet customerWallet = new CustomerWallet();
        customerWallet.setCustomerId(customerId);
        customerWallet.setCryptoId(cryptoId);
        customerWallet.setWalletAddress(walletAddress);
        customerWallet.setCreatedAt(new Date());
        customerWallet.setActive(0);
        return customerWallet;
    }

    private static final long serialVersionUID=1L;
    public SysDictData toDict() {
        SysDictData data = new SysDictData();
        data.setDictLabel(walletAddress);
        data.setDictValue(id.toString());
        return data;
    }

    /** 客户钱包ID */
    @TableId(value = "id")
    private Long id;

    /** SaaS客户ID */
    @Excel(name = "SaaS客户ID", langKey = "payment.customerWallet.excel.customerId")
    private Long customerId;

    /** 加密货币ID */
    @Excel(name = "加密货币ID", langKey = "payment.customerWallet.excel.cryptoId")
    private Long cryptoId;

    /** 钱包地址 */
    @Excel(name = "钱包地址", langKey = "payment.customerWallet.excel.walletAddress")
    private String walletAddress;

    /** 类型(0-激活，1-停用) */
    @Excel(name = "类型(0-激活，1-停用)", langKey = "payment.customerWallet.excel.active")
    private Integer active;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间", langKey = "payment.customerWallet.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID", langKey = "payment.customerWallet.excel.createdBy")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间", langKey = "payment.customerWallet.excel.deletedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID", langKey = "payment.customerWallet.excel.deletedBy")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)", langKey = "payment.customerWallet.excel.deleted")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述", langKey = "payment.customerWallet.excel.remark")
    private String remark;

    /** 钱包名称 */
    @Excel(name = "钱包名称", langKey = "payment.customerWallet.excel.walletName")
    private String walletName;

}
