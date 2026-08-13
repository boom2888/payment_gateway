package com.games.payment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 付款方列对象 emi_payer_table
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("emi_payer_table")
public class EmiPayerTable  {
    

    /** id */
    @TableId(value = "id")
    private Long id;

    /** 付款人名称 */
    @Excel(name = "付款人名称")
    private String payerName;

    /** 付款账户名称 */
    @Excel(name = "付款账户名称")
    private String paymentAccountName;

    /** 汇出地区 */
    @Excel(name = "汇出地区")
    private String remitArea;

    /** 付款账号 */
    @Excel(name = "付款账号")
    private String paymentAccount;

    /** 付款银行 */
    @Excel(name = "付款银行")
    private String paymentBank;

    /** use */
    @Excel(name = "use")
    private String use;

    /** 个人或者企业 */
    @Excel(name = "个人或者企业")
    private String type;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

}
