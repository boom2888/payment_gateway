package com.games.wynpay;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.games.api.bean.PayOrderResult;
import com.games.api.dto.CreateOrderDto;
import com.games.api.dto.PayOrderRs;
import com.games.api.enums.ApiStatus;
import com.games.api.enums.ApiType;
import com.games.api.enums.FeeIncludeOption;
import com.games.api.enums.OrderStatus;
import com.games.api.vo.CreateOrderVo;
import com.games.api.vo.PayOrderVo;
import com.games.payment.domain.Crypto;
import com.games.payment.domain.MerchantFeeModel;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.service.IApiLogService;
import com.games.payment.service.IOrderService;
import com.safecharge.exception.SafechargeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * WynPay通道接口服务类
 */
@Service
public class WynPayApiService {
    @Autowired
    private WynPayApiUtils apiUtils;
    @Autowired
    private WynPayConfig config;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IApiLogService apiLogService;

    public Order makeOrder(Shop shop, MerchantFeeModel feeModel, CreateOrderVo vo, Crypto crypto){
        CreateOrderDto dto = apiUtils.makeOrder(vo, config);
        FeeIncludeOption includeFee = FeeIncludeOption.valueOf(vo.getInclude());
        // 当前时间 + rollingReserveReleaseDays 天，格式为数字型 YYYYMMDD
        DateTime rollingDateTime = DateUtil.offsetDay(DateUtil.date(), feeModel.getRollingReserveReleaseDays());
        String rollingDate = DateUtil.format(rollingDateTime, "yyyyMMdd");
        // 创建订单对象
        Order order = BeanUtil.copyProperties(vo, Order.class, "customerId");
        BigDecimal fiatAmount = dto.getFiatAmount();
        BigDecimal cryptoAmount = dto.getCryptoAmount();

        order.setFiatAmount(fiatAmount);
        order.setCryptoAmount(cryptoAmount);
        order.setProcessingFee(BigDecimal.ZERO);
        order.setGatewayFee(BigDecimal.ZERO);
        order.setRollingFee(BigDecimal.ZERO);
        order.setRollingDate(Integer.parseInt(rollingDate));
//        if(quote != null) {
//            order.setLiquidityQuote(quote.getLiquidityQuote());
//            order.setLiquidityProvider(quote.getLiquidityProvider());
//        }
//        order.setAmlStatus(amlStatus.ordinal());
//        order.setAmlScore(amlScore);
//        String orderStatus = amlStatus == AmlStatus.APPROVED ? OrderStatus.WAITING_FOR_DEPOSIT.getCode() : OrderStatus.AML_HOLD.getCode();
        order.setIncludeFee(includeFee.getCode());
        order.setStatus(OrderStatus.WAITING_FOR_DEPOSIT.getCode(), "Wyn默认新订单");
        return order;
    }

    public PayOrderResult pay(Shop shop, Order order, PayOrderVo vo) throws SafechargeException {
        PayOrderRs apiRs = apiUtils.payOrder(vo, config);
        String superOrderId = apiRs.getOrder().getOrderNo();
        Order dbOrder = orderService.getById(order.getId());
        String newState = OrderStatus.valueOf(apiRs.getOrder().getStatus()).getCode();
        dbOrder.setUpdateAt(new Date());
        dbOrder.setStatus(newState, "根据Wyn支付结果修改订单状态");
        dbOrder.setFailReasons(apiRs.getOrder().getFailReasons());
        dbOrder.setOrderUuid(superOrderId);
        dbOrder.setPayTime(new Date());
        orderService.updateById(dbOrder);
        String cardNo = vo.getCardInfo().getCardNumber();
        String ip = vo.getDeviceDetail().getIpAddress();

        apiLogService.saveLog(ApiType.PAYMENT, ApiStatus.SUCC, order.getId(), order.getSsoOrderId(), cardNo, JSONUtil.toJsonStr(vo), JSONUtil.toJsonStr(apiRs), shop.getId(), ip);

        PayOrderResult result = new PayOrderResult(true, apiRs.getAuth3ds());
        result.setSuperOrderId(superOrderId);
        return result;
    }


}
