package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import com.games.thunes.dto.TransactionData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Thunes 转账记录对象 thunes_transfer_record
 *
 * @author System
 * @date 2025-01-08
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("thunes_transfer_record")
public class ThunesTransferRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 商户ID */
    @Excel(name = "商户ID", langKey = "thunes.transfer.excel.merchantId")
    private Long merchantId;

    /** 外部订单号 */
    @Excel(name = "外部订单号", langKey = "thunes.transfer.excel.externalId")
    private String externalId;

    /** 交易类型 */
    @Excel(name = "交易类型", langKey = "thunes.transfer.excel.transactionType")
    private String transactionType;

    /** 转账金额 */
    @Excel(name = "转账金额", langKey = "thunes.transfer.excel.amount")
    private BigDecimal amount;

    /** 转账货币 */
    @Excel(name = "转账货币", langKey = "thunes.transfer.excel.currency")
    private String currency;

    /** 目标国家代码 */
    @Excel(name = "目标国家代码", langKey = "thunes.transfer.excel.targetCountry")
    private String targetCountry;

    /** 目标货币 */
    @Excel(name = "目标货币", langKey = "thunes.transfer.excel.targetCurrency")
    private String targetCurrency;

    /** 目标金额 */
    @Excel(name = "目标金额", langKey = "thunes.transfer.excel.targetAmount")
    private BigDecimal targetAmount;

    /** 汇率 */
    @Excel(name = "汇率", langKey = "thunes.transfer.excel.exchangeRate")
    private BigDecimal exchangeRate;

    /** 平台手续费 */
    @Excel(name = "平台手续费", langKey = "thunes.transfer.excel.handlingFee")
    private BigDecimal handlingFee;

    /** 渠道手续费（成本） */
    @Excel(name = "渠道手续费", langKey = "thunes.transfer.excel.feeAmount")
    private BigDecimal feeAmount;

    /** 手续费货币 */
    @Excel(name = "手续费货币", langKey = "thunes.transfer.excel.feeCurrency")
    private String feeCurrency;

    /** Thunes报价ID */
    @Excel(name = "Thunes报价ID", langKey = "thunes.transfer.excel.quotationId")
    private String quotationId;

    /** Thunes交易ID */
    @Excel(name = "Thunes交易ID", langKey = "thunes.transfer.excel.transactionId")
    private String transactionId;

    /** Thunes支付方ID */
    @Excel(name = "Thunes支付方ID", langKey = "thunes.transfer.excel.payerId")
    private Long payerId;

    /** 内部状态 */
    @Excel(name = "内部状态", langKey = "thunes.transfer.excel.status")
    private String status;

    /** Thunes状态 */
    @Excel(name = "Thunes状态", langKey = "thunes.transfer.excel.thunesStatus")
    private String thunesStatus;

    /** 失败原因 */
    @Excel(name = "失败原因", langKey = "thunes.transfer.excel.failReason")
    private String failReason;

    /** 商户回调地址 */
    private String callbackUrl;

    /** 交易动态数据 */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TransactionData transactionData;

    /** 创建时间 */
    @Excel(name = "创建时间", langKey = "thunes.transfer.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间", langKey = "thunes.transfer.excel.updatedAt", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 回调接收时间 */
    @Excel(name = "回调接收时间", langKey = "thunes.transfer.excel.callbackReceivedAt", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date callbackReceivedAt;

    /**
     * 获取收款方名称（用于显示）
     */
    public String getBeneficiaryName() {
        if (transactionData == null || transactionData.getBeneficiary() == null) {
            return null;
        }

        TransactionData.PartyInfo beneficiary = transactionData.getBeneficiary();
        if ("INDIVIDUAL".equals(beneficiary.getType()) && beneficiary.getIndividual() != null) {
            TransactionData.IndividualInfo individual = beneficiary.getIndividual();
            return (individual.getFirstName() + " " + individual.getLastName()).trim();
        } else if ("BUSINESS".equals(beneficiary.getType()) && beneficiary.getBusiness() != null) {
            return beneficiary.getBusiness().getRegisteredName();
        }

        return null;
    }

    /**
     * 获取收款方账号（用于显示）
     */
    public String getBeneficiaryAccount() {
        if (transactionData == null || transactionData.getPaymentMethod() == null) {
            return null;
        }

        TransactionData.PaymentMethodInfo paymentMethod = transactionData.getPaymentMethod();
        if ("BANK_ACCOUNT".equals(paymentMethod.getType()) && paymentMethod.getBankAccount() != null) {
            return paymentMethod.getBankAccount().getBankAccountNumber();
        } else if ("MOBILE_WALLET".equals(paymentMethod.getType()) && paymentMethod.getMobileWallet() != null) {
            return paymentMethod.getMobileWallet().getPhoneNumber();
        }

        return null;
    }

    /**
     * 获取收款方地址（用于显示）
     */
    public String getBeneficiaryAddress() {
        if (transactionData == null || transactionData.getBeneficiary() == null) {
            return null;
        }

        TransactionData.PartyInfo beneficiary = transactionData.getBeneficiary();
        if ("INDIVIDUAL".equals(beneficiary.getType()) && beneficiary.getIndividual() != null) {
            return beneficiary.getIndividual().getAddress();
        } else if ("BUSINESS".equals(beneficiary.getType()) && beneficiary.getBusiness() != null) {
            return beneficiary.getBusiness().getAddress();
        }

        return null;
    }
}
