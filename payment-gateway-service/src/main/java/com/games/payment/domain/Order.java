package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.api.vo.PayOrderVo;
import com.games.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单对象 order
 *
 * @author Ticker
 * @date 2025-07-09
 */
@NoArgsConstructor
@Accessors(chain = true)
@TableName("`order`")
@Slf4j
public class Order implements Serializable {

    private static final long serialVersionUID=1L;


    /** 订单ID */
    @TableId(value = "id")
    @Excel(name = "订单 ID", langKey = "payment.order.excel.orderId")
    private Long id;

    @TableField(exist = false)
    private List<Long> ids;

    /** 商户ID */
    @Excel(name = "商户 ID", langKey = "payment.order.excel.merchantId")
    private Long merchantId;

    @TableField(exist = false)
    @Excel(name = "商户名称", langKey = "payment.order.excel.merchantName")
    private String merchantName;

    /** 商户单号 */
    @Excel(name = "商户单号", langKey = "payment.order.excel.ssoOrderId")
    private String ssoOrderId;

    /** 法币金额 */
    @Excel(name = "法币金额", langKey = "payment.order.excel.fiatAmount")
    private BigDecimal fiatAmount;

    @TableField(exist = false)
    @Excel(name = "法币单位", langKey = "payment.order.excel.fiatCurrency")
    private String faitCurrency;

    /** 订单状态(0-已创建，1-AML开始，2-AML暂停，3-AML拒绝，4-等待存款，5-处理中，6-存款拒绝，7-取消订单，8-已完成，9-完成) */
    @Excel(name = "订单状态", langKey = "payment.order.excel.orderStatus", readConverterExp="0=已创建,1=AML开始,2=AML暂停,3=AML拒绝,4=等待存款,5=处理中,6=存款拒绝,7=取消订单,8=已完成,9=完成", dictType = "ORDER_STATUS")
    private String status;

