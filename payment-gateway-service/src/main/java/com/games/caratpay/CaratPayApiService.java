package com.games.caratpay;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.api.bean.PayOrderResult;
import com.games.api.bean.RefundOrderResult;
import com.games.api.dto.OrderCallbackDto;
import com.games.api.enums.AmlStatus;
import com.games.api.enums.FeeIncludeOption;
import com.games.api.enums.OrderStatus;
import com.games.api.enums.PaymentProvider;
import com.games.api.factory.PaymentStrategy;
import com.games.api.vo.*;
import com.games.caratpay.utils.CaratSignUtil;
import com.games.common.utils.MessageUtils;
import com.games.caratpay.vo.CaratCreateOrderRes;
import com.games.caratpay.vo.CaratCreateOrderVo;
import com.games.common.exception.BusinessException;
import com.games.common.utils.http.HttpUtils;
import com.games.nuvei.NuveiApiService;
import com.games.payment.domain.Crypto;
import com.games.payment.domain.MerchantFeeModel;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.service.ICurrencyService;
import com.games.payment.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

import static com.games.caratpay.utils.CaratSignUtil.parseQueryParams;
import static com.games.caratpay.utils.CaratSignUtil.verifyCallbackSign;

@Slf4j
@Service
public class CaratPayApiService implements PaymentStrategy {

    @Autowired
    CaratPayConfig caratPayConfig;
    @Autowired
    ICurrencyService currencyService;
    @Resource
    IOrderService orderService;

    @Override
    public String getProviderType() {
        return PaymentProvider.CARAT_PAY.getCode();
    }

    @Override
    public Order makeOrder(Shop shop, MerchantFeeModel feeModel, ApiBaseReq vo, Crypto crypto) {
        // 复用 Nuvei 的通用下单逻辑，同时保留并执行 CaratPay 的下单流程
        QrCodeCreateOrderVo createOrderVo = (QrCodeCreateOrderVo) vo;
        // 当前时间 + rollingReserveReleaseDays 天，格式为数字型 YYYYMMDD
        DateTime rollingDateTime = DateUtil.offsetDay(DateUtil.date(), feeModel.getRollingReserveReleaseDays());
        String rollingDate = DateUtil.format(rollingDateTime, "yyyyMMdd");
        // 创建订单对象
        Order order = BeanUtil.copyProperties(vo, Order.class, "customerId");
        order.setFiatAmount(createOrderVo.getOrderAmount());
        order.setProcessingFee(BigDecimal.ZERO);
        order.setGatewayFee(BigDecimal.ZERO);
        order.setRollingFee(BigDecimal.ZERO);
        order.setRollingDate(Integer.parseInt(rollingDate));
        order.setLiquidityQuote(BigDecimal.ONE);
        order.setLiquidityProvider("no");
        order.setAmlStatus(AmlStatus.APPROVED.ordinal());
        order.setAmlScore(1);
        order.setIncludeFee(FeeIncludeOption.INCLUDE.getCode());
//        return createCaratPayOrder(order, createOrderVo);
        return order;
    }


    @Override
    public PayOrderResult payOrder(Shop shop, Order order, PayOrderVo vo) {
        throw new BusinessException("The payment function is temporarily unavailable.");
    }

    @Override
    public OrderCallbackDto handleCallback(String rawBody, Map<String, String> headers) {
        return callback(rawBody, headers);
    }

    @Override
    public RefundOrderResult refundOrder(Shop shop, Order order, RefundOrderVo vo) {
        throw new BusinessException("The refund function is temporarily unavailable.");
    }

    @Override
    public boolean supports(String providerType) {
        return PaymentStrategy.super.supports(providerType);
    }


    public Order createCaratPayOrder(Order baseOrder, QrCodeCreateOrderVo vo) {
        // 创建 CaratPay 订单请求
        CaratCreateOrderVo caratCreateOrderVo = new CaratCreateOrderVo();
        caratCreateOrderVo.setMchId(caratPayConfig.getMerchantId());
        caratCreateOrderVo.setProductId(vo.getProductId()); // 根据业务需求设置
        caratCreateOrderVo.setMchOrderNo(vo.getSsoOrderId());
        caratCreateOrderVo.setCurrency(vo.getCurrency());
        caratCreateOrderVo.setAmount(vo.getOrderAmount().multiply(new BigDecimal(100)).intValue());
        caratCreateOrderVo.setClientIp(vo.getIpAddress());
        caratCreateOrderVo.setSubject(vo.getProductName());
        caratCreateOrderVo.setBody(vo.getProductDesc());
        caratCreateOrderVo.setReturnUrl(caratPayConfig.getReturnUrl());
        caratCreateOrderVo.setNotifyUrl(caratPayConfig.getNotifyUrl());
        caratCreateOrderVo.setExtra(vo.getExtra());
        caratCreateOrderVo.setDevice(vo.getDevice());
        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> rawMap = objectMapper.convertValue(caratCreateOrderVo, Map.class);
        caratCreateOrderVo.setSign(CaratSignUtil.getSignResult(rawMap, caratPayConfig));
        rawMap.put("sign", caratCreateOrderVo.getSign());

        String result = HttpUtils.sendGetWithOkHttp(caratPayConfig.getCreateOrderUrl(), rawMap);
        
        CaratCreateOrderRes res = null;
        try {
            res = objectMapper.readValue(result, CaratCreateOrderRes.class);
        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
            log.error("解析 CaratPay 创建订单响应失败", e);
        }
        if(res == null) {
            baseOrder.setStatus(OrderStatus.DEPOSIT_DECLINED.getCode(), "结果为空，支付失败");
            baseOrder.setFailReasons(MessageUtils.message("request.failed"));
        }else if (!"SUCCESS".equalsIgnoreCase(res.getRetCode())) {
            baseOrder.setStatus(OrderStatus.DEPOSIT_DECLINED.getCode(), "结果状态不对，支付失败");
            baseOrder.setFailReasons(res.getRetMsg());
        } else {
            updateOrderStatusFromCaratPayResponse(baseOrder, res);
        }
        return baseOrder;
    }
    
