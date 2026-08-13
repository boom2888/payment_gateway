package com.games.api.enums;


import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "是否包含手续费选项")
public enum FeeIncludeOption {

    @ApiModelProperty("不包含手续费")
    EXCLUDE("0", "不包含"),

    @ApiModelProperty("包含手续费")
    INCLUDE("1", "包含"),

    @ApiModelProperty("商户出手续费")
    MERCHANT("2", "商户出");

    private final String code;
    private final String desc;

    FeeIncludeOption(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static FeeIncludeOption fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (FeeIncludeOption opt : values()) {
            if (StrUtil.equals(opt.code, code)) {
                return opt;
            }
        }
        throw new IllegalArgumentException("Unsupported FeeIncludeOption: " + code);
    }
}
