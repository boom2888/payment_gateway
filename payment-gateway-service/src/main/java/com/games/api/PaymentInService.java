package com.games.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.games.api.bean.PayOrderResult;
import com.games.api.bean.RefundOrderResult;
import com.games.api.dto.*;
import com.games.api.enums.*;
import com.games.api.vo.*;
import com.games.common.core.domain.R;
import com.games.common.exception.BusinessException;
import com.games.common.utils.DateUtils;
import com.games.common.utils.SignUtil;
import com.games.nuvei.*;
import com.games.pay.vo.TotalFiatAmountVo;
import com.games.payment.CardService;
import com.games.payment.PushService;
import com.games.payment.domain.*;
import com.games.payment.service.*;
import com.games.shift4.Shift4ApiService;
import com.games.system.service.ISysConfigService;
import com.games.wynpay.WynPayApiService;
import com.safecharge.exception.SafechargeException;
import com.safecharge.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PaymentInService {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IMovementDataService movementDataService;
    @Autowired
    private ICustomerAddressService addressService;
    @Autowired
    private ICustomerWalletService walletService;
    @Autowired
    private NuveiApiService nuveiApiService;
    @Autowired
    private WynPayApiService wynPayApiService;
    @Autowired
    private Shift4ApiService shift4ApiService;
    @Autowired
    private IMerchantFeeModelService feeModelService;
    @Autowired
    IShopService shopService;
    @Autowired
    ISysConfigService configService;
    @Autowired
    CacheService cacheService;
    @Autowired
    RateService rateService;
    @Autowired
    QuoteService quoteService;
    @Autowired
    AmlService amlService;
    @Autowired
    ICurrencyService currencyService;
    @Autowired
    ICryptoService cryptoService;
    @Autowired
    IUserService userService;
    @Autowired
    ICustomerService customerService;
    @Autowired
    IAcquirerOrderService acquirerOrderService;
    @Autowired
    CardService cardService;
    @Autowired
    SecurityInterceptionService interceptionService;
    @Autowired
    PaymentCachingService cachingService;
    @Autowired
    IRefundOrderService refundOrderService;
    @Autowired
    IIpInfoService ipInfoService;
    @Autowired
    IShopLimitService shopLimitService;
    @Autowired
    PushService pushService;

    @Transactional
    public Order createOrder(Shop shop, CreateOrderVo vo) {
        // String ip = IpUtils.getIp();
        // System.out.println("客户端IP==>" + ip);
        log.info("创建订单参数{}", JSON.toJSONString(vo));
        OrderType orderType = OrderType.valueOf(vo.getOrderType());
        Order existingOrder = orderService.getOrderBySsoOrderId(vo.getSsoOrderId());
        if (existingOrder != null
                && !StrUtil.equals(existingOrder.getStatus(), OrderStatus.WAITING_FOR_DEPOSIT.getCode())) {
            throw new BusinessException(StrUtil.format(
                    "This ssoOrderId already exists. Please contact the Merchant Customer Support to place a new Order: sso_order_id:{}",
                    vo.getSsoOrderId()));
        }
        if (existingOrder != null) {
            throw new BusinessException("Order already exists");
        }

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
        // String baseCurrency = configService.selectConfigByKey(BASE_CURRENCY);

        Currency currency = currencyService.getByCode(vo.getCurrency());
        if (currency == null) {
            throw new BusinessException("Currency not found");
        }

        Crypto crypto = cryptoService.getBySymbol(vo.getCryptoCurrency());
        if (crypto == null) {
            throw new BusinessException("Crypto not found");
        }

        // 以美元为基准计算金额
        BigDecimal rateInfo = rateService.getRate(vo.getCurrency(), baseCurrency);
        BigDecimal transferAmount = null;
        if (rateInfo != null) {
            BigDecimal maxAmount = rateInfo.multiply(orderAmount);
            if (maxAmount.compareTo(shop.getOnRampMax()) > 0) {
                throw new BusinessException("Order amount exceeds max limit");
            }
            if (!"USD".equals(baseCurrency)) {
                rateInfo = rateService.getRate(vo.getCurrency(), "USD");
            }
            transferAmount = rateInfo.multiply(orderAmount).setScale(4, RoundingMode.HALF_UP);
        }

        // 累加支付成功金额
        if (configService.isShopLimitOpen()) {
            ShopLimit shopLimit = shopLimitService.getByShopId(shop.getId());
            if (shopLimit != null) {
                shopLimit.checkAmount();
            }
        }
        // 保存客户信息
        Customer customer = customerService.getBySsoCustomerId(shop.getId(), vo.getCustomerId());
        if (customer == null) {
            User user = User.init(vo.getCustomerEmail());
            user.setCountryCode(vo.getCustomerCountry());
            userService.save(user);

            customer = Customer.init(shop.getId(), vo.getCustomerId());
            customer.setUserId(user.getId());
            customer.setSaasUserCorporationId(1L);
            customer.setSubscriptionModelId(1L);
            customer.setRiskLevel(1L);
            customerService.save(customer);
        }
        CustomerWallet customerWallet = walletService.getCustomerWalletByCrypto(customer.getId(), crypto.getId());
        if (customerWallet == null) {
            customerWallet = CustomerWallet.init(customer.getId(), crypto.getId(), vo.getCustomerWallet());
            walletService.save(customerWallet);
        }

        // 保存订单信息
        boolean isNuvei = FromSource.valueOf(shop.getFromSource()) == FromSource.NUVEI;
        boolean isShift4 = FromSource.valueOf(shop.getFromSource()) == FromSource.SHIFT4;
        boolean isWynPay = FromSource.valueOf(shop.getFromSource()) == FromSource.WYNPAY;
        Order order;
        if (isNuvei) {
            order = nuveiApiService.makeOrder(shop, feeModel, vo, crypto);
        } else if (isShift4) {
            order = shift4ApiService.makeOrder(shop, feeModel, vo, crypto);
        } else if (isWynPay) {
            order = wynPayApiService.makeOrder(shop, feeModel, vo, crypto);
        } else {
            order = nuveiApiService.makeOrder(shop, feeModel, vo, crypto);
//            throw new BusinessException("Unknown Payment Channel");
        }
        order.setMerchantId(shop.getId());
        order.setCustomerId(customer.getId());
        order.setCustomerWalletId(customerWallet.getId());
        order.setCurrencyId(currency.getId());
        order.setCryptoId(crypto.getId());
        order.setCreatedAt(new Date());
        order.setType(orderType.getCode());
        order.setIsOtc(0);
        order.setProductName(vo.getProductName());
        order.setTransferAmount(transferAmount);
        orderService.save(order);
        log.info("创建订单返回结果：{}", JSONUtil.toJsonStr(order));

        // 保存风控需要的数据
        cachingService.updateToCacheAfterCreate(shop, order, vo);
        return order;
    }

    // 白名单 IP 列表
    private static final List<String> BLOCKED_IPS = Arrays.asList(
            "8.212.26.9",
            "73.41.17.247",
            "104.28.32.92",
            "104.28.48.166",
            "104.28.49.10",
            "104.28.123.180",
            "107.115.227.31",
            "126.218.127.214",
            "146.70.31.230");

    public R<PayOrderRs> payOrder(Shop shop, Order dbOrder, PayOrderVo vo) throws SafechargeException {
        if (!OrderStatus.canPayState().contains(OrderStatus.fromCode(dbOrder.getStatus()))) {
            throw new BusinessException("order status can not pay");
        }
        // 二级商户替换功能
        String cardNumber = vo.getCardInfo() != null ? vo.getCardInfo().getCardNumber() : null;
        String maskedCardNumber = SignUtil.maskPan(cardNumber);
        shop = shopService.replaceShopToSecondaryMerchant(shop, cardNumber, dbOrder);
        //
        if (configService.isShopLimitOpen()) {
            ShopLimit shopLimit = shopLimitService.getByShopId(shop.getId());
            if (shopLimit != null) {
                shopLimit.checkAmount();
            }
        }
        String cardFinger = SignUtil.cardFingerprint(cardNumber);
        PayOrderVo safeVo = JSONUtil.toBean(JSONUtil.toJsonStr(vo), PayOrderVo.class);
        if (safeVo == null) {
            safeVo = new PayOrderVo();
            BeanUtil.copyProperties(vo, safeVo);
        }
        if (safeVo.getCardInfo() == null && vo.getCardInfo() != null) {
            safeVo.setCardInfo(BeanUtil.copyProperties(vo.getCardInfo(), CardInfoVo.class));
        }
        if (safeVo.getCardInfo() != null) {
            safeVo.getCardInfo().setCardNumber(maskedCardNumber);
            safeVo.getCardInfo().setCvv(null);
            safeVo.getCardInfo().setCardFinger(cardFinger);
        }
        String safePayInfoJson = JSONUtil.toJsonStr(safeVo);
        log.info("{},支付参数信息:{}", dbOrder.getId(), JSONUtil.toJsonPrettyStr(safeVo));
        // 黑名单卡验证
        if (cardService.isBlackCard(cardFinger, cardNumber)) {
            throw new BusinessException("Card number is black");
        }
        // 白名单卡验证
        if (!cardService.isWhiteCard(cardFinger, cardNumber, shop)) {
            throw new BusinessException(StrUtil.format("Shop ID: {}, Card Number: {} is not in the whitelist",
                    shop.getId(), maskedCardNumber));
        }
        if (StrUtil.equals(PaymentType.GOOGLE_PAY.getValue(), vo.getPaymentType())
                && StrUtil.isNotBlank(vo.getGooglePayToken())) {
            throw new BusinessException("Google Pay Token is missing");
        }
        if (StrUtil.equals(PaymentType.APPLE_PAY.getValue(), vo.getPaymentType())
                && StrUtil.isNotBlank(vo.getApplePayToken())) {
            throw new BusinessException("Apple Pay Token is missing");
        }
        String ip = vo.getDeviceDetail().getIpAddress();

        if (BLOCKED_IPS.contains(ip)) {
            System.out.println(StrUtil.format("命中黑名单IP{}", ip));
        }

        if (configService.ipSwitchOpen()) {
            String newIp = ipInfoService.getBlackIp(ip);
            if (StrUtil.isNotBlank(newIp)) {
                vo.getDeviceDetail().setIpAddress(newIp);
                System.out.println(StrUtil.format("黑名单IP由{}变更为{}", ip, newIp));
                ip = newIp;
            }
        }

        // 支付拦截验证
        interceptionService.payInterception(dbOrder, shop, vo);

        boolean isWyn = FromSource.valueOf(shop.getFromSource()) == FromSource.WYNPAY;
        boolean isNuvei = FromSource.valueOf(shop.getFromSource()) == FromSource.NUVEI;
        boolean isShift4 = FromSource.valueOf(shop.getFromSource()) == FromSource.SHIFT4;
        PayOrderResult payResults;

        if (isWyn) {
            payResults = wynPayApiService.pay(shop, dbOrder, vo);
        } else if (isNuvei) {
            payResults = nuveiApiService.pay(shop, dbOrder, vo);
        } else if (isShift4) {
            payResults = shift4ApiService.pay(shop, dbOrder, vo);
        } else {
            throw new BusinessException(String.format("不支持的支付渠道：%s", FromSource.valueOf(shop.getFromSource())));
        }
        log.info("orderNo: {}，payResult: {}, ", dbOrder.getId(), JSONUtil.toJsonStr(payResults));
        // 更新数据库订单信息
        dbOrder.setUpdateAt(new Date());
        dbOrder.setFailReasons(payResults.getMessage());
        dbOrder.setPayInfo(safePayInfoJson);
        dbOrder.setPaymentIp(ip);
        dbOrder.setPayTime(new Date());
        dbOrder.setStatus(OrderStatus.DEPOSIT_DECLINED.getCode(), "订单状态默认为失败");
        dbOrder.setPaymentType(vo.getPaymentType());
        // 订单支付失败，只要拿到供应商单号，都存起来
        if (payResults.getSuperOrderId() != null && !payResults.getSuperOrderId().isEmpty()) {
            dbOrder.setOrderUuid(payResults.getSuperOrderId());
        }
        if (payResults.getTransactionId() != null && !payResults.getTransactionId().isEmpty()) {
            dbOrder.setTransactionId(payResults.getTransactionId());
        }
        if (payResults.isSucc()) {
            String superOrderId = payResults.getSuperOrderId();
            dbOrder.setOrderUuid(superOrderId);
            dbOrder.setStatus(payResults.getOrderStatus().getCode(), "根据支付结果修改");

            dbOrder.setTransactionId(payResults.getTransactionId());
            dbOrder.setRelatedTransactionId(payResults.getRelatedTransactionId());
            dbOrder.setUserPaymentOptionId(payResults.getUserPaymentOptionId());
            dbOrder.setAcquirerId(payResults.getAcquirerId());
        }

        // 使用通用的条件更新方法：如果当前状态是9，则不更新状态字段，但更新其他字段
        boolean update = orderService.updateOrderExcludeStatus(dbOrder, "9");
        log.info("更新订单状态{},orderInfo:{}", update, JSONUtil.toJsonStr(dbOrder));
        // 推送新的订单状态
        // pushService.pushOrder(dbOrder.getId());

        // 支付结果
        PayOrderDto dto = BeanUtil.copyProperties(dbOrder, PayOrderDto.class);
        dto.setStatus(OrderStatus.fromCode(dbOrder.getStatus()).name());
        dto.setOrderNo(dbOrder.getId() + "");
        dto.setType(OrderType.fromCode(dbOrder.getType()).name());
        Crypto crypto = cryptoService.getById(dbOrder.getCryptoId());
        Currency currency = currencyService.getById(dbOrder.getCurrencyId());
        dto.setCrypto(crypto.getShortName());
        dto.setCurrency(currency.getCode());
        // 处理中的订单不返回失败原因
        if (OrderStatus.APPROVED.getCode().equals(dbOrder.getStatus())) {
            dto.setFailReasons(null);
        }

        // 组装返回结果
        PayOrderRs payOrderRs = new PayOrderRs(payResults.isTds(), payResults.getTdsDto(), dto);
        log.info("支付返回结果{}", JSONUtil.toJsonStr(payOrderRs));
        // 支付成功数据 更新到redis
        cachingService.updateToCacheAfterPayAsync(vo, payResults);

        if (!payResults.isSucc()) {
            return R.fail(payResults.getMessage());
        }
        return R.ok(payOrderRs);
    }

    public OrderInfoDto orderInfo(Order dbOrder) {
        if (dbOrder == null) {
            throw new BusinessException("order not exists");
        }

        // Shift4 订单：每次查询都调用 101 接口核对状态
        Shop shop = shopService.getById(dbOrder.getMerchantId());
        if (shop != null && FromSource.SHIFT4.name().equals(shop.getFromSource())) {
            String currentStatus = dbOrder.getStatus();
            // 调用 Shift4 查询接口确认交易状态
            boolean isTransactionSuccess = shift4ApiService.queryOrderTransactionStatus(dbOrder);

            if (isTransactionSuccess && !OrderStatus.COMPLETED.getCode().equals(currentStatus)) {
                // Shift4 端交易成功，但本地状态不是 COMPLETED，需要更新
                log.warn("Shift4订单状态不一致 - 订单ID: {}, 本地状态: {}, Shift4状态: 成功",
                        dbOrder.getId(), OrderStatus.fromCode(currentStatus).name());
                dbOrder.setStatus(OrderStatus.COMPLETED.getCode(), "Shift4交易查询确认成功-状态修复");
                orderService.updateById(dbOrder);
                log.info("订单状态通过Shift4查询更新为COMPLETED - 订单ID: {}", dbOrder.getId());
            } else if (!isTransactionSuccess && OrderStatus.COMPLETED.getCode().equals(currentStatus)) {
                // 本地是 COMPLETED，但 Shift4 端查询失败，记录警告日志（不自动更新，需要人工确认）
                log.warn("Shift4订单状态异常 - 订单ID: {}, 本地状态: COMPLETED, 但Shift4查询返回失败，请人工核查",
                        dbOrder.getId());
            }
        }

        Crypto crypto = cryptoService.getById(dbOrder.getCryptoId());
        Currency currency = currencyService.getById(dbOrder.getCurrencyId());
        CustomerWallet wallet = walletService.getCustomerWalletByCrypto(dbOrder.getCustomerId(), dbOrder.getCryptoId());
        OrderInfoDto dto = BeanUtil.copyProperties(dbOrder, OrderInfoDto.class);
        dto.setCrypto(crypto.getShortName());
        dto.setCurrency(currency.getCode());
        if (wallet != null) {
            dto.setWalletAddress(wallet.getWalletAddress());
        }
        dto.setStatus(OrderStatus.fromCode(dbOrder.getStatus()).name());
        dto.setType(OrderType.fromCode(dbOrder.getType()).name());
        dto.format();
        dto.setSource(shop.getFromSource());
        dto.setProductName(dbOrder.getProductName());
        dto.setOrderNo(dbOrder.getSsoOrderId());
        return dto;
    }

    @Transactional
    public ThreedsCallbackDto threedsCallback(Order order, Shop shop, boolean isSuc) {
        try {
            if (isSuc) {
                PaymentResponse paymentResponse = nuveiApiService.finalPayOrder(shop, order);
                String message = paymentResponse.getReason();
                if (StrUtil.isBlank(message)) {
                    message = paymentResponse.getIssuerDeclineReason();
                }
                if (StrUtil.isBlank(message)) {
                    message = paymentResponse.getGwErrorReason();
                }
                Boolean error = !(paymentResponse.getErrCode() == 0 && paymentResponse.getGwErrorCode() == 0);
                if (paymentResponse.getErrCode() == 0 && paymentResponse.getGwErrorCode() == 0) {
                    log.info("3DS回掉支付成功");
                    orderService.updateOrderStateAndTransactionID(order.getId(), paymentResponse.getOrderId(),
                            OrderStatus.APPROVED, paymentResponse.getTransactionId());
                } else {
                    orderService.updateOrderState(order.getId(), paymentResponse.getOrderId(),
                            OrderStatus.DEPOSIT_DECLINED);
                }
                return new ThreedsCallbackDto(error, message);
            } else {
                if (StrUtil.equalsAny(order.getStatus(), OrderStatus.APPROVED.getCode(),
                        OrderStatus.FULLFILLED.getCode())) {
                    return new ThreedsCallbackDto(false,
                            "3DS Authentication was not successful but order status is PROCESSING");
                }
                orderService.updateOrderState(order.getId(), null, OrderStatus.DEPOSIT_DECLINED);
                return new ThreedsCallbackDto(true, "3DS Authentication was not successful");
            }
        } catch (Exception e) {
            log.error("创建 3DS 最终支付失败", e);
            return new ThreedsCallbackDto(true, "Error happening in creating final payment");
        }
    }

    @Transactional
    public OrderCallbackDto orderCallback(ThreedsCallbackVo vo) {

        if (StrUtil.isBlank(vo.getTransactionType())) {
            return new OrderCallbackDto(null, 200, false, "Transaction type is missing");
        }
        if (StrUtil.equals(vo.getTransactionType(), "Credit")) {
            return new OrderCallbackDto(null, 200, false, "transactionType is Credit, No processing is required");
        }
        if (StrUtil.isBlank(vo.getClientUniqueId())) {
            return new OrderCallbackDto(null, 500, true, "clientUniqueId is required");
        }

        String orderId = vo.getClientRequestId().replace("MY_ORDER_ID_", "");
        System.out.println("回掉处理订单参数:" + orderId);
        Order order = orderService.getById(orderId);
        if (order == null) {
            return new OrderCallbackDto(null, 500, true, "Acquirer order not found!");
        }
        System.out.println("回掉处理订单:" + order.getId());
        if (StrUtil.equals(vo.getStatus(), "APPROVED")) {
            System.out.println("nuvei-回调: 支付成功,处理成功流程 - Payment approved - processing success flow");

        } else if (StrUtil.equals(vo.getStatus(), "DECLINED")) {
            System.out.println("nuvei-回调: 支付被拒绝,处理失败流程 - Payment declined - processing failure flow");

        } else if (StrUtil.equals(vo.getStatus(), "ERROR")) {
            System.out.println("nuvei-回调: 支付错误,处理错误流程 - Payment error - processing error flow");
        }
        try {
            if (StrUtil.contains(vo.getReason(), "TRANSREASON1=1000000154")) {
                String ip = order.getPaymentIp();
                System.out.println("回掉被拉黑的IP:" + ip);
                ipInfoService.saveBlackIp(ip);
            }
            // 如果我们自己的订单状态是失败，但是nuvie成功了
            if (order.getTransactionId() == null && vo.getTransactionID() != null) {
                Order updateOrder = new Order();
                updateOrder.setId(order.getId());
                updateOrder.setTransactionId(vo.getTransactionID());
                orderService.updateById(updateOrder);
            }
            // 更新主订单状态
            OrderStatus newOrderStatus = StrUtil.equals(vo.getStatus(), "APPROVED") ? OrderStatus.COMPLETED
                    : OrderStatus.DEPOSIT_DECLINED;
            boolean state = orderService.updateOrderState(order.getId(), null, newOrderStatus);
            if (!state) {
                log.error("updateOrderState error: orderId={}", orderId);
            }
        } catch (Exception e) {
            log.error("更新订单状态异常:{}", e.getMessage());
            throw new RuntimeException(e);
        }

        return new OrderCallbackDto(order.getId(), 200, true, "Error happening in creating final payment");
    }

    @Transactional(rollbackFor = Exception.class)
    public RefundOrderDto refundOrder(Order dbOrder, RefundOrderVo vo) {
        OrderStatus orderStatus = OrderStatus.fromCode(dbOrder.getStatus());
        if (!OrderStatus.canRefundState().contains(orderStatus)) {
            throw new BusinessException(StrUtil.format("order status {} can not refund", orderStatus));
        }
        Currency currency = currencyService.getById(dbOrder.getCurrencyId());
        if (currency == null) {
            throw new BusinessException("currency is not supported");
        }
        if (!vo.getCurrency().equals(currency.getCode())) {
            throw new BusinessException("The refund currency does not match the payment currency.");
        }
        RefundOrder processingRefundOrder = new RefundOrder();
        BigDecimal remainingRefundableAmount = null;
        try {
            // 防重复提交：根据退款订单号查询是否已存在
            RefundOrder existingRefundOrder = refundOrderService.selectByRefundSsoId(vo.getRefundSsoId());
            processingRefundOrder.setOrderId(dbOrder.getId());
            processingRefundOrder.setRefundSsoId(vo.getRefundSsoId());
            processingRefundOrder.setMerchantId(Long.valueOf(vo.getMerchantId()));
            processingRefundOrder.setRefundAmount(vo.getRefundAmount());
            processingRefundOrder.setStatus(RefundStatus.PROCESSING.getCode());
            processingRefundOrder.setRefundCurrencyId(dbOrder.getCurrencyId());
            processingRefundOrder.setCreateAt(DateUtils.iso8601ToTimestamp(DateUtils.getNowDateWithISO8601Format()));
            if (existingRefundOrder == null) {
                refundOrderService.save(processingRefundOrder);
            } else if (RefundStatus.canNotRefundStatus
                    .contains(RefundStatus.getName(existingRefundOrder.getStatus()))) {
                throw new BusinessException("The refund order is already being processed.");
            } else {
                processingRefundOrder.setId(existingRefundOrder.getId());
            }

            // 使用数据库原子操作计算剩余可退款金额，确保并发安全
            remainingRefundableAmount = calculateRemainingRefundableAmount(dbOrder);

            if (vo.getRefundAmount().compareTo(remainingRefundableAmount) > 0) {
                throw new BusinessException("The refund amount has exceeded the maximum refundable limit.");
            }

            // 创建处理中的退款记录，防止重复提交

            // 根据支付渠道调用对应的第三方API进行退款
            Shop shop = shopService.getById(dbOrder.getMerchantId());
            RefundOrderResult refund;
            boolean isNuvei = FromSource.NUVEI.name().equals(shop.getFromSource());
            boolean isShift4 = FromSource.SHIFT4.name().equals(shop.getFromSource());
            if (isNuvei) {
                refund = nuveiApiService.refund(dbOrder, vo);
            } else if (isShift4) {
                refund = shift4ApiService.refund(dbOrder, vo);
            } else {
                throw new BusinessException("The Payment Channel is not supported.");
            }
            if (refund.getTransactionId() != null) {
                processingRefundOrder.setTransactionId(refund.getTransactionId());
            }
            // 根据退款结果更新状态
            if (refund.isSuccess()) {
                processingRefundOrder.setStatus(RefundStatus.SUCCESS.getCode());
                processingRefundOrder
                        .setRemainingRefundableAmount(remainingRefundableAmount.subtract(vo.getRefundAmount()));
            } else {
                processingRefundOrder.setRemainingRefundableAmount(remainingRefundableAmount);
                processingRefundOrder.setStatus(RefundStatus.FAILED.getCode());
                processingRefundOrder.setFailReason(refund.getMessage());
            }
            processingRefundOrder.setCreateAt(DateUtils.iso8601ToTimestamp(DateUtils.getNowDateWithISO8601Format()));

            // 更新退款记录状态
            refundOrderService.updateById(processingRefundOrder);

            // 构建返回结果
            return buildRefundOrderDtoFromProcessing(processingRefundOrder, vo, dbOrder, refund);

        } catch (BusinessException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database operation failed: {}", e.getMessage(), e);
            throw new BusinessException("System internal error");
        } catch (Exception e) {
            // 如果退款过程中出现异常，将状态更新为失败
            log.error("Refund processing failed: {}", e.getMessage());
            processingRefundOrder.setFailReason("Refund processing failed: " + e.getMessage());
            processingRefundOrder.setStatus(RefundStatus.FAILED.getCode());
            refundOrderService.updateById(processingRefundOrder);
            throw e;
        }
    }

    public RefundOrderDto getRefundDetail(Order order, RefundDetailVo vo) {
        RefundOrder refundOrder = refundOrderService.selectByRefundSsoIdAndOrderId(vo.getRefundSsoId(), order.getId());
        Currency currency = currencyService.getById(refundOrder.getRefundCurrencyId());
        RefundOrderDto refundOrderDto = new RefundOrderDto();
        refundOrderDto.setSsoOrderId(order.getSsoOrderId());
        refundOrderDto.setRefundAmount(refundOrder.getRefundAmount());
        refundOrderDto.setStatus(RefundStatus.getName(refundOrder.getStatus()).name());
        refundOrderDto.setRemainingRefundableAmount(refundOrder.getRemainingRefundableAmount());
        refundOrderDto.setCurrency(currency.getCode());
        refundOrderDto.setMerchantId(vo.getMerchantId());
        refundOrderDto.setRefundOrderId(String.valueOf(refundOrder.getId()));
        refundOrderDto.setRefundTime(DateUtils.timestampToIso8601(refundOrder.getCreateAt()));
        refundOrderDto.setFailReasons(refundOrder.getFailReason());
        refundOrderDto.setRefundSsoOrderId(vo.getRefundSsoId());
        return refundOrderDto;
    }

    public MerchantBalanceDto getBalance(Shop shop) {
        // 统计循环保证金
        BigDecimal rollingFeeTotal = orderService.getRollingFeeTotalByMerchant(shop.getId());
        // 统计所有完成订单金额
        TotalFiatAmountVo fiatAmountTotal = orderService.getTransferAmountTotalByMerchant(shop.getId());
        // 统计所有的可提取余额
        BigDecimal merchantSettlementFee = movementDataService.calMerchantSettlementFeeTotal(shop.getId());
        MerchantBalanceDto merchantBalanceDto = new MerchantBalanceDto();
        merchantBalanceDto.setTotalBalance(fiatAmountTotal.getTotalAmount());
        merchantBalanceDto.setMarginBalance(rollingFeeTotal);
        merchantBalanceDto.setWithdrawableBalance(merchantSettlementFee);
        merchantBalanceDto.setFrozenBalance(fiatAmountTotal.getTotalAmount().subtract(merchantSettlementFee));
        merchantBalanceDto.setCurrency(fiatAmountTotal.getCurrencyCode());
        return merchantBalanceDto;
    }

    /**
     * 从处理中的退款记录构建DTO
     */
    private RefundOrderDto buildRefundOrderDtoFromProcessing(RefundOrder processingRefundOrder, RefundOrderVo vo,
            Order dbOrder, RefundOrderResult refund) {
        RefundOrderDto dto = new RefundOrderDto();
        dto.setCurrency(vo.getCurrency());
        dto.setMerchantId(String.valueOf(dbOrder.getMerchantId()));
        dto.setSsoOrderId(dbOrder.getSsoOrderId());
        dto.setRefundTime(DateUtils.timestampToIso8601(processingRefundOrder.getCreateAt()));
        dto.setRefundAmount(vo.getRefundAmount());
        dto.setRemainingRefundableAmount(processingRefundOrder.getRemainingRefundableAmount());
        dto.setStatus(RefundStatus.getName(processingRefundOrder.getStatus()).name());
        dto.setRefundOrderId(String.valueOf(processingRefundOrder.getId()));
        dto.setRefundSsoOrderId(vo.getRefundSsoId());

        if (!refund.isSuccess()) {
            dto.setFailReasons(refund.getMessage());
        }

        return dto;
    }

    private BigDecimal calculateRemainingRefundableAmount(Order order) {

        // 查询该订单最近一次成功的退款记录
        RefundOrder lastSuccessRefund = refundOrderService.selectSuccessOrderId(order.getId());
        BigDecimal remainingAmount;

        if (lastSuccessRefund != null && lastSuccessRefund.getRemainingRefundableAmount() != null) {
            // 使用记录的剩余可退款金额
            remainingAmount = lastSuccessRefund.getRemainingRefundableAmount();
        } else {
            // 没有退款记录，剩余金额就是原始金额
            remainingAmount = order.getFiatAmount();
        }
        return remainingAmount;
    }
}
