package com.games.wynpay.swap.vo;

import com.games.payment.domain.Shop;
import com.games.wynpay.swap.domain.SwapOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.RoundingMode;

@Data
public class WynOrderVo {

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型, 例如: ON_RAMP, NFT_ON_RAMP", required = true, example = "ON_RAMP")
    private String orderType = "NFT_ON_RAMP";

    @NotBlank(message = "商户订单号(ssoOrderId)不能为空")
    @ApiModelProperty(value = "商户订单号", required = true, example = "merchant_order_id_1009")
    private String ssoOrderId;

    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @ApiModelProperty(value = "订单金额", required = true, example = "15")
    private String orderAmount;

    // ==== 货币字段 ====

    @NotBlank(message = "货币代码(currency)不能为空")
    @Pattern(regexp = "^[A-Z]{3}$", message = "货币代码格式不正确，应为3位大写字母")
    @ApiModelProperty(value = "法币币种代码 (3位大写)", required = true, example = "USD")
    private String currency;

    @NotBlank(message = "加密货币(cryptoCurrency)不能为空")
    @Pattern(regexp = "^[A-Z]{3,6}$", message = "加密货币格式不正确，应为大写字母")
    @ApiModelProperty(value = "加密货币币种代码 (大写)", required = true, example = "USDT")
    private String cryptoCurrency;

    @ApiModelProperty(value = "产品名称", example = "Good Nft")
    private String productName;

    // ==== 客户字段 ====

    @NotBlank(message = "商户用户编号不能为空")
    @ApiModelProperty(value = "商户用户编号", required = true, example = "merchant_user_id_1001")
    private String customerId;

    @NotBlank(message = "客户钱包地址不能为空")
    @ApiModelProperty(value = "客户钱包地址", required = true, example = "0x388C818CA8B9251b393131C08a736A67ccB19297")
    private String customerWallet;

    @NotBlank(message = "客户邮箱不能为空")
    @Email(message = "客户邮箱格式不正确")
    @ApiModelProperty(value = "客户邮箱", required = true, example = "jack@safapay.com")
    private String customerEmail = "jack@safapay.com";

    @NotBlank(message = "客户名字不能为空")
    @ApiModelProperty(value = "客户名字", required = true, example = "Jecson")
    private String customerFirstName = "Jack";

    @NotBlank(message = "客户姓氏不能为空")
    @ApiModelProperty(value = "客户姓氏", required = true, example = "Tom")
    private String customerLastName = "Tom";

    @NotBlank(message = "客户出生日期不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "出生日期格式错误，应为 YYYY-MM-DD")
    @ApiModelProperty(value = "客户出生日期", required = true, example = "1990-01-01")
    private String customerDob = "1990-01-01";

    @NotBlank(message = "客户地址不能为空")
    @ApiModelProperty(value = "客户地址", required = true, example = "350 5th Ave")
    private String customerAddress = "350 5th Ave";

    @NotBlank(message = "客户城市不能为空")
    @ApiModelProperty(value = "客户城市", required = true, example = "New York")
    private String customerCity = "New York";

    @NotBlank(message = "客户国家不能为空")
    @Pattern(regexp = "^[A-Z]{2}$", message = "客户国家应为2位大写国家代码（如US）")
    @ApiModelProperty(value = "客户国家(2位大写代码)", required = true, example = "US")
    private String customerCountry = "GB";

    //    @NotBlank(message = "客户州不能为空")
    @ApiModelProperty(value = "客户州", required = true, example = "NY")
    private String customerState="";

    @NotBlank(message = "客户邮编不能为空")
    @ApiModelProperty(value = "客户邮编", required = true, example = "10118")
    private String customerZip = "EC4R 0AN";

    @ApiModelProperty(value = "是否包含手续费(EXCLUDE:不包含, INCLUDE:包含，MERCHANT:商户出)", example = "INCLUDE")
    private String include = "INCLUDE";

    @NotBlank(message = "merchantId不能为空")
    @ApiModelProperty(value = "商户ID, 测试时候用1", required = true, example = "1")
    private String merchantId;


    /** 签名 (商户私钥,用户编号customerId)*/
    private String sign;

    public WynOrderVo(SwapOrder payment, Shop marketShop){
        this.merchantId = marketShop.getId()+"";
        this.customerId = payment.getAddress();
        this.orderType =  "ON_RAMP";
//        this.customerEmail = RandomUtil.randomString(6) + "_noemail@gmail.com";
        this.customerWallet = payment.getAddress();
        this.ssoOrderId = "swap_" + payment.getId();
        this.orderAmount = payment.getAmount().setScale(2, RoundingMode.UP).toString();
        this.currency = payment.getCurrency();
        this.cryptoCurrency = payment.getCrypto();
        this.productName = payment.getCrypto();
        this.sign = com.games.common.utils.SignUtil.sign(marketShop.getMerchantHashSecretKey(),  this.ssoOrderId);
    }
}
