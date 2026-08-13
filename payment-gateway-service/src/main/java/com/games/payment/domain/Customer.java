package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.payment.enums.CustomerType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户对象 customer
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("customer")
public class Customer  {

private static final long serialVersionUID=1L;
    public static Customer init(Long shopId, String ssoCustomerId){
        Customer customer = new Customer();
        customer.setMerchantId(shopId);
        customer.setSsoCustomerId(ssoCustomerId);
        customer.setCreatedAt(new Date());
        customer.setSum90Day(BigDecimal.ZERO);
        customer.setType(CustomerType.PERSONAL.getCode());
        return customer;
    }



    /** SaaS客户ID */
    @TableId(value = "id")
    private Long id;

    /** 基本用户信息的用户ID */
    @Excel(name = "基本用户信息的用户ID", langKey = "payment.customer.excel.userId")
    private Long userId;

    /** 客户登录的SaaS用户ID */
    @Excel(name = "客户登录的SaaS用户ID", langKey = "payment.customer.excel.saasUserCorporationId")
    private Long saasUserCorporationId;

    /** 客户类型(0-个人，1-企业) */
    @Excel(name = "客户类型(0-个人，1-企业)", langKey = "payment.customer.excel.type")
    private Integer type;

    /** 指定客户经理 */
    @Excel(name = "指定客户经理", langKey = "payment.customer.excel.manager")
    private Long manager;

    /** 订阅级别ID */
    @Excel(name = "订阅级别ID", langKey = "payment.customer.excel.subscriptionModelId")
    private Long subscriptionModelId;

    /** 风险等级ID */
    @Excel(name = "风险等级ID", langKey = "payment.customer.excel.riskLevel")
    private Long riskLevel;

    /** 人类可读ID */
    @Excel(name = "人类可读ID", langKey = "payment.customer.excel.idRef")
    private String idRef;

    /** VIP ID */
    @Excel(name = "VIP ID", langKey = "payment.customer.excel.vipId")
    private Long vipId;

    /** 商户ID */
    @Excel(name = "商户ID", langKey = "payment.customer.excel.merchantId")
    private Long merchantId;

    /** SSO客户ID */
    @Excel(name = "SSO客户ID", langKey = "payment.customer.excel.ssoCustomerId")
    private String ssoCustomerId;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间", langKey = "payment.customer.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID", langKey = "payment.customer.excel.createdBy")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间", langKey = "payment.customer.excel.deletedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID", langKey = "payment.customer.excel.deletedBy")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)", langKey = "payment.customer.excel.deleted")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述", langKey = "payment.customer.excel.remark")
    private String remark;

    /** Fireblocks金库账户ID */
    @Excel(name = "Fireblocks金库账户ID", langKey = "payment.customer.excel.fireblocksVaultAccountId")
    private String fireblocksVaultAccountId;

    /** 90天内订单总额 */
    @Excel(name = "90天内订单总额", langKey = "payment.customer.excel.sum90Day")
    private BigDecimal sum90Day;

    /** 推荐码 */
    @Excel(name = "推荐码", langKey = "payment.customer.excel.referralCode")
    private String referralCode;

}
