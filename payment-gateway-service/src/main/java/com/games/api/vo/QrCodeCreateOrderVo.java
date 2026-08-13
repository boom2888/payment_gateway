package com.games.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.games.common.exception.BusinessException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Data
@ApiModel("创建订单参数")
public class QrCodeCreateOrderVo extends ApiBaseReq{
    public void check() {
        // 校验 orderAmount 格式和范围
        try {
            if (orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("订单金额必须大于0");
            }
        } catch (NumberFormatException e) {
            throw BusinessException.api("order.amount.format.error");
        }
        // 校验 currency 格式为 3 位大写字母
        if (!currency.matches("^[A-Z]{3}$")) {
            throw BusinessException.api("currency.code.format.error");
        }
    }

    @NotBlank(message = "{merchant.order.id.not.empty}")
    @ApiModelProperty(value = "商户订单号", required = true, example = "merchant_order_id_1009")
    private String ssoOrderId;

    @NotNull(message = "{order.amount.not.empty}")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @ApiModelProperty(value = "订单金额", required = true, example = "15")
    private BigDecimal orderAmount;

    // ==== 货币字段 ====

    @NotBlank(message = "{currency.code.not.empty}")
    @Pattern(regexp = "^[A-Z]{3}$", message = "货币代码格式不正确，应为3位大写字母")
    @ApiModelProperty(value = "法币币种代码 (3位大写)", required = true, example = "KRW")
    private String currency;

    @ApiModelProperty(value = "商品名称", required = true, example = "测试商品")
    private String productName;

    @ApiModelProperty(value = "商品名称", required = true, example = "测试商品描述")
    private String productDesc;

    // ==== 客户字段 ====
    @NotBlank(message = "{merchant.user.id.not.empty}")
    @ApiModelProperty(value = "商户用户编号", required = true, example = "merchant_user_id_1001")
    private String customerId;

    @ApiModelProperty(value = "客户端ip", required = true, example = "210.73.10.148")
    private String ipAddress;
    /**
     * 客户端设备
     * 示例: pc或mobile
     */
    @ApiModelProperty(value = "客户端设备（pc或mobile）", required = true, example = "pc")
    private String device;
    /**
     * Product ID: 9009  /kakaopay
     * Product ID： 9011  /NAVERPAY
     *  Product ID： 9022  /Toss Pay
     *  Product ID： 9023  /PAYCO
     *  Product ID： 9019  /Korean_Local_Card
     */
    @ApiModelProperty(value = "支付产品ID", required = true, example = "9009")
    private Integer productId;

    /**
     * 特定渠道发起时额外参数
     * 示例: {"openId":"o2RvowBf7sOVJf8kJksUEMceaDqo"} visa通道：{"cardNo":"4367000000000000","expDate":"0629","cvv":"123"}
     */
    @ApiModelProperty(value = "特定渠道发起时额外参数", required = false, example = "{\"openId\":\"o2RvowBf7sOVJf8kJksUEMceaDqo\"} visa通道：{\"cardNo\":\"4367000000000000\",\"expDate\":\"0629\",\"cvv\":\"123\"}")
    private String extra;


    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public String getSignParams() {
        return ssoOrderId;
    }
}
