package com.games.api.enums;


import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel(description = "订单类型枚举")
@Getter
public enum OrderType {

    @ApiModelProperty("韩国入金")
    CARA_ON_RAMP("6", "CARA_ON_RAMP"),

    @ApiModelProperty("法币买加密货币")
    ON_RAMP("0", "ON_RAMP"),

    @ApiModelProperty("卖出加密货币")
    OFF_RAMP("1", "OFF_RAMP"),

    @ApiModelProperty("NFT 买入")
    NFT_ON_RAMP("2", "NFT_ON_RAMP"),

    @ApiModelProperty("NFT 卖出")
    NFT_OFF_RAMP("3", "NFT_OFF_RAMP"),

    @ApiModelProperty("TOKEN 买入")
    TOKEN_ON_RAMP("4", "TOKEN_ON_RAMP"),

    @ApiModelProperty("TOKEN 卖出")
    TOKEN_OFF_RAMP("5", "TOKEN_OFF_RAMP");


    public static boolean isOffOrder(String orderType){
        return StrUtil.equalsAny(orderType, OFF_RAMP.name(), NFT_OFF_RAMP.name());
    }

    private final String code;
    private final String name;

    OrderType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static OrderType fromCode(String code) {
        for (OrderType type : OrderType.values()) {
            if (StrUtil.equals(type.code, code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported OrderType: " + code);
    }
}
