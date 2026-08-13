package com.games.api.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "AML风控状态枚举")
public enum AmlStatus {

    //AML状态(0-已创建，1-待处理，2-已批准，3-已拒绝)
    @ApiModelProperty("审核通过")
    APPROVED("审核通过"),

    @ApiModelProperty("审核拒绝")
    REJECTED("审核拒绝"),

    @ApiModelProperty("待审核")
    PENDING("待审核"),

    @ApiModelProperty("暂停")
    HOLD("暂停");

    private final String description;

    AmlStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
