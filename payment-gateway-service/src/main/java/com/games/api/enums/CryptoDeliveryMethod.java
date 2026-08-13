package com.games.api.enums;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "加密货币发放方式")
public enum CryptoDeliveryMethod {

    @ApiModelProperty("平台自发放（如内部冷钱包转账）")
    HOUSE_DELIVERY(0, "平台发放"),

    @ApiModelProperty("第三方或链上直接交付")
    DIRECT_DELIVERY(1, "链上直发");

    private final int code;
    private final String desc;

    CryptoDeliveryMethod(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CryptoDeliveryMethod fromCode(int code) {
        for (CryptoDeliveryMethod method : CryptoDeliveryMethod.values()) {
            if (method.code == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unsupported CryptoDeliveryMethod: " + code);
    }
}
