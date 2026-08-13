package com.games.caratpay.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaratCreateOrderVo {

    // 必填字段
    /**
     * 商户ID，分配的商户号
     * 示例: 20001222
     */
    private long mchId;
    
    /**
     * 支付产品ID，详见5、支付产品
     * 示例: 8000
     */
    private int productId;
    
    /**
     * 商户生成的订单号
     * 示例: 20160427210604000490
     */
    private String mchOrderNo;
    
    /**
     * 三位货币代码
     * 示例: cny
     */
    private String currency;
    
    /**
     * 支付金额，单位分
     * 示例: 100
     */
    private int amount;
    
    /**
     * 客户端IP地址
     * 示例: 210.73.10.148
     */
    private String clientIp;
    
    /**
     * 商品主题
     * 示例: 测试商品1
     */
    private String subject;
    
    /**
     * 商品描述信息
     * 示例: 测试商品描述
     */
    private String body;
    
    /**
     * 签名值，详见签名算法
     * 示例: C380BEC2BFD727A4B6845133519F3AD6
     */
    private String sign;

    // 可选字段
    /**
     * 该商户创建的应用对应的ID
     * 示例: 0ae8be35ff634e2abe94f5f32f6d5c4f
     */
    private String appId;
    
    /**
     * 客户端设备
     * 示例: pc或mobile
     */
    private String device;
    
    /**
     * 支付结果同步回调URL
     * 示例: http://shop.xxx.org/return.htm
     */
    private String returnUrl;
    
    /**
     * 支付结果异步回调URL
     * 示例: http://shop.xxx.org/notify.htm
     */
    private String notifyUrl;
    
    /**
     * 支付中心回调时会原样返回
     */
    private String param1;
    
    /**
     * 支付中心回调时会原样返回
     */
    private String param2;
    
    /**
     * 特定渠道发起时额外参数
     * 示例: {"openId":"o2RvowBf7sOVJf8kJksUEMceaDqo"} visa通道：{"cardNo":"4367000000000000","expDate":"0629","cvv":"123"}
     */
    private String extra;

}