    private void updateOrderStatusFromCaratPayResponse(Order order, CaratCreateOrderRes res) {
        int status = res.getStatus();
        OrderStatus ourStatus;
        switch (status) {
            case -1:
                ourStatus = OrderStatus.DEPOSIT_DECLINED;
                break;
            case 0:
            case 1:
                ourStatus = OrderStatus.WAITING_FOR_DEPOSIT;
                break;
            case 2:
            case 3:
                ourStatus = OrderStatus.COMPLETED;
                break;
            default:
                throw new BusinessException("Unknown order status: " + status);
        }
        order.setStatus(ourStatus.getCode(), "根据updateOrderStatusFromCaratPayResponse订单结果修改");
        order.setOrderUuid(res.getPayOrderId());
        // 提取 codeUrl（或回退到 payUrl）作为 payParams，使用 Jackson 解析
        ObjectMapper mapper = new ObjectMapper();
        String codeUrl = null;
        try {
            JsonNode node = mapper.valueToTree(res.getPayParams());
            if (node != null && !node.isNull()) {
                if (node.hasNonNull("codeUrl")) {
                    codeUrl = node.get("codeUrl").asText();
                } else if (node.hasNonNull("payUrl")) {
                    // 部分支付方式为表单跳转，仅提供 payUrl
                    codeUrl = node.get("payUrl").asText();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        order.setPayParams(codeUrl);
    }

    public OrderCallbackDto callback(String rawBody, Map<String, String> headers) {
        try {
            log.info("处理 CaratPay 回调: {}", rawBody);
            
            // 解析 URL 编码的查询参数
            Map<String, String> params = parseQueryParams(rawBody);
            
            // 提取必要参数
            String mchOrderNo = params.get("mchOrderNo");
            String statusStr = params.get("status");
            String sign = params.get("sign");
            
            if (StrUtil.isBlank(mchOrderNo)) {
                return new OrderCallbackDto(null, 400, true, "mchOrderNo is required");
            }
            
            if (StrUtil.isBlank(statusStr)) {
                return new OrderCallbackDto(null, 400, true, "status is required");
            }
            
            int status = Integer.parseInt(statusStr);
            
            log.info("处理 CaratPay 订单: {}, 状态: {}", mchOrderNo, status);

            if (StrUtil.isNotBlank(sign)) {
                boolean signValid = verifyCallbackSign(params, sign, caratPayConfig);
                if (!signValid) {
                    log.warn("CaratPay回调验签失败");
                    return new OrderCallbackDto(null, 400, true, "Invalid signature");
                }
            }
            
            // 查询订单
            Order order = orderService.getOrderBySsoOrderId(mchOrderNo);
            if (order == null) {
                return new OrderCallbackDto(null, 500, true, "Order not found: " + mchOrderNo);
            }
            
            // 根据状态处理订单
            OrderStatus newOrderStatus;
            String message;
            
            if (status == 2) { // 2-支付成功
                log.info("CaratPay回调: 支付成功");
                newOrderStatus = OrderStatus.COMPLETED;
                message = "Payment successful";
            } else if (status == -1) { // -1-支付失败
                log.info("CaratPay回调: 支付失败");
                newOrderStatus = OrderStatus.DEPOSIT_DECLINED;
                message = "Payment failed";
            } else if (status == 0 || status == 1) { // 0,1-待支付
                log.info("CaratPay回调: 待支付状态 {}", status);
                newOrderStatus = OrderStatus.WAITING_FOR_DEPOSIT;
                message = "Payment pending";
            } else {
                log.warn("CaratPay回调: 未知状态 {}", status);
                newOrderStatus = OrderStatus.DEPOSIT_DECLINED;
                message = "Unknown status: " + status;
            }
            
            // 更新订单状态
            orderService.updateOrderState(order.getId(), null, newOrderStatus);
            
            // 记录回调信息
            log.info("CaratPay回调处理完成 - 订单: {}, 状态: {}, 消息: {}", mchOrderNo, newOrderStatus.getCode(), message);
            
            return new OrderCallbackDto(order.getId(), 200,false, message);
            
        } catch (NumberFormatException e) {
            log.error("CaratPay回调状态解析错误", e);
            return new OrderCallbackDto(null, 400, true, "Invalid status format");
        } catch (Exception e) {
            log.error("处理 CaratPay 回调异常", e);
            return new OrderCallbackDto(null, 500, true, "Error processing callback: " + e.getMessage());
        }
    }
}
