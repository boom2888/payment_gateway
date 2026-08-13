package com.games.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建订单参数")
public class TdsCallBackReq extends ApiBaseReq{

    @ApiModelProperty(value = "商户订单号", example = "TEST-0c8671", required = true)
    private String ssoOrderId;

    /**
     * 订单状态：
     * 0-创建，1-AML 开始，2-AML 暂停，3-AML 拒绝，
     * 4-待入金，5-处理中，6-入金失败，7-取消，8-已执行，9-完成
     */
    @ApiModelProperty(value = "订单状态", example = "4", required = true)
    private String status;

}
