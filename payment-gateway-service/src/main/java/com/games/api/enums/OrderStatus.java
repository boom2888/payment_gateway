package com.games.api.enums;


import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ApiModel(description = "订单状态枚举")
public enum OrderStatus {

//    @ApiModelProperty("0 - 创建")
//    CREATED("0", "创建"),
//
//    @ApiModelProperty("1 - AML 开始")
//    AML_START("1", "AML 开始"),
//
//    @ApiModelProperty("2 - AML 暂停")
//    AML_HOLD("2", "AML 暂停"),
//
//    @ApiModelProperty("3 - AML 拒绝")
//    AML_REJECTED("3", "AML 拒绝"),

    @ApiModelProperty("4 - 待入金")
    WAITING_FOR_DEPOSIT("4", "待入金"),

    @ApiModelProperty("5 - 处理中")
    APPROVED("5", "处理中"),

    @ApiModelProperty("6 - 入金失败")
    DEPOSIT_DECLINED("6", "入金失败"),

    @ApiModelProperty("7 - 已取消")
    CANCELED("7", "已取消"),

    @ApiModelProperty("8 - 已执行")
    FULLFILLED("8", "已执行"),

    @ApiModelProperty("9 - 完成")
    COMPLETED("9", "完成"),
    CLOSE_REFUND("10", "完成"),
    CLOSE_DECLINE("13", "完成"),

    TDS_PENDING("17", "3DS认证中"),
    CREDITS("20", "已退款"),
    CB_CANCELLED("30", "拒付取消"),
    RDR("40", "争议交易"),
    ;

    public static Set<OrderStatus> canPayState(){
        Set<OrderStatus> handleSets = new HashSet<>();
        handleSets.add(WAITING_FOR_DEPOSIT);
        handleSets.add(DEPOSIT_DECLINED);
        return handleSets;
    }

    public static Set<OrderStatus> canRefundState(){
        Set<OrderStatus> handleSets = new HashSet<>();
        handleSets.add(COMPLETED);
        return handleSets;
    }

    public static List<String> getSuccessStatus() {
        List<String> successStatus = new ArrayList<>();
        successStatus.add(OrderStatus.COMPLETED.getCode());
        return successStatus;
    }

    public static List<String> getFailStatus() {
        List<String> successStatus = new ArrayList<>();
        successStatus.add(OrderStatus.WAITING_FOR_DEPOSIT.getCode());
        successStatus.add(OrderStatus.DEPOSIT_DECLINED.getCode());
        successStatus.add(OrderStatus.CANCELED.getCode());
        return successStatus;
    }

    public static List<String> getCancelStatus() {
        List<String> successStatus = new ArrayList<>();
        successStatus.add(OrderStatus.CANCELED.getCode());
        return successStatus;
    }


    /**
     * -- GETTER --
     * 获取状态码
     */
    private final String code;
    /**
     * -- GETTER --
     * 获取中文描述
     */
    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /** 根据 code 获取枚举 */
    public static OrderStatus fromCode(String code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (StrUtil.equals(status.code, code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown OrderStatus code: " + code);
    }
}
