package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.core.domain.entity.SysDictData;
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
 * 银行账户对象 bank_account
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("bank_account")
public class BankAccount  {

    private static final long serialVersionUID=1L;
    public SysDictData toDict() {
        SysDictData data = new SysDictData();
        data.setDictLabel(bankName);
        data.setDictValue(id.toString());
        return data;
    }

    /** 银行账户ID */
    @TableId(value = "id")
    private Long id;

    /** 银行名称 */
    @Excel(name = "银行名称")
    private String bankName;

    /** 银行地址 */
    @Excel(name = "银行地址")
    private Long bankAddressId;

    /** 银行排序代码 */
    @Excel(name = "银行排序代码")
    private String sortCode;

    /** 银行账户号码 */
    @Excel(name = "银行账户号码")
    private String accountNumber;

    /** 国际银行账户号码 */
    @Excel(name = "国际银行账户号码")
    private String iban;

    /** Swift代码 */
    @Excel(name = "Swift代码")
    private String bicSwift;

    /** 货币ID */
    @Excel(name = "货币ID")
    private Long currencyId;

    /** 银行账户类型(0-支票账户，1-储蓄账户) */
    @Excel(name = "银行账户类型(0-支票账户，1-储蓄账户)")
    private Long type;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

    /** 路由号码 */
    @Excel(name = "路由号码")
    private String routingNumber;

    /** 银行交易对手ID */
    @Excel(name = "银行交易对手ID")
    private String bankCounterpartyId;

    /** 银行发卡国家 */
    @Excel(name = "银行发卡国家")
    private String bankCountry;

    /** AUD的BSB代码 */
    @Excel(name = "AUD的BSB代码")
    private String bsbCode;

    /** INR的IFSC */
    @Excel(name = "INR的IFSC")
    private String ifsc;

    /** MXN的CLABE */
    @Excel(name = "MXN的CLABE")
    private String clabe;

    /** 交易对手地址邮政编码 */
    @Excel(name = "交易对手地址邮政编码")
    private String cptyAddressPostcode;

    /** 交易对手地址 */
    @Excel(name = "交易对手地址")
    private String cptyAddress;

    /** 交易对手国家 */
    @Excel(name = "交易对手国家")
    private String cptyCountry;

    /** 交易对手城市 */
    @Excel(name = "交易对手城市")
    private String cptyCity;

    /** 交易对手街道第一行 */
    @Excel(name = "交易对手街道第一行")
    private String cptyStreetLine1;

    /** 收款人名字 */
    @Excel(name = "收款人名字")
    private String firstName;

    /** 收款人姓氏 */
    @Excel(name = "收款人姓氏")
    private String lastName;

}
