package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
import java.util.Date;

/**
 * nuvei导入数据明细对象 movement_data
 *
 * @author Ticker
 * @date 2025-07-21
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("movement_data")
public class MovementData {

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel
    @TableId(value = "id")
    private Long id;

    /** $column.columnComment */
    @Excel(name = "Multi Client")
    private String multiClient;

    /** $column.columnComment */
    @Excel(name = "Client Name")
    private String clientName;

    /** $column.columnComment */
    @Excel(name = "Acquirer Bank")
    private String acquirerBank;

    /** $column.columnComment */
    @Excel(name = "Movement Date")
    private String movementDate;

    /** $column.columnComment */
    @Excel(name = "Movement Type")
    private String movementType;

    /** $column.columnComment */
    @Excel(name = "Movement Details")
    private String movementDetails;

    /** $column.columnComment */
    @Excel(name = "Payment Method")
    private String paymentMethod;

    /** $column.columnComment */
    @Excel(name = "Is 3D")
    @TableField("is_3d")
    private String is3d;

    /** $column.columnComment */
    @Excel(name = "Card Type")
    private String cardType;

    /** $column.columnComment */
    @Excel(name = "Country")
    private String country;

    /** $column.columnComment */
    @Excel(name = "Transaction Result")
    private String transactionResult;

    /** $column.columnComment */
    @Excel(name = "Transaction ID")
    private String transactionId;

    /** $column.columnComment */
    @Excel(name = "Credit Card Last 4 Digits")
    @TableField("credit_card_last_4_digits")
    private String creditCardLast4Digits;

    /** $column.columnComment */
    @Excel(name = "Client Unique ID")
    private String clientUniqueId;

    /** $column.columnComment */
    @Excel(name = "Currency")
    private String currency;

    /** $column.columnComment */
    @Excel(name = "Amount")
    private BigDecimal amount;

    private BigDecimal baseRate;
    private BigDecimal fxRate;

    private BigDecimal transferAmount;

    /** $column.columnComment */
    @Excel(name = "Discount Fee")
    private BigDecimal discountFee;

    /** $column.columnComment */
    @Excel(name = "Transaction Fee")
    private BigDecimal transactionFee;

    /** $column.columnComment */
    @Excel(name = "Service Fee")
    private BigDecimal serviceFee;

    /** $column.columnComment */
    @Excel(name = "Reserve")
    private BigDecimal reserve;

    /** $column.columnComment */
    @Excel(name = "VAT on Fees")
    private BigDecimal vatOnFees;

    /** $column.columnComment */
    @Excel(name = "Net Amount")
    private BigDecimal netAmount;

    /** $column.columnComment */
    @Excel(name = "Settlement Currency")
    private String settlementCurrency;

    /** $column.columnComment */
    @Excel(name = "Settlement Amount")
    private BigDecimal settlementAmount;

    /** $column.columnComment */
    @Excel(name = "Settlement Reserve")
    private BigDecimal settlementReserve;

    /** $column.columnComment */
    @Excel(name = "Jurisdiction")
    private String jurisdiction;

    /** $column.columnComment */
    @Excel(name = "Prepaid")
    private String prepaid;

    /** $column.columnComment */
    @Excel(name = "Plus Commission")
    private BigDecimal plusCommission;

    /** $column.columnComment */
    @Excel(name = "External Token Provider")
    private String externalTokenProvider;

    /** $column.columnComment */
    @Excel(name = "On Us")
    private String onUs;

    /** $column.columnComment */
    @Excel(name = "Order ID")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "Is Interchange ++")
    private String isInterchangePlusPlus;

    /** $column.columnComment */
    @Excel(name = "Reseller Fee")
    private BigDecimal resellerFee;

    /** $column.columnComment */
    @Excel(name = "Is Online Payout")
    private String isOnlinePayout;

    /** $column.columnComment */
    @Excel(name = "Movement ID")
    private String movementId;

    /** $column.columnComment */
    @Excel(name = "Scheme Fee")
    private BigDecimal schemeFee;

    /** $column.columnComment */
    @Excel(name = "Reseller Commission")
    private BigDecimal resellerCommission;

    /** $column.columnComment */
    @Excel(name = "Interchange Fee")
    private BigDecimal interchangeFee;

    /** $column.columnComment */
    @Excel(name = "Site ID")
    private Integer siteId;

    /** $column.columnComment */
    @Excel(name = "Site Name")
    private String siteName;

    /** $column.columnComment */
    @Excel(name = "Card Product")
    private String cardProduct;

    /** $column.columnComment */
    @Excel(name = "Custom Data")
    private String customData;

    /** $column.columnComment */
    @Excel(name = "ARN")
    private String arn;

    /** $column.columnComment */
    @Excel(name = "Payout ID")
    private Integer payoutId;

    /** $column.columnComment */
    @Excel(name = "Bank Transaction ID")
    private String bankTransactionId;

    /** $column.columnComment */
    @Excel(name = "Is AFT")
    private String isAft;

    /** $column.columnComment */
    @Excel(name = "Batch ID")
    private String batchId;

    /** $column.columnComment */
    @Excel(name = "Processing Channel")
    private String processingChannel;

    /** $column.columnComment */
    @Excel(name = "BIN")
    private Integer bin;

    /** $column.columnComment */
    @Excel(name = "Record Id")
    private Integer recordId;

    /** $column.columnComment */
    @Excel(name = "Is Currency Converted")
    private String isCurrencyConverted;

    /** $column.columnComment */
    @Excel(name = "Payment Sub Method")
    private String paymentSubMethod;

    /** $column.columnComment */
    @Excel(name = "End User Amount")
    private BigDecimal endUserAmount;

    /** $column.columnComment */
    @Excel(name = "End User Currency")
    private String endUserCurrency;

    /** 导入记录id */
    @Excel
    private Long movementImportId;

    private BigDecimal merchantProcessingFee;
    private BigDecimal merchantRollingReserve;
    private BigDecimal merchantGatewayFee;
    private BigDecimal merchantRefundFee;
    private BigDecimal merchantRdrFee;
    private BigDecimal merchantChargebackFee;
    private BigDecimal merchantSettlementFee;
    private String merchantSettlementRemark;

    @TableField(exist = false)
    private String businessName;

    @TableField(exist = false)
    private String merchantId;

    @TableField(exist = false)
    private BigDecimal cbFee;

    @TableField(exist = false)
    private BigDecimal refundFee;

    @TableField(exist = false)
    private BigDecimal rollingFee;

    @TableField(exist = false)
    private BigDecimal cryptoAmount;

    @TableField(exist = false)
    private Integer orderStatusRaw; // 对应 o.status

    @TableField(exist = false)
    private String cryptoId; // c.short_name

    @TableField(exist = false)
    private String regionFlag;

    @TableField(exist = false)
    private String ssoOrderId;

    @TableField(exist = false)
    private String feeType;

    @TableField(exist = false)
    private String currencyId;

    @TableField(exist = false)
    private Date createdAt;

    @TableField(exist = false)
    private BigDecimal b2c2FxRate;

    @TableField(exist = false)
    private BigDecimal fiatAmount;
}