    /** 记录创建时间 */
    @Excel(name = "创建时间", langKey = "payment.order.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 加密货币金额 */
    @Excel(name = "加密货币金额", langKey = "payment.order.excel.cryptoAmount")
    private BigDecimal cryptoAmount;

    @TableField(exist = false)
    @Excel(name = "加密货币单位", langKey = "payment.order.excel.cryptoCurrency")
    private String cryptoCurrency;

    @Excel(name = "渠道单号", langKey = "payment.order.excel.transactionId")
    private String transactionId;

    @Excel(name = "回调状态", langKey = "payment.order.excel.callbackStatus", dictType = "ORDER_CALLBACK_STATUS")
    private  String callbackStatus;

    /** 失败原因 */
    @Excel(name = "失败原因", langKey = "payment.order.excel.failReasons")
    private String failReasons;

    /** 支付IP地址 */
    @Excel(name = "支付IP地址", langKey = "payment.order.excel.paymentIp")
    private String paymentIp;

    /** SaaS客户ID */
    @Excel(name = "客户ID", langKey = "payment.order.excel.customerId")
    private Long customerId;

    /** 客户钱包ID */
    @Excel(name = "客户钱包ID", langKey = "payment.order.excel.customerWalletId")
    private Long customerWalletId;

    /** 出入类型（0-入金，1-出金） */
    @Excel(name = "出入类型", langKey = "payment.order.excel.type", dictType = "ORDER_TYPE")
    private String type;

    /** 法币ID */
    private Long currencyId;

    /** 加密货币ID */
    private Long cryptoId;

    /** 法币费用金额 */
    @Excel(name = "处理费用", langKey = "payment.order.excel.processingFee")
    private BigDecimal processingFee;

    /** 流动性报价 */
    @Excel(name = "货币换汇", langKey = "payment.order.excel.liquidityQuote")
    private BigDecimal liquidityQuote;

    /** 流动性提供商 */
    private String liquidityProvider;

    /** 渠道单号 */
    private String orderUuid;

    /** 订单参考号 */
    private String reference;

    /** 旅行规则状态（0：已创建，1：待处理，2：已批准，3：已拒绝） */
    private Long travelRuleStatus;

    /** AML ID */
    private String amlId;

    /** AML载荷 */
    private String amlPayload;

    /** AML评分 */
    private Integer amlScore;

    /** AML状态(0-已创建，1-待处理，2-已批准，3-已拒绝) */
    private Integer amlStatus;

    /** 创建记录的用户ID */
    private Long createdBy;

    /** 记录删除时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    private Long deleted;

    /** 记录描述 */
    private String remark;

    /** 旅行规则ID */
    private String travelRuleId;

    /** 关联config_mid表的mid_code */
    private String mid;

    /** OTC订单为1，非OTC为0 */
    private Integer isOtc;

    /** 是否包含费用（1：包含费用，0：排除费用） */
    @Excel(name = "包含费用", langKey = "payment.order.excel.includeFee", dictType = "ORDER_INCLUDE_FEE")
    private String includeFee;

    /** 标志指示订单金额是否已添加到90天总额中 */
    @TableField(value = "sum_90_checked")
    private Integer sum90Checked;

    /** 欺诈检查评分 */
    private String fraudScore;

    /** 网关费用金额 */
    @Excel(name = "网关费用金额", langKey = "payment.order.excel.gatewayFee")
    private BigDecimal gatewayFee;

    /** 推荐码 */
    private String referralCode;

    /** 银行账户ID */
    @Excel(name = "银行账户ID", langKey = "payment.order.excel.bankAccountId")
    private Long bankAccountId;

    /** ANI检查响应 */
    private String aniCode;

    /** 回滚状态标志  */
    private Long isRollback;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date callbackTime;

    private  Integer callbackNum;
    private  String callbackMsg;
    private  Integer rollingDate;
    
    @Excel(name = "流转保证金", langKey = "payment.order.excel.rollingFee")
    private  BigDecimal rollingFee;

    private  BigDecimal transferAmount;
    private  Integer settlementStatus;

    @Excel(name = "更新时间", langKey = "payment.order.excel.updatedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    private String acquirerId;
    private String userPaymentOptionId;

    private String relatedTransactionId;
    private String payInfo;

    @TableField(exist = false)
    @Excel(name = "卡号", langKey = "payment.order.excel.cardNumber")
    private String cardNumber;

    @Excel(name = "商品名称", langKey = "payment.order.excel.productName")
    @ApiModelProperty(value = "商品名称", example = "Good Nft")
    private String productName;

    private String paymentType;

    /** 支付参数 二维码支付的时候返回*/
    private String payParams;

    @TableField(exist = false)
    private PayOrderVo payInfoVo;

    @TableField(exist = false)
    @Excel(name = "钱包地址", langKey = "payment.order.excel.walletAddress")
    @ApiModelProperty("加密货币接收钱包地址")
    private String walletAddress;

    //上游渠道
    @TableField(exist = false)
    @Excel(name = "支付机构", langKey = "payment.order.excel.paymentInstitution")
    private String fromSource;

    /** 是否为退款订单（0-否，1-是） */
    @TableField(exist = false)
    @ApiModelProperty("是否为退款订单")
    private Integer isRefundOrder;

    /** 退款金额总和 */
    @TableField(exist = false)
    @Excel(name = "退款金额", langKey = "payment.order.excel.refundAmount")
    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    /** 查询条件：创建时间开始 */
    @TableField(exist = false)
    @ApiModelProperty("创建时间开始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /** 查询条件：创建时间结束 */
    @TableField(exist = false)
    @ApiModelProperty("创建时间结束")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public void setStatus(String status2) {
        setStatus(status2, "默认设置");
    }

    public void setStatus(String status2, String desc) {
//        log.info("设置订单{},状态为{},新订单状态为{}，备注为:{}", id, status, status2, desc);
        this.status = status2;
    }

    /**
     * 请求参数
     */
    @Setter
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    @Getter
    @TableField(exist = false)
    @Excel(name = "邮箱", langKey = "payment.order.excel.email")
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getFaitCurrency() {
        return faitCurrency;
    }

    public void setFaitCurrency(String faitCurrency) {
        this.faitCurrency = faitCurrency;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerWalletId() {
        return customerWalletId;
    }

    public void setCustomerWalletId(Long customerWalletId) {
        this.customerWalletId = customerWalletId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getFiatAmount() {
        return fiatAmount;
    }

    public void setFiatAmount(BigDecimal fiatAmount) {
        this.fiatAmount = fiatAmount;
    }

    public Long getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(Long cryptoId) {
        this.cryptoId = cryptoId;
    }

    public BigDecimal getCryptoAmount() {
        return cryptoAmount;
    }

    public void setCryptoAmount(BigDecimal cryptoAmount) {
        this.cryptoAmount = cryptoAmount;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public BigDecimal getLiquidityQuote() {
        return liquidityQuote;
    }

    public void setLiquidityQuote(BigDecimal liquidityQuote) {
        this.liquidityQuote = liquidityQuote;
    }

    public String getLiquidityProvider() {
        return liquidityProvider;
    }

    public void setLiquidityProvider(String liquidityProvider) {
        this.liquidityProvider = liquidityProvider;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Long getTravelRuleStatus() {
        return travelRuleStatus;
    }

    public void setTravelRuleStatus(Long travelRuleStatus) {
        this.travelRuleStatus = travelRuleStatus;
    }

    public String getStatus() {
        return status;
    }

    public String getAmlId() {
        return amlId;
    }

    public void setAmlId(String amlId) {
        this.amlId = amlId;
    }

    public String getAmlPayload() {
        return amlPayload;
    }

    public void setAmlPayload(String amlPayload) {
        this.amlPayload = amlPayload;
    }

    public Integer getAmlScore() {
        return amlScore;
    }

    public void setAmlScore(Integer amlScore) {
        this.amlScore = amlScore;
    }

    public Integer getAmlStatus() {
        return amlStatus;
    }

    public void setAmlStatus(Integer amlStatus) {
        this.amlStatus = amlStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Long getDeleted() {
        return deleted;
    }

    public void setDeleted(Long deleted) {
        this.deleted = deleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTravelRuleId() {
        return travelRuleId;
    }

    public void setTravelRuleId(String travelRuleId) {
        this.travelRuleId = travelRuleId;
    }

    public String getPaymentIp() {
        return paymentIp;
    }

    public void setPaymentIp(String paymentIp) {
        this.paymentIp = paymentIp;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getIsOtc() {
        return isOtc;
    }

    public void setIsOtc(Integer isOtc) {
        this.isOtc = isOtc;
    }

    public String getSsoOrderId() {
        return ssoOrderId;
    }

    public void setSsoOrderId(String ssoOrderId) {
        this.ssoOrderId = ssoOrderId;
    }

    public String getIncludeFee() {
        return includeFee;
    }

    public void setIncludeFee(String includeFee) {
        this.includeFee = includeFee;
    }

    public Integer getSum90Checked() {
        return sum90Checked;
    }

    public void setSum90Checked(Integer sum90Checked) {
        this.sum90Checked = sum90Checked;
    }

    public String getFraudScore() {
        return fraudScore;
    }

    public void setFraudScore(String fraudScore) {
        this.fraudScore = fraudScore;
    }

    public BigDecimal getGatewayFee() {
        return gatewayFee;
    }

    public void setGatewayFee(BigDecimal gatewayFee) {
        this.gatewayFee = gatewayFee;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getAniCode() {
        return aniCode;
    }

    public void setAniCode(String aniCode) {
        this.aniCode = aniCode;
    }

    public Long getIsRollback() {
        return isRollback;
    }

    public void setIsRollback(Long isRollback) {
        this.isRollback = isRollback;
    }

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(String callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public Integer getCallbackNum() {
        return callbackNum;
    }

    public void setCallbackNum(Integer callbackNum) {
        this.callbackNum = callbackNum;
    }

    public String getCallbackMsg() {
        return callbackMsg;
    }

    public void setCallbackMsg(String callbackMsg) {
        this.callbackMsg = callbackMsg;
    }

    public Integer getRollingDate() {
        return rollingDate;
    }

    public void setRollingDate(Integer rollingDate) {
        this.rollingDate = rollingDate;
    }

    public BigDecimal getRollingFee() {
        return rollingFee;
    }

    public void setRollingFee(BigDecimal rollingFee) {
        this.rollingFee = rollingFee;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Integer getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(Integer settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getFailReasons() {
        return failReasons;
    }

    public void setFailReasons(String failReasons) {
        this.failReasons = failReasons;
    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId;
    }

    public String getUserPaymentOptionId() {
        return userPaymentOptionId;
    }

    public void setUserPaymentOptionId(String userPaymentOptionId) {
        this.userPaymentOptionId = userPaymentOptionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRelatedTransactionId() {
        return relatedTransactionId;
    }

    public void setRelatedTransactionId(String relatedTransactionId) {
        this.relatedTransactionId = relatedTransactionId;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayParams() {
        return payParams;
    }

    public void setPayParams(String payParams) {
        this.payParams = payParams;
    }

    public PayOrderVo getPayInfoVo() {
        return payInfoVo;
    }

    public void setPayInfoVo(PayOrderVo payInfoVo) {
        this.payInfoVo = payInfoVo;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getFromSource() {
        return fromSource;
    }

    public void setFromSource(String fromSource) {
        this.fromSource = fromSource;
    }

    public Integer getIsRefundOrder() {
        return isRefundOrder;
    }

    public void setIsRefundOrder(Integer isRefundOrder) {
        this.isRefundOrder = isRefundOrder;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
