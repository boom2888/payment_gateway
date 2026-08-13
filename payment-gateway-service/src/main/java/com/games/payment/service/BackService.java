package com.games.payment.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.games.api.PaymentInService;
import com.games.api.dto.OrderInfoDto;
import com.games.api.dto.OrderInfoSignDto;
import com.games.api.enums.OrderStatus;
import com.games.api.enums.OrderType;
import com.games.common.enums.TradeCallBackStatus;
import com.games.common.utils.SignUtil;
import com.games.payment.PushService;
import com.games.payment.domain.ApiToNftOrderVo;
import com.games.payment.domain.CustomerWallet;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class BackService {

    @Autowired
    private IShopService shopService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private PaymentInService inService;
    @Resource
    private ICustomerWalletService walletService;
    @Autowired
    PushService pushService;

    /**
     * 订单回调
     * 
     * @param orderNo 订单号
     */
    public void oneOrderCallBack(Long orderNo) {
        try {
            Order order = orderService.getById(orderNo);
            if (order == null) {
                log.warn("订单不存在, orderNo: {}", orderNo);
                return;
            }
            Shop shop = shopService.getById(order.getMerchantId());
            if (shop == null) {
                log.warn("商户不存在, merchantId: {}", order.getMerchantId());
                return;
            }
            self().orderCallBack(shop, order, shop.getNotifyOrderStatusEndpoint());
            pushService.pushShopOrder(order.getId());
            // 第二个回调（基于概率）
            Integer endpoint2Percent = shop.getEndpoint2Percent();
            if (endpoint2Percent != null && endpoint2Percent > 0) {
                int randomPercent = (int) (Math.random() * 100);
                log.info("订单第二个回调概率检查, orderNo: {}, 设定概率: {}%, 随机数: {}",
                        orderNo, endpoint2Percent, randomPercent);

                if (randomPercent <= endpoint2Percent) {
                    log.info("触发第二个回调, orderNo: {}, endpoint2: {}",
                            orderNo, shop.getNotifyOrderStatusEndpoint2());
                    apiToNftOrderCallBack(shop, order, shop.getNotifyOrderStatusEndpoint2());
                    log.info("第二个回调执行完成, orderNo: {}", orderNo);
                } else {
                    log.info("跳过第二个回调, orderNo: {}, 随机数 {} >= 设定概率 {}",
                            orderNo, randomPercent, endpoint2Percent);
                }
            } else {
                log.info("第二个回调未配置或概率为0, orderNo: {}, endpoint2Percent: {}",
                        orderNo, endpoint2Percent);
            }
            if (OrderType.NFT_ON_RAMP.getCode().equals(order.getType()) && OrderStatus.COMPLETED.getCode().equals(order.getStatus())) {
                String nftUrl = shop.getNotifyOrderStatusEndpointNft();
                log.info("NFT订单回调处理, orderNo: {}, merchantId: {}, nftUrl: {}", order.getId(), shop.getId(), nftUrl);
                if (StrUtil.isNotBlank(nftUrl)) {
                    notifyNftOrderStatusCallBack(shop, order, nftUrl);
                } else {
                    log.warn("NFT订单回调地址为空, orderNo: {}, merchantId: {}", order.getId(), shop.getId());
                }
            }
        } catch (Exception e) {
            log.error("订单回调异步任务执行失败, orderNo: {}", orderNo, e);
        }
    }

    /**
     * 3DS认证结果回调
     * 
     * @param orderNo
     * @param isFail
     */
    public void tdsOrderCallBack(Long orderNo, boolean isFail) {
        Order order = orderService.getById(orderNo);
        Shop shop = shopService.getById(order.getMerchantId());
        String backShopUrl = (isFail ? shop.getThreeDFailureUrl() : shop.getThreeDSuccessUrl());
        self().orderCallBack(shop, order, backShopUrl);
        pushService.pushShopOrder(order.getId());
    }

    public Map<String, Object> getOrderSignParams(Shop shop, Order order) {
        OrderInfoDto orderInfoDto = inService.orderInfo(order);
        OrderInfoSignDto orderInfoSignDto = BeanUtil.copyProperties(orderInfoDto, OrderInfoSignDto.class);
        orderInfoSignDto.setSsoOrderId(order.getSsoOrderId());
        String sign = SignUtil.sign(shop.getMerchantHashSecretKey(), orderInfoSignDto.getSignParams());
        Map<String, Object> logicParam = SignUtil.getParams(orderInfoSignDto);
        logicParam.put("sign", sign);
        if (StrUtil.equals(OrderType.CARA_ON_RAMP.getCode(), order.getType())) {
            logicParam.remove("cryptoAmount");
            logicParam.remove("crypto");
            logicParam.remove("walletAddress");
            logicParam.remove("type");
        }
        return logicParam;
    }

    private BackService self() {
        return (BackService) AopContext.currentProxy();
    }

    @Transactional(rollbackFor = Exception.class)
    public void orderCallBack(Shop shop, Order oldOrder, String url) {
        if (StrUtil.isBlank(url)) {
            log.warn("回调URL为空，跳过回调处理, 订单号: {}", oldOrder.getId());
            return;
        }

        Order dbOrder = orderService.getById(oldOrder.getId());
        log.info("API回调订单, 单号:{}, 状态:{},订单信息:{}", dbOrder.getId(), dbOrder.getStatus(), JSON.toJSONString(dbOrder));

        Order updateOrder = new Order();
        updateOrder.setId(oldOrder.getId());

        Map<String, Object> logicParam = getOrderSignParams(shop, dbOrder);
        boolean isBackSucc = false;
        try {
            HttpResponse response = HttpRequest.post(url).body(JSON.toJSONString(logicParam))
                    .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                    .charset(Charset.defaultCharset())
                    .execute();
            String body = response.body();
            log.info("API回调订单,单号:{},{},回调地址:{}", dbOrder.getId(), dbOrder.getSsoOrderId(),
                    shop.getNotifyOrderStatusEndpoint());
            log.info("API回调订单,参数:{}", JSON.toJSONString(logicParam));
            log.info("API回调订单,响应:{}", body);

            isBackSucc = StrUtil.isNotBlank(body) && response.isOk();
            boolean isJson = JSONUtil.isTypeJSONObject(body);
            if (isBackSucc && isJson) {
                JSONObject resObject = JSONUtil.parseObj(body);
                String data = resObject.getStr("data");
                String code = resObject.getStr("code");
                boolean isSucc = StrUtil.equals(data, "success") || StrUtil.equals(code, "200");
                if (isSucc) {
                    // 更新回调状态
                    updateOrder.setCallbackStatus(TradeCallBackStatus.OK.getCode());
                    updateOrder.setCallbackNum(dbOrder.getCallbackNum() + 1);
                } else {
                    // 存储失败状态
                    updateOrder.setCallbackNum(dbOrder.getCallbackNum() + 1);
                    updateOrder.setCallbackStatus(TradeCallBackStatus.FAIL.getCode());
                    updateOrder.setCallbackMsg(resObject.toString());
                }
            } else {
                // 存储失败状态
                updateOrder.setCallbackNum(dbOrder.getCallbackNum() + 1);
                updateOrder.setCallbackStatus(TradeCallBackStatus.FAIL.getCode());
                updateOrder.setCallbackMsg(body);
            }
        } catch (Exception e) {
            log.error("订单回调失败: {}", dbOrder.getId(), e);
            // 存储失败状态
            updateOrder.setCallbackNum(dbOrder.getCallbackNum() + 1);
            updateOrder.setCallbackStatus(TradeCallBackStatus.FAIL.getCode());
            updateOrder.setCallbackMsg(e.getMessage());
        }
        updateOrder.setCallbackTime(new Date());
        updateOrder.setUpdateAt(new Date());

        log.info("自动执行记录,商户:{},是否成功:{},订单:{}", shop.getBusinessName(), isBackSucc, dbOrder.getId());
        boolean updated = orderService.updateById(updateOrder);
        log.info("更新回调状态结果:{}, 更新数据:{}", updated, JSON.toJSONString(updateOrder));
    }

    public void apiToNftOrderCallBack(Shop shop, Order order, String url) {
        if (StrUtil.isBlank(url)) {
            log.warn("NFT回调URL为空，跳过回调处理, 订单号: {}", order.getId());
            return;
        }
        ApiToNftOrderVo vo = new ApiToNftOrderVo();
        vo.setMerchantId(String.valueOf(shop.getId()));
        vo.setAmount(order.getFiatAmount());
        CustomerWallet customerWalletByCrypto = walletService.getCustomerWalletByCrypto(order.getCustomerId(), null);
        vo.setAddress(customerWalletByCrypto.getWalletAddress());
        vo.setSsoOrderId(order.getSsoOrderId());
        try (HttpResponse response = HttpRequest.post(url).body(JSON.toJSONString(vo))
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .charset(Charset.defaultCharset())
                .execute()) {
            String body = response.body();
            log.info("API回调NFT订单,单号:{},{},回调地址:{}", order.getId(), order.getSsoOrderId(), url);
            log.info("API回调NFT订单,参数:{}", JSON.toJSONString(vo));
            log.info("API回调NFT订单,响应:{}", body);
        }
    }

    public void notifyNftOrderStatusCallBack(Shop shop, Order order, String url) {
        Map<String, Object> logicParam = getOrderSignParams(shop, order);
        try (HttpResponse response = HttpRequest.post(url).body(JSON.toJSONString(logicParam))
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .charset(Charset.defaultCharset())
                .execute()) {
            String body = response.body();
            log.info("回调NFT订单,单号:{},{},回调地址:{}", order.getId(), order.getSsoOrderId(), url);
            log.info("回调NFT订单,参数:{}", JSON.toJSONString(logicParam));
            log.info("回调NFT订单,响应:{}", body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
