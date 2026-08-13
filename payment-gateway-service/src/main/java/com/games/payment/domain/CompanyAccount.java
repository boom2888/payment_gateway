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
 * 公司账户对象 company_account
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("company_account")
public class CompanyAccount  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private String id;

    /** APM提供商账户ID */
    @Excel(name = "APM提供商账户ID", langKey = "payment.companyAccount.excel.accountId")
    private String accountId;

    /** 账户货币 */
    @Excel(name = "账户货币", langKey = "payment.companyAccount.excel.currency")
    private String currency;

    /** 记录插入时间 */
    @Excel(name = "记录插入时间", langKey = "payment.companyAccount.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间", langKey = "payment.companyAccount.excel.updatedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
