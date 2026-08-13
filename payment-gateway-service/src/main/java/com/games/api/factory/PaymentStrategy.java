package com.games.api.factory;

import com.games.api.bean.PayOrderResult;
import com.games.api.bean.RefundOrderResult;
import com.games.api.dto.OrderCallbackDto;
import com.games.api.vo.ApiBaseReq;
import com.games.api.vo.PayOrderVo;
import com.games.api.vo.RefundOrderVo;
import com.games.payment.domain.Crypto;
import com.games.payment.domain.MerchantFeeModel;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;

import java.util.Map;

/**
 * 支付策略接口
 * 定义所有支付供应商需要实现的方法
 */
public interface PaymentStrategy {
    
    /**
     * 获取支付供应商类型
     * @return 支付供应商类型
     */
    String getProviderType();


    Order makeOrder(Shop shop, MerchantFeeModel feeModel, ApiBaseReq vo, Crypto crypto);


    /**
     * 支付订单
     * @param shop 商户信息
     * @param order 订单信息
     * @param vo 支付订单请求
     * @return 支付结果
     */
    PayOrderResult payOrder(Shop shop, Order order, PayOrderVo vo);


    OrderCallbackDto handleCallback(String rawBody, Map<String, String> headers);
    
    /**
     * 退款订单
     * @param shop 商户信息
     * @param order 订单信息
     * @param vo 退款订单请求
     * @return 退款结果
     */
    RefundOrderResult refundOrder(Shop shop, Order order, RefundOrderVo vo);
    
    /**
     * 检查支付供应商是否支持
     * @param providerType 支付供应商类型
     * @return 是否支持
     */
    default boolean supports(String providerType) {
        return getProviderType().equals(providerType);
    }
}