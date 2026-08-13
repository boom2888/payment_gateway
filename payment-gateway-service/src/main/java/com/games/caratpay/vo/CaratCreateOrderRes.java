package com.games.caratpay.vo;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建支付订单响应对象
 * 仅在 retCode 为 SUCCESS 时返回以下字段
 */
@Data
public class CaratCreateOrderRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签名值，详见签名算法
     * 示例: C380BEC2BFD727A4B6845133519F3AD6
     */
    private String sign;

    /**
     * 支付中心生成的订单号
     * 示例: 20160427210604000490
     */
    private String payOrderId;

    /**
     * 支付参数，JSON格式数据
     * 可能内容:
     * 1. 二维码支付:
     * {"codeUrl":"付款链接","codeImgUrl":"付款二维码图片地址","payMethod":"codeImg"}
     * 2. 表单跳转支付:
     * {"payMethod":"formJump", "payUrl":"自动提交的form表单内容"}
     */
    private JsonNode payParams;

    /**
     * 支付状态
     * -1: 支付失败
     *  0: 订单生成
     *  1: 支付中
     *  2: 支付成功
     *  3: 业务处理完成
     * 示例: 1
     */
    private int status;

    /**
     * SUCCESS/FAIL此字段标识是否成功
     */
    private String retCode;

    private String retMsg;
}
