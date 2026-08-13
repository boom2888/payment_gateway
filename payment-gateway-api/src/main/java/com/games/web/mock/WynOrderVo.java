package com.games.web.mock;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.games.common.utils.SignUtil;
import com.games.payment.domain.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class WynOrderVo {

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty(value = "Order type, e.g. ON_RAMP, NFT_ON_RAMP", required = true, example = "ON_RAMP")
    private String orderType = "NFT_ON_RAMP";

    @NotBlank(message = "商户订单号(ssoOrderId)不能为空")
    @ApiModelProperty(value = "Merchant order ID", required = true, example = "merchant_order_id_1009")
    private String ssoOrderId;

    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @ApiModelProperty(value = "Order amount", required = true, example = "15")
    private String orderAmount;

    // ==== 货币字段 ====

    @NotBlank(message = "货币代码(currency)不能为空")
    @Pattern(regexp = "^[A-Z]{3}$", message = "货币代码格式不正确，应为3位大写字母")
    @ApiModelProperty(value = "Fiat currency code (3 uppercase letters)", required = true, example = "USD")
    private String currency;

    @NotBlank(message = "加密货币(cryptoCurrency)不能为空")
    @Pattern(regexp = "^[A-Z]{3,6}$", message = "加密货币格式不正确，应为大写字母")
    @ApiModelProperty(value = "Cryptocurrency code (uppercase)", required = true, example = "USDT")
    private String cryptoCurrency;

    @ApiModelProperty(value = "Product name", example = "Good Nft")
    private String productName;

    // ==== 客户字段 ====

    @NotBlank(message = "商户用户编号不能为空")
    @ApiModelProperty(value = "Merchant user ID", required = true, example = "merchant_user_id_1001")
    private String customerId;

    @NotBlank(message = "客户钱包地址不能为空")
    @ApiModelProperty(value = "Customer wallet address", required = true, example = "0x388C818CA8B9251b393131C08a736A67ccB19297")
    private String customerWallet;

    @NotBlank(message = "客户邮箱不能为空")
    @Email(message = "客户邮箱格式不正确")
    @ApiModelProperty(value = "Customer email", required = true, example = "jack@safapay.com")
    private String customerEmail = "jack@safapay.com";

    @NotBlank(message = "客户名字不能为空")
    @ApiModelProperty(value = "Customer first name", required = true, example = "Jecson")
    private String customerFirstName = "Jack";

    @NotBlank(message = "客户姓氏不能为空")
    @ApiModelProperty(value = "Customer last name", required = true, example = "Tom")
    private String customerLastName = "Tom";

    @NotBlank(message = "客户出生日期不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "出生日期格式错误，应为 YYYY-MM-DD")
    @ApiModelProperty(value = "Customer date of birth", required = true, example = "1990-01-01")
    private String customerDob = "1990-01-01";

    @NotBlank(message = "客户地址不能为空")
    @ApiModelProperty(value = "Customer address", required = true, example = "350 5th Ave")
    private String customerAddress = "350 5th Ave";

    @NotBlank(message = "客户城市不能为空")
    @ApiModelProperty(value = "Customer city", required = true, example = "New York")
    private String customerCity = "New York";

    @NotBlank(message = "客户国家不能为空")
    @Pattern(regexp = "^[A-Z]{2}$", message = "客户国家应为2位大写国家代码（如US）")
    @ApiModelProperty(value = "Customer country (2 uppercase-letter code)", required = true, example = "US")
    private String customerCountry = "GB";

    //    @NotBlank(message = "客户州不能为空")
    @ApiModelProperty(value = "Customer state", required = true, example = "NY")
    private String customerState="";

    @NotBlank(message = "客户邮编不能为空")
    @ApiModelProperty(value = "Customer ZIP code", required = true, example = "10118")
    private String customerZip = "EC4R 0AN";

    @ApiModelProperty(value = "Fee inclusion mode (EXCLUDE: excluded, INCLUDE: included, MERCHANT: merchant pays)", example = "INCLUDE")
    private String include = "INCLUDE";

    @NotBlank(message = "merchantId不能为空")
    @ApiModelProperty(value = "Merchant ID, use 1 for testing", required = true, example = "1")
    private String merchantId;

    /**
     * 不传的时候返回 json， 传 1 返回重定向结果
     */
    private Integer createType = 1;
    /** 签名 (商户私钥,用户编号customerId)*/
    private String sign;

    public WynOrderVo(Shop marketShop, BigDecimal amount, String currency){
        this.merchantId = marketShop.getId().toString();
        this.customerId = RandomUtil.randomString(8);

        boolean isNft = true;
        this.orderType = isNft ? "NFT_ON_RAMP" : "ON_RAMP";
//        this.customerEmail = RandomUtil.randomString(6) + "_noemail@gmail.com";
        this.customerWallet = RandomUtil.randomString(24);
        this.ssoOrderId = RandomUtil.randomString(16);
        this.orderAmount = amount.setScale(2, RoundingMode.UP).toString();
        this.currency = currency;
        this.cryptoCurrency = "USDC";
        this.productName = "Goods";
        this.sign = SignUtil.sign(marketShop.getMerchantHashSecretKey(),  this.ssoOrderId);
    }
}
