package com.games.caratpay.vo;

import lombok.Data;

@Data
public class CaratCallBackOrderVo {

    /**
     * 支付订单号
     * 支付中心生成的订单号
     */
    private String payOrderId;

    /**
     * 商户ID
     * 支付中心分配的商户号
     */
    private String mchId;

    /**
     * 应用ID
     * 该商户创建的应用对应的ID
     */
    private String appId;

    /**
     * 支付产品ID
     */
    private int productId;

    /**
     * 商户订单号
     * 商户生成的订单号
     */
    private String mchOrderNo;

    /**
     * 支付金额
     * 单位分
     */
    private int amount;

    /**
     * 状态
     * 支付状态,-1-支付失败,0-订单生成,1-支付中,2-支付成功,3-业务处理完成
     */
    private int status;

    /**
     * 渠道订单号
     * 三方支付渠道订单号
     */
    private String channelOrderNo;

    /**
     * 渠道数据包
     * 支付渠道数据包
     */
    private String channelAttach;

    /**
     * 扩展参数1
     * 支付中心回调时会原样返回
     */
    private String param1;

    /**
     * 扩展参数2
     * 支付中心回调时会原样返回
     */
    private String param2;

    /**
     * 支付成功时间
     * 精确到毫秒
     */
    private long paySuccTime;

    /**
     * 通知类型
     * 1-前台通知，2-后台通知
     */
    private int backType;

    /**
     * 签名
     * 签名值，详见签名算法
     */
    private String sign;
}
