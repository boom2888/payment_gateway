package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import com.games.common.core.domain.entity.SysDictData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 企业客户公司对象 shop
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    public SysDictData toDict() {
        SysDictData data = new SysDictData();
        data.setDictLabel(businessName);
        data.setDictValue(id.toString());
        data.setDictLabelZh(businessName);
        return data;
    }

    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 企业授权用户ID */
    @Excel(name = "企业授权用户ID", langKey = "payment.shop.excel.userId")
    private Long userId;

    /** 企业名称 */
    @Excel(name = "企业名称", langKey = "payment.shop.excel.businessName")
    private String businessName;

    /** Stripe账号 */
    @Excel(name = "Stripe账号", langKey = "payment.shop.excel.stripeAccount")
    private String stripeAccount;
    /** Stripe粉润类型  */
    private String stripeFeeType;
    /** Stripe粉润  */
    private BigDecimal stripeFee;


    /** 企业编号 */
    @Excel(name = "企业编号", langKey = "payment.shop.excel.businessNo")
    private String businessNo;

    /** 企业经营地址 */
    @Excel(name = "企业经营地址", langKey = "payment.shop.excel.businessAddress")
    private Long businessAddress;

    /** 企业所在国家 */
    @Excel(name = "企业所在国家", langKey = "payment.shop.excel.country")
    private String country;

    /** 企业行业 */
    @Excel(name = "企业行业", langKey = "payment.shop.excel.industry")
    private String industry;

    /** 企业副行业 */
    @Excel(name = "企业副行业", langKey = "payment.shop.excel.sideIndustry")
    private String sideIndustry;

    /** 企业注册日期 */
    @Excel(name = "企业注册日期", langKey = "payment.shop.excel.registrationDate", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registrationDate;

    /** 企业注册地址 */
    @Excel(name = "企业注册地址", langKey = "payment.shop.excel.registrationAddress")
    private Long registrationAddress;

    /** 企业股权结构 */
    @Excel(name = "企业股权结构", langKey = "payment.shop.excel.shareholdingStructure")
    private String shareholdingStructure;

    /** 企业年营业额 */
    @Excel(name = "企业年营业额", langKey = "payment.shop.excel.turnoverYear")
    private String turnoverYear;

    /** 企业周营业额 */
    @Excel(name = "企业周营业额", langKey = "payment.shop.excel.turnoverWeek")
    private String turnoverWeek;

    /** 企业站点 */
    @Excel(name = "企业站点", langKey = "payment.shop.excel.station")
    private String station;

    /** 企业网站 */
    @Excel(name = "企业网站", langKey = "payment.shop.excel.website")
    private String website;

    /** 企业供应商名称 */
    @Excel(name = "企业供应商名称", langKey = "payment.shop.excel.supplierName")
    private String supplierName;

    /** 企业供应商合同文件URL */
    @Excel(name = "企业供应商合同文件URL", langKey = "payment.shop.excel.supplierContracts")
    private String supplierContracts;

    /** 企业资料状态(0-待审核，1-已批准，2-已拒绝) */
    @Excel(name = "企业资料状态(0-待审核，1-已批准，2-已拒绝, 3-binderr审核中，4-binderr筛选失败)", langKey = "payment.shop.excel.businessProfileStatus", dictType = "BUSINESS_CUSTOMER_CORPORATION_BUSINESS_PROFILE_STATUS")
    private Integer businessProfileStatus;

    /** 董事资料状态 */
    @Excel(name = "董事资料状态", langKey = "payment.shop.excel.directorProfileStatus")
    private String directorProfileStatus;

    /** 董事文件状态 */
    @Excel(name = "董事文件状态", langKey = "payment.shop.excel.directorDocumentStatus")
    private String directorDocumentStatus;

    /** SaaS用户公司ID */
    @Excel(name = "SaaS用户公司ID", langKey = "payment.shop.excel.saasUserCorporationId")
    private Long saasUserCorporationId;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间", langKey = "payment.shop.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID", langKey = "payment.shop.excel.createdBy")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间", langKey = "payment.shop.excel.deletedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID", langKey = "payment.shop.excel.deletedBy")
    private Long deletedBy;

    /** 记录描述 */
    @Excel(name = "记录描述", langKey = "payment.shop.excel.remark")
    private String remark;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)", langKey = "payment.shop.excel.deleted")
    private Long deleted;

    /** 企业描述 */
    @Excel(name = "企业描述", langKey = "payment.shop.excel.businessDesc")
    private String businessDesc;

    /** 关联config_mid表的mid_code */
    @Excel(name = "关联config_mid表的mid_code", langKey = "payment.shop.excel.mid")
    private String mid;

    /** 请求加密货币交易端点 */
    @Excel(name = "请求加密货币交易端点", langKey = "payment.shop.excel.requestCryptoTransactionEndpoint")
    private String requestCryptoTransactionEndpoint;

    /** 商户加密货币交易API密钥 */
    @Excel(name = "商户加密货币交易API密钥", langKey = "payment.shop.excel.merchantHashSecretKey")
    private String merchantHashSecretKey;

    /** 商户订单状态webhook URL */
    @Excel(name = "商户订单状态webhook URL", langKey = "payment.shop.excel.notifyOrderStatusEndpoint")
    private String notifyOrderStatusEndpoint;

    /** 类型(0-关闭，1-开启) */
    @Excel(name = "类型(0-关闭，1-开启)", langKey = "payment.shop.excel.isSiftOn")
    @TableField("is_sift_on")
    private Long isSiftOn;

    /** 类型(0-关闭，1-开启) */
    @Excel(name = "类型(0-关闭，1-开启)", langKey = "payment.shop.excel.is3dsOn")
    @TableField("is_3ds_on")
    private Long is3dsOn;

    /** 加密货币交付方式(0-商户交付，1-平台交付) */
    @Excel(name = "加密货币交付方式(0-商户交付，1-平台交付)", langKey = "payment.shop.excel.cryptoDeliveryMethod")
    private Long cryptoDeliveryMethod;

    /** 直营或平台商户(0-商户，1-平台商户) */
    @Excel(name = "直营或平台商户(0-商户，1-平台商户)", langKey = "payment.shop.excel.isHouseMerchant")
    private Long isHouseMerchant;

    /** 第二个商户订单状态webhook URL */
    @Excel(name = "第二个商户订单状态webhook URL", langKey = "payment.shop.excel.notifyOrderStatusEndpoint2")
    @TableField("notify_order_status_endpoint_2")
    private String notifyOrderStatusEndpoint2;

    @TableField(value = "endpoint_2_percent", updateStrategy = com.baomidou.mybatisplus.annotation.FieldStrategy.ALWAYS)
    private Integer endpoint2Percent;

    /** 预留百分比 */
    @Excel(name = "预留百分比", langKey = "payment.shop.excel.reservePercentage")
    private BigDecimal reservePercentage;

    /** 预留持有期 */
    @Excel(name = "预留持有期", langKey = "payment.shop.excel.reserveHoldingPeriod")
    private Long reserveHoldingPeriod;

    /** 结算货币表主键 */
    @Excel(name = "结算货币表主键", langKey = "payment.shop.excel.settlementCurrency")
    private Long settlementCurrency;

    /** 利润分成 */
    @Excel(name = "利润分成", langKey = "payment.shop.excel.profitSharing")
    private String profitSharing;

    /** 仅白名单卡模式(0-否，1-是) */
    @Excel(name = "仅白名单卡模式(0-否，1-是)", langKey = "payment.shop.excel.isWhitelistOn")
    private Long isWhitelistOn;

    /** TG通知商户群组编号 */
    @Excel(name = "TG通知商户群组编号", langKey = "payment.shop.excel.notifyOrderStatusEndpoint3")
    @TableField("notify_order_status_endpoint_3")
    private String notifyOrderStatusEndpoint3;

    /** ANI检查标志 */
    @Excel(name = "ANI检查标志", langKey = "payment.shop.excel.aniCheck")
    private Long aniCheck;

    /** 收单机构ID */
    @Excel(name = "收单机构ID", langKey = "payment.shop.excel.acquirerId")
    private Long acquirerId;

    /** 加密货币交付延迟时间 */
    @Excel(name = "加密货币交付延迟时间", langKey = "payment.shop.excel.cryptoDeliveryDelayTime")
    private String cryptoDeliveryDelayTime;

    /** 订单状态变更通知Webhook URL */
    @Excel(name = "订单状态变更通知Webhook URL", langKey = "payment.shop.excel.orderStatusChangeNotification")
    private String orderStatusChangeNotification;

    /** 3DS成功重定向URL */
    @Excel(name = "3DS成功重定向URL", langKey = "payment.shop.excel.threeDSuccessUrl")
    private String threeDSuccessUrl;

    /** 3DS失败重定向URL */
    @Excel(name = "3DS失败重定向URL", langKey = "payment.shop.excel.threeDFailureUrl")
    private String threeDFailureUrl;

    /** 默认成功重定向URL */
    @Excel(name = "默认成功重定向URL", langKey = "payment.shop.excel.defaultRedirectSuccessUrl")
    private String defaultRedirectSuccessUrl;

    /** 默认失败重定向URL */
    @Excel(name = "默认失败重定向URL", langKey = "payment.shop.excel.defaultRedirectFailureUrl")
    private String defaultRedirectFailureUrl;

    /** 入金最小金额 */
    @Excel(name = "入金最小金额", langKey = "payment.shop.excel.onRampMin")
    private BigDecimal onRampMin;

    /** 入金最大金额 */
    @Excel(name = "入金最大金额", langKey = "payment.shop.excel.onRampMax")
    private BigDecimal onRampMax;

    /** 出金最小金额 */
    @Excel(name = "出金最小金额", langKey = "payment.shop.excel.offRampMin")
    private BigDecimal offRampMin;

    /** 出金最大金额 */
    @Excel(name = "出金最大金额", langKey = "payment.shop.excel.offRampMax")
    private BigDecimal offRampMax;

    /** 增值税编号 */
    @TableField("vat_number")
    private String vatNumber;

    private Long merchantFeeModelId;
    private String companyAuthType;
    private String companyAuthTypeUrl;
    private String companyDocUrl;
    private String directorName;
    private String directorEmail;
    private String backOpen;
    // 上游渠道
    private String fromSource; // FromSource
    // 渠道商户标识 QBSG*Expedilion 格式
    private String descriptor;

    /** Thunes转账手续费 */
    @Excel(name = "Thunes转账手续费", langKey = "payment.shop.excel.thunesTransferFee")
    private BigDecimal thunesTransferFee;

    private Integer isRedirect;

    // 创建 Binderr 实体的 id
    @TableField("entity_id")
    private Long entityId;

    /**
     * 受益人认证信息
     */
    @TableField(exist = false)
    private List<Ubo> ubos;

    @TableField(exist = false)
    private String userName;

    /**
     * 0 是启用，1 是禁用
     */
    @TableField(exist = false)
    private Integer status;

    @TableField(exist = false)
    private String code;

    private String notifyOrderStatusEndpointNft;

    @TableField(exist = false)
    private String password;

    @TableField(exist = false)
    private Integer partnerId;

    /** 父商户ID（用于二级商户） */
    @Excel(name = "父商户ID", langKey = "payment.shop.excel.parentId")
    private Long parentId;

    /** 临时字段：用户账号名（用于新增二级商户时创建账号） */
    @TableField(exist = false)
    private String accountName;

    /** 临时字段：用户密码（用于新增二级商户时创建账号） */
    @TableField(exist = false)
    private String accountPassword;

    /** 临时字段：类型过滤（1-全部，2-只显示有parentId的商户） */
    @TableField(exist = false)
    private Integer type;

    /** 查询起始时间（用于列表条件） */
    @TableField(exist = false)
    private String beginTime;

    /** 查询结束时间（用于列表条件） */
    @TableField(exist = false)
    private String endTime;
}
