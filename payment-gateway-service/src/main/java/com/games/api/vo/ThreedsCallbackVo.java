package com.games.api.vo;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@ApiModel("Nuvei 3DS 支付回调对象")
@Data
public class ThreedsCallbackVo {

    @ApiModelProperty("未映射的扩展字段")
    private Map<String, Object> extras = new HashMap<>();

    @ApiModelProperty("PPP 状态")
    @JsonProperty("ppp_status")
    private String pppStatus;
    @ApiModelProperty("交易状态（APPROVED：成功，ERROR：错误，REDIRECT：跳转，DECLINED：拒绝）")
    @JsonProperty("Status")
    private String status;
    @ApiModelProperty("扩展错误码")
    @JsonProperty("ExErrCode")
    private String exErrCode;
    @ApiModelProperty("错误码")
    @JsonProperty("ErrCode")
    private String errCode;
    @ApiModelProperty("APM 错误码")
    @JsonProperty("errApmCode")
    private String errApmCode;
    @ApiModelProperty("APM 错误描述")
    @JsonProperty("errApmDescription")
    private String errApmDescription;
    @ApiModelProperty("SC 错误码")
    @JsonProperty("errScCode")
    private String errScCode;
    @ApiModelProperty("SC 错误描述")
    @JsonProperty("errScDescription")
    private String errScDescription;
    @ApiModelProperty("失败原因")
    @JsonProperty("Reason")
    private String reason;
    @ApiModelProperty("失败原因码")
    @JsonProperty("ReasonCode")
    private String reasonCode;
    @ApiModelProperty("PPP 交易 ID")
    @JsonProperty("ppp_TransactionID")
    private String pppTransactionID;
    @ApiModelProperty("用户 ID")
    @JsonProperty("userid")
    private String userid;
    @ApiModelProperty("商户订单号")
    @JsonProperty("merchant_unique_id")
    private String merchantUniqueId;
    @ApiModelProperty("自定义数据")
    @JsonProperty("customData")
    private String customData;
    @ApiModelProperty("产品 ID")
    @JsonProperty("productId")
    private String productId;
    @ApiModelProperty("用户名")
    @JsonProperty("first_name")
    private String firstName;
    @ApiModelProperty("用户姓氏")
    @JsonProperty("last_name")
    private String lastName;
    @ApiModelProperty("邮箱地址")
    @JsonProperty("email")
    private String email;
    @ApiModelProperty("支付币种")
    @JsonProperty("currency")
    private String currency;
    @ApiModelProperty("支付方式名称")
    @JsonProperty("pmDisplayName")
    private String pmDisplayName;
    @ApiModelProperty("地址行1")
    @JsonProperty("address1")
    private String address1;
    @ApiModelProperty("地址行2")
    @JsonProperty("address2")
    private String address2;
    @ApiModelProperty("国家代码")
    @JsonProperty("country")
    private String country;
    @ApiModelProperty("州/省")
    @JsonProperty("state")
    private String state;
    @ApiModelProperty("城市")
    @JsonProperty("city")
    private String city;
    @ApiModelProperty("邮编")
    @JsonProperty("zip")
    private String zip;
    @ApiModelProperty("联系电话1")
    @JsonProperty("phone1")
    private String phone1;
    @ApiModelProperty("联系电话2")
    @JsonProperty("phone2")
    private String phone2;
    @ApiModelProperty("联系电话3")
    @JsonProperty("phone3")
    private String phone3;
    @ApiModelProperty("客户端 IP")
    @JsonProperty("client_ip")
    private String clientIp;
    @ApiModelProperty("持卡人姓名")
    @JsonProperty("nameOnCard")
    private String nameOnCard;
    @ApiModelProperty("卡号")
    @JsonProperty("cardNumber")
    private String cardNumber;
    @ApiModelProperty("卡 BIN")
    @JsonProperty("bin")
    private String bin;
    @ApiModelProperty("是否未使用 CVV")
    @JsonProperty("noCVV")
    private String noCVV;
    @ApiModelProperty("收单行 ID")
    @JsonProperty("acquirerId")
    private String acquirerId;
    @ApiModelProperty("收单银行")
    @JsonProperty("acquirerBank")
    private String acquirerBank;
    @ApiModelProperty("卡过期月份")
    @JsonProperty("expMonth")
    private String expMonth;
    @ApiModelProperty("卡过期年份")
    @JsonProperty("expYear")
    private String expYear;
    @ApiModelProperty("卡 token ID")
    @JsonProperty("tokenId")
    private String tokenId;
    @ApiModelProperty("授权码")
    @JsonProperty("AuthCode")
    private String authCode;
    @ApiModelProperty("AVS 响应码")
    @JsonProperty("AvsCode")
    private String avsCode;
    @ApiModelProperty("CVV2 验证结果")
    @JsonProperty("Cvv2Reply")
    private String cvv2Reply;
    @ApiModelProperty("总折扣")
    @JsonProperty("total_discount")
    private String totalDiscount;
    @ApiModelProperty("总手续费")
    @JsonProperty("total_handling")
    private String totalHandling;
    @ApiModelProperty("总运费")
    @JsonProperty("total_shipping")
    private String totalShipping;
    @ApiModelProperty("总税费")
    @JsonProperty("total_tax")
    private String totalTax;
    @ApiModelProperty("Buy Button 产品组合 ID")
    @JsonProperty("buyButtonProductBundleId")
    private String buyButtonProductBundleId;
    @ApiModelProperty("商户站点 ID")
    @JsonProperty("merchant_site_id")
    private String merchantSiteId;
    @ApiModelProperty("商户状态")
    @JsonProperty("merchant_status")
    private String merchantStatus;
    @ApiModelProperty("动作类型")
    @JsonProperty("action")
    private String action;
    @ApiModelProperty("请求版本")
    @JsonProperty("requestVersion")
    private String requestVersion;
    @ApiModelProperty("消息")
    @JsonProperty("message")
    private String message;
    @ApiModelProperty("商户区域语言")
    @JsonProperty("merchantLocale")
    private String merchantLocale;
    @ApiModelProperty("未知参数")
    @JsonProperty("unknownParameters")
    private String unknownParameters;
    @ApiModelProperty("支付方式")
    @JsonProperty("payment_method")
    private String paymentMethod;
    @ApiModelProperty("唯一 ID")
    @JsonProperty("ID")
    private String id;
    @ApiModelProperty("商户 ID")
    @JsonProperty("merchant_id")
    private String merchantId;
    @ApiModelProperty("响应时间戳")
    @JsonProperty("responseTimeStamp")
    private String responseTimeStamp;
    @ApiModelProperty("Buy Button 产品 ID")
    @JsonProperty("buyButtonProductId")
    private String buyButtonProductId;
    @ApiModelProperty("网站管理员 ID")
    @JsonProperty("webMasterId")
    private String webMasterId;
    @ApiModelProperty("已应用的优惠")
    @JsonProperty("appliedPromotions")
    private String appliedPromotions;
    @ApiModelProperty("唯一信用卡标识")
    @JsonProperty("uniqueCC")
    private String uniqueCC;
    @ApiModelProperty("交易类型")
    @JsonProperty("transactionType")
    private String transactionType;
    @ApiModelProperty("外部邮箱")
    @JsonProperty("externalEmail")
    private String externalEmail;
    @ApiModelProperty("发卡组织")
    @JsonProperty("cardCompany")
    private String cardCompany;
    @ApiModelProperty("用户 token ID")
    @JsonProperty("user_token_id")
    private String userTokenId;
    @ApiModelProperty("用户支付选项 ID")
    @JsonProperty("userPaymentOptionId")
    private String userPaymentOptionId;
    @ApiModelProperty("交易 ID")
    @JsonProperty("TransactionID")
    private String transactionID;
    @ApiModelProperty("总金额")
    @JsonProperty("totalAmount")
    private String totalAmount;
    @ApiModelProperty("动态描述")
    @JsonProperty("dynamicDescriptor")
    private String dynamicDescriptor;
    @ApiModelProperty("手续费金额")
    @JsonProperty("feeAmount")
    private String feeAmount;
    @ApiModelProperty("门牌号")
    @JsonProperty("houseNumber")
    private String houseNumber;
    @ApiModelProperty("自定义币种")
    @JsonProperty("customCurrency")
    private String customCurrency;
    @ApiModelProperty("UPO 注册时间")
    @JsonProperty("upoRegistrationDate")
    private String upoRegistrationDate;
    @ApiModelProperty("类型")
    @JsonProperty("type")
    private String type;
    @ApiModelProperty("客户端请求 ID")
    @JsonProperty("clientRequestId")
    private String clientRequestId;
    @ApiModelProperty("关联交易 ID")
    @JsonProperty("relatedTransactionId")
    private String relatedTransactionId;
    @ApiModelProperty("卡号后四位")
    @JsonProperty("lastFourDigits")
    private String lastFourDigits;
    @ApiModelProperty("响应签名")
    @JsonProperty("responsechecksum")
    private String responsechecksum;
    @ApiModelProperty("预先响应签名")
    @JsonProperty("advanceResponseChecksum")
    private String advanceResponseChecksum;
    @ApiModelProperty("发卡行名称")
    @JsonProperty("issuerName")
    private String issuerName;
    @ApiModelProperty("客户端唯一 ID")
    @JsonProperty("clientUniqueId")
    private String clientUniqueId;
    @ApiModelProperty("3DS 验证状态")
    @JsonProperty("transStatus")
    private String transStatus;
    @JsonAnySetter
    public void captureExtra(String key, Object value) {
        extras.put(key, value);
    }
}
