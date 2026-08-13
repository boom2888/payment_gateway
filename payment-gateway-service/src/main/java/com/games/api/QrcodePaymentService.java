package com.games.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.games.api.enums.OrderStatus;
import com.games.api.enums.OrderType;
import com.games.api.factory.PaymentStrategyResolver;
import com.games.api.vo.QrCodeCreateOrderVo;
import com.games.caratpay.CaratPayApiService;
import com.games.common.exception.BusinessException;
import com.games.nuvei.RateService;
import com.games.payment.domain.*;
import com.games.payment.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
@Slf4j
public class QrcodePaymentService {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IMerchantFeeModelService feeModelService;
    @Autowired
    RateService rateService;
    @Autowired
    ICurrencyService currencyService;
    @Autowired
    ICryptoService cryptoService;
    @Autowired
    IUserService userService;
    @Autowired
    ICustomerService customerService;
    @Autowired
    private CaratPayApiService caratPayApiService;
    @Resource
    private PaymentStrategyResolver paymentStrategyResolver;

    @Transactional
    public Order createOrder(Shop shop, QrCodeCreateOrderVo vo) {
        log.info("创建订单参数{}", JSON.toJSONString(vo));
        OrderType orderType = OrderType.CARA_ON_RAMP;
        Order existingOrder = orderService.getOrderBySsoOrderId(vo.getSsoOrderId());
        if (existingOrder != null && !StrUtil.equals(existingOrder.getStatus(), OrderStatus.WAITING_FOR_DEPOSIT.getCode())) {
            throw new BusinessException(StrUtil.format(
                    "This ssoOrderId already exists. Please contact the Merchant Customer Support to place a new Order: sso_order_id:{}",
                    vo.getSsoOrderId()
            ));
        }
        if (existingOrder == null) {
            MerchantFeeModel feeModel = feeModelService.getById(shop.getMerchantFeeModelId());
            if (feeModel == null) {
                throw new BusinessException("Merchant fee model does not exist");
            }

            BigDecimal orderAmount = vo.getOrderAmount();
            if (orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Order amount must be greater than 0");
            }

            Currency settlementCurrency = currencyService.getById(shop.getSettlementCurrency());
            if (settlementCurrency == null) {
                throw new BusinessException("memerchat settlement currency null error");
            }
            String baseCurrency = settlementCurrency.getCode();
            BigDecimal rateInfo = rateService.getRate(vo.getCurrency(), baseCurrency);
            if (rateInfo == null) {
                throw new BusinessException("Rate info not found");
            }

            BigDecimal maxAmount = rateInfo.multiply(orderAmount);
            if (maxAmount.compareTo(shop.getOnRampMax()) > 0) {
                throw new BusinessException("Order amount exceeds max limit");
            }

            Currency currency = currencyService.getByCode(vo.getCurrency());
            if (currency == null) {
                throw new BusinessException("Currency not found");
            }

            // 以美元为基准计算金额
            if (!"USD".equals(baseCurrency)) {
                rateInfo = rateService.getRate(vo.getCurrency(), "USD");
            }
            BigDecimal transferAmount = rateInfo.multiply(orderAmount).setScale(4, RoundingMode.HALF_UP);

            Customer customer = customerService.getBySsoCustomerId(shop.getId(), vo.getCustomerId());
            if (customer == null) {
                User user = User.init(null);
                user.setCountryCode(null);
                userService.save(user);

                customer = Customer.init(shop.getId(), vo.getCustomerId());
                customer.setUserId(user.getId());
                customer.setSaasUserCorporationId(1L);
                customer.setSubscriptionModelId(1L);
                customer.setRiskLevel(1L);
                customerService.save(customer);
            }
            //保存订单信息
//            PaymentStrategy paymentStrategy = paymentStrategyResolver.resolve(shop);
//            Order order = paymentStrategy.makeOrder(shop, feeModel, vo, null);
            Order order = caratPayApiService.makeOrder(shop, feeModel, vo, null);
            caratPayApiService.createCaratPayOrder(order, vo);
            order.setMerchantId(shop.getId());
            order.setCustomerId(customer.getId());
            order.setCurrencyId(currency.getId());
            order.setCreatedAt(new Date());
            order.setType(orderType.getCode());
            order.setIsOtc(0);
            order.setTransferAmount(transferAmount);
            orderService.save(order);
            log.info("创建订单返回结果：{}", JSONUtil.toJsonStr(order));
            return order;
        }else{
            caratPayApiService.createCaratPayOrder(existingOrder, vo);
            orderService.updateById(existingOrder);
        }
        return existingOrder;
    }
}
