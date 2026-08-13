package com.games.nuvei;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.games.api.bean.AmlResult;
import com.games.api.bean.PayOrderResult;
import com.games.api.bean.RefundOrderResult;
import com.games.api.dto.TdsDto;
import com.games.api.enums.*;
import com.games.api.vo.CardInfoVo;
import com.games.api.vo.CreateOrderVo;
import com.games.api.vo.PayOrderVo;
import com.games.api.vo.RefundOrderVo;
import com.games.common.exception.BusinessException;
import com.games.common.utils.SignUtil;
import com.games.common.utils.ServletUtils;
import com.games.nuvei.enums.DigitalAssetTypeEnums;
import com.games.nuvei.utils.ErrorCodeMappingUtils;
import com.games.nuvei.vo.OpenOrderVo;
import com.games.nuvei.vo.QuoteResult;
import com.games.payment.domain.*;
import com.games.payment.enums.CardBrandEnums;
import com.games.payment.service.*;
import com.games.system.service.ISysConfigService;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.*;
import com.safecharge.model.Card;
import com.safecharge.response.*;
import com.safecharge.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.games.common.constant.Constants.MASTERCARD_DESCRIPTOR;
import static com.games.common.constant.Constants.PLANT_NAME;
import static com.games.common.constant.Constants.TDS_ON;

@Slf4j
@Service
@RequiredArgsConstructor
public class NuveiApiService {
    private final ICurrencyService currencyService;
    private final IUserService userService;
    private final ICustomerService customerService;
    private final ICardService cardService;
    private final IApiLogService apiLogService;
    private final NuveiConfig config;
    private final ISysConfigService configService;
    private final QuoteService quoteService;
    private final AmlService amlService;
    private final IIpInfoService ipInfoService;
    private final IShopLimitService shopLimitService;
    private final ICustomerTokenMappingService customerTokenMappingService;
    private final CardPaymentLimitService cardPaymentLimitService;

    private static final Long acquirerId = 1L;

    public Order makeOrder(Shop shop, MerchantFeeModel feeModel, CreateOrderVo vo, Crypto crypto) {
        BigDecimal orderAmount = vo.getOrderAmount();
        // 7. 风险评估 (AML)
        AmlStatus amlStatus = AmlStatus.APPROVED;
        Integer amlScore = null;
        if (shop.getCryptoDeliveryMethod() == CryptoDeliveryMethod.HOUSE_DELIVERY.getCode()) {
            AmlResult amlResult = amlService.getAMLScore(vo.getCustomerWallet(), vo.getCryptoCurrency());
            amlScore = amlResult.getScore();
            if (StrUtil.equals(amlResult.getRiskLevel(), AmlRiskLevel.HIGH.name())) {
                amlStatus = AmlStatus.REJECTED;
            }
        }
        String token = crypto.getB2c2Ticker();

        QuoteResult quote;
        // 8. 获取流动性报价
        if (OrderType.isOffOrder(vo.getOrderType())) {
            // 当b2c2Ticker传BTC时，由于1个BTC价格太高，可能拿不到流动性报价，先修改quantity为0.01，然后返回值*100来获取流动性报价
            quote = quoteService.getQuote("sell", token, vo.getCurrency(), NuveiCons.QUANTITY, crypto.getQuoteSource());
        } else {
            quote = quoteService.getQuote("buy", token, vo.getCurrency(), NuveiCons.QUANTITY, crypto.getQuoteSource());
        }
        if (quote.getError() != null) {
            throw new BusinessException(quote.getError());
        }

        // 计算手续费
        boolean isEu = NuveiCons.EU_COUNTRIES.contains(shop.getCountry());
        boolean isUsd = "USD".equals(vo.getCurrency());

        BigDecimal feeRate;
        if (isEu && isUsd) {
            feeRate = feeModel.getProcessFeeEuUsd();
        } else if (isEu) {
            feeRate = feeModel.getProcessFeeEuNonUsd();
        } else if (isUsd) {
            feeRate = feeModel.getProcessFeeNonEuUsd();
        } else {
            feeRate = feeModel.getProcessFeeNonEuNonUsd();
        }

        // 当前时间 + rollingReserveReleaseDays 天，格式为数字型 YYYYMMDD
        DateTime rollingDateTime = DateUtil.offsetDay(DateUtil.date(), feeModel.getRollingReserveReleaseDays());
        String rollingDate = DateUtil.format(rollingDateTime, "yyyyMMdd");

        BigDecimal fee = feeRate.multiply(orderAmount).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal rollingFee = feeModel.calculateRollingFee(orderAmount);
        BigDecimal gatewayFee = feeModel.calculateGatewayFee(orderAmount);

        BigDecimal fiatAmount;
        BigDecimal cryptoAmount;

        String include = vo.getInclude();
        if (StrUtil.isBlank(include)) {
            throw new BusinessException("include field must not be empty");
        }
        FeeIncludeOption includeFee = FeeIncludeOption.valueOf(include);
        boolean isIncludeFee = includeFee == FeeIncludeOption.INCLUDE;

        if (!OrderType.isOffOrder(vo.getOrderType())) {
            fiatAmount = orderAmount;
            if (!isIncludeFee) {
                fiatAmount = fiatAmount.add(fee);
            }
            if (fiatAmount.compareTo(fee) <= 0) {
                throw new BusinessException("Order amount must exceed fee");
            }
            cryptoAmount = quoteService.getCrypto(orderAmount,
                    BigDecimal.valueOf(1.0D).divide(quote.getLiquidityQuote(), 10, RoundingMode.HALF_UP), fee,
                    includeFee);
        } else {
            cryptoAmount = orderAmount;
            if (!isIncludeFee) {
                cryptoAmount = cryptoAmount.add(fee);
            }
            if (cryptoAmount.compareTo(fee) <= 0) {
                throw new BusinessException("Order amount must exceed fee");
            }
            fiatAmount = quoteService.getFiat(orderAmount, quote.getLiquidityQuote(), fee, includeFee);
        }

        // 创建订单对象
        Order order = BeanUtil.copyProperties(vo, Order.class, "customerId");
        order.setFiatAmount(fiatAmount);
        order.setCryptoAmount(cryptoAmount);
        order.setProcessingFee(fee);
        order.setGatewayFee(gatewayFee);
        order.setRollingFee(rollingFee);
        order.setRollingDate(Integer.parseInt(rollingDate));
        order.setLiquidityQuote(quote.getLiquidityQuote());
        order.setLiquidityProvider(quote.getLiquidityProvider());
        order.setAmlStatus(amlStatus.ordinal());
        order.setAmlScore(amlScore);
        // String orderStatus = amlStatus == AmlStatus.APPROVED ?
        // OrderStatus.WAITING_FOR_DEPOSIT.getCode()
        // : OrderStatus.AML_HOLD.getCode();

        String orderStatus = OrderStatus.WAITING_FOR_DEPOSIT.getCode();
        order.setIncludeFee(includeFee.getCode());
        order.setStatus(orderStatus, "Nuvei默认新订单状态");
        return order;
    }

    public PayOrderResult pay(Shop shop, Order order, PayOrderVo vo) {
        String errorReason = null;
        PayOrderResult result = new PayOrderResult();

        String userTokenId = String.valueOf(order.getCustomerId());
        String clientUniqueId = String.valueOf(order.getId());
        String clientRequestId = String.valueOf(order.getId());

        // 支付卡号
        String cardNo = vo.getCardInfo().getCardNumber();
        Card card = vo.toCard();
        List<com.games.payment.domain.Card> cardDbs = cardService.getCardsByCardNumber(card.getCardNumber(),
                acquirerId);
        String cardBrand = null;
        if (!cardDbs.isEmpty()) {
            cardBrand = cardDbs.get(0).getCardBrand();
        }
        if (StrUtil.isBlank(cardBrand)) {
            cardBrand = getCardBrand(clientUniqueId, clientRequestId, cardNo);
        }
        // 不同的cardBand走不同的通道
        Safecharge safecharge = config.init(cardBrand);

        Customer customer = customerService.getById(order.getCustomerId());
        User user = userService.getById(customer.getUserId());
        String userEmail = vo.getCardInfo().getEmail();

        if (StrUtil.isBlank(userEmail)) {
            userEmail = user.getEmail();
        }
        String emailCopy = userEmail;
        String currency = currencyService.getById(order.getCurrencyId()).getCode();
        BigDecimal fiatAmount = order.getFiatAmount().setScale(2, RoundingMode.UP);
        String amount = fiatAmount.toString();

        Constants.TransactionType transactionType = Constants.TransactionType.Sale;

        DeviceDetails deviceDetails = vo.toDeviceDetails();
        // BrowserDetails details = NuveiCons.getBrowserDetails(vo);

        CardInfoVo cardInfo = vo.getCardInfo();

        RecipientDetails recipientDetails = new RecipientDetails();
        recipientDetails.setFirstName(cardInfo.getFirstName());
        recipientDetails.setLastName(cardInfo.getLastName());

        DynamicDescriptor dynamicDescriptor = new DynamicDescriptor();
        dynamicDescriptor.setMerchantName(PLANT_NAME + shop.getDescriptor());
        if (CardBrandEnums.MASTERCARD.getValue().equals(cardBrand)) {
            dynamicDescriptor.setMerchantName(PLANT_NAME + shop.getDescriptor() + MASTERCARD_DESCRIPTOR);
        }
        InitPaymentCard cardInit = BeanUtil.copyProperties(card, InitPaymentCard.class);
        InitPaymentPaymentOption initPaymentOption = new InitPaymentPaymentOption();
        initPaymentOption.setCard(cardInit);
        InitPaymentResponse response = null;
        PaymentOption paymentOption = new PaymentOption();
        paymentOption.setCard(card);
        // 提高支付概率，init就需要替换userTokenId
        com.games.payment.domain.Card cardDb = null;
        boolean shop3DsOpen = TDS_ON.equals(shop.getIs3dsOn());
        // 3ds开关打开，不复用token
        if (configService.isUserPaymentOptionOpen() && !shop3DsOpen) {
            Pair<String, com.games.payment.domain.Card> cardPair = dealUserToken(order, card, paymentOption, cardDbs);
            userTokenId = cardPair.getLeft();
            cardDb = cardPair.getRight();
        }
        try {
            response = safecharge.initPayment(userTokenId, clientUniqueId, clientRequestId, currency, amount,
                    deviceDetails, initPaymentOption, null,
                    null, null, null, null, recipientDetails, null);
        } catch (SafechargeException e) {
            errorReason = e.getMessage();
        }
        // 当返回ExtendErrorCode时，进行错误原因映射，方便查看问题
        if (response != null && response.getGwErrorCode() != null && response.getGwExtendedErrorCode() != 0) {
            String errReason = ErrorCodeMappingUtils.dealGwErrorCode(response.getGwErrorCode(),
                    response.getGwExtendedErrorCode());
            if (errReason != null) {
                response.setGwErrorReason(errReason);
            }
        }
        boolean isInitSucc = response != null && response.getStatus() == Constants.APIResponseStatus.SUCCESS
                && response.getGwErrorCode() == 0 && response.getErrCode() == 0;
        String param = response == null ? null : response.getParam();
        String resJson = response == null ? null : response.getJson();
        ApiStatus status = ApiStatus.SUCC;
        if (response == null) {
            status = ApiStatus.ERR;
        } else if (!isInitSucc) {
            status = ApiStatus.FAIL;
        }
        apiLogService.saveLog(ApiType.INIT_PAYMENT, status, order.getId(), order.getSsoOrderId(), cardNo, param,
                resJson, shop.getId(), deviceDetails.getIpAddress());

        if (isInitSucc) {
            CardResponse cardResponse;
            String version = response.getPaymentOption().getCard().getThreeD().getVersion();
            String relatedTransactionId = response.getTransactionId();

            String methodUrl = response.getPaymentOption().getCard().getThreeD().getMethodUrl();
            String methodPayload = response.getPaymentOption().getCard().getThreeD().getMethodPayload();
            boolean isSupportTds = Boolean
                    .parseBoolean(response.getPaymentOption().getCard().getThreeD().getV2supported());

            // 这个时候需要进行3DS认证
            if (isSupportTds && StrUtil.isAllNotBlank(methodUrl, methodPayload)) {
                // TdsDto tdsDto = new TdsDto(methodUrl, methodPayload);
                // return new PayOrderResult(null, true, tdsDto);
            }

            log.info("订单号:{},支付卡:{},是否支持3DS:{},商户3DS是否开启:{}", order.getId(),
                    SignUtil.maskPan(cardNo), isSupportTds, shop3DsOpen);
            if (shop3DsOpen && isSupportTds) {
                ThreeD threeD = new ThreeD();
                threeD.setMethodCompletionInd("U");
                threeD.setPlatformType("02");
                threeD.setVersion(version);
                threeD.setNotificationURL(config.getGatewayPayUrl() + NuveiCons.TDS_BACK_URL);
                threeD.setMerchantURL(config.getGatewayPayUrl());
                V2AdditionalParams additionalParams = new V2AdditionalParams();
                additionalParams.setChallengePreference("01");
                additionalParams.setChallengeWindowSize("05");

                HttpServletRequest request = ServletUtils.getRequest();
                BrowserDetails browserDetails = request == null ? NuveiCons.defaultBrowserDetails()
                        : NuveiCons.fromRequest(request);
                browserDetails.setIp(deviceDetails.getIpAddress());
                threeD.setBrowserDetails(browserDetails);

                threeD.setV2AdditionalParams(additionalParams);
                card.setThreeD(threeD);
            }

            // 存储支付的卡信息
            if (cardDb != null) {
                userEmail = cardDb.getCardholderEmailAddress();
                // 读区卡上面的token，提高支付概率
            }
            UserAddress billingAddress = vo.toUserAddress(userEmail, "");

            PaymentResponse payResponse = null;
            // 加密货币传 7，Token 传 3
            String digitalAssetType = DigitalAssetTypeEnums.CRYPTOCURRENCY.getCode();
            OrderType orderType = OrderType.fromCode(order.getType());
            List<OrderType> orderTypeList = Arrays.asList(OrderType.TOKEN_ON_RAMP, OrderType.TOKEN_OFF_RAMP);
            if (orderTypeList.contains(orderType)) {
                digitalAssetType = DigitalAssetTypeEnums.BLOCKCHAIN_NATIVE_TOKEN.getCode();
            }
            try {
                payResponse = safecharge.payment(userTokenId, clientUniqueId, clientRequestId, paymentOption, null,
                        currency, amount, null, null, deviceDetails, null, null,
                        billingAddress, dynamicDescriptor, null, null,
                        NuveiCons.defaultUrlDetails(config.getGatewayPayUrl()), null,
                        null, null, relatedTransactionId, transactionType, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, recipientDetails, null, null,
                        null, null, digitalAssetType);
            } catch (SafechargeException e) {
                // 支付调用异常，设置为处理中状态，因为可能是网络原因导致的没有拿到返回结果
                log.error("订单:{}, Payment调用异常: {}", order.getId(), e.getMessage());
                result.setSucc(true); // 设置成功标志，确保订单状态会被更新
                result.setOrderStatus(OrderStatus.APPROVED); // 设置为处理中
                result.setMessage(e.getMessage());
                result.setRelatedTransactionId(relatedTransactionId);
                // 记录异常日志
                apiLogService.saveLog(ApiType.PAYMENT, ApiStatus.ERR, order.getId(), order.getSsoOrderId(),
                        cardNo, null, e.getMessage(), shop.getId(), deviceDetails.getIpAddress());
                return result; // 直接返回
            }

            boolean isPaySucc = false;
            if (payResponse != null) {
                isPaySucc = payResponse.getStatus() == Constants.APIResponseStatus.SUCCESS
                        && payResponse.getGwErrorCode() == 0 && payResponse.getErrCode() == 0;
            }
            param = payResponse == null ? null : payResponse.getParam();
            resJson = payResponse == null ? null : payResponse.getJson();
            if (payResponse == null) {
                status = ApiStatus.ERR;
            } else if (!isPaySucc) {
                status = ApiStatus.FAIL;
            }
            apiLogService.saveLog(ApiType.PAYMENT, status, order.getId(), order.getSsoOrderId(), cardNo, param, resJson,
                    shop.getId(), deviceDetails.getIpAddress());

            System.out.println(StrUtil.format("订单{}, 支付结果为:", order.getId(), resJson));

            if (payResponse != null && StrUtil.isNotBlank(payResponse.getGwErrorReason())) {
                System.out.println(StrUtil.format("1.交易编号:{},支付失败,错误码为={},扩展错误码为={},错误原为={}",
                        payResponse.getTransactionId(), payResponse.getGwErrorCode(),
                        payResponse.getGwExtendedErrorCode(), payResponse.getGwErrorReason()));
                // 如果是IP黑名单限制
                if (StrUtil.contains(payResponse.getGwErrorReason(), "TRANSREASON1=1000000154")) {
                    String ip = vo.getDeviceDetail().getIpAddress();
                    System.out.println("被拉黑的IP:" + ip);
                    ipInfoService.saveBlackIp(ip);
                }
            }

            String userPaymentOptionId = (payResponse != null && payResponse.getPaymentOption() != null)
                    ? payResponse.getPaymentOption().getUserPaymentOptionId()
                    : null;
            // 软支付，待研究
            // boolean softDecline = StrUtil.equals("softDecline",
            // safecharge.getPaymentOption().getCard().getThreeD().getFlow());
            if (isPaySucc) {
                com.games.payment.domain.Card savedCard = null;
                try {
                    cardResponse = payResponse.getPaymentOption().getCard();

                    // 保存或更新卡信息
                    savedCard = saveOrUpdateCardInfo(cardDbs, vo, card, cardResponse, userEmail, user.getId(),
                            userPaymentOptionId, deviceDetails, emailCopy);

                    // 累加支付成功金额
                    updateShopLimit(shop.getId(), fiatAmount);
                } catch (Exception e) {
                    log.error("更新卡信息失败：{}", e.getMessage());
                }
                boolean isTds = StrUtil.equals(payResponse.getTransactionStatus(),
                        NuveiTransactionStatus.REDIRECT.name());
                ThreeDResponse threeD = payResponse.getPaymentOption().getCard().getThreeD();
                String acsUrl = threeD.getAcsUrl();
                String cReq = threeD.getcReq();
                TdsDto tdsDto = isTds ? new TdsDto(acsUrl, cReq) : null;

                OrderStatus orderStatus = isTds ? OrderStatus.TDS_PENDING : OrderStatus.DEPOSIT_DECLINED;
                if (StrUtil.equals(payResponse.getTransactionStatus(), NuveiTransactionStatus.APPROVED.name())) {
                    orderStatus = OrderStatus.APPROVED;
                }
                result = new PayOrderResult(true, tdsDto, orderStatus);
                result.setSuperOrderId(payResponse.getOrderId());
                result.setTransactionId(payResponse.getTransactionId());
                result.setRelatedTransactionId(relatedTransactionId);
                result.setAcquirerId(threeD.getAcsTransID());
                result.setUserPaymentOptionId(userPaymentOptionId);
                result.setIs3DSAuthenticated(isSupportTds && shop3DsOpen);
                result.setCardInfo(savedCard != null ? savedCard : cardDb);
            } else {
                if (payResponse != null && StrUtil.isNotBlank(payResponse.getOrderId())) {
                    result.setSuperOrderId(payResponse.getOrderId());
                }
                if (payResponse != null && StrUtil.isNotBlank(payResponse.getTransactionId())) {
                    result.setTransactionId(payResponse.getTransactionId());
                }
                if (payResponse != null && StrUtil.isNotBlank(payResponse.getGwErrorReason())) {
                    result.setMessage(payResponse.getGwErrorReason());
                    System.out.println(StrUtil.format("交易编号:{},支付失败,错误码为={},扩展错误码为={},错误原为={}",
                            payResponse.getTransactionId(), payResponse.getGwErrorCode(),
                            payResponse.getGwExtendedErrorCode(), payResponse.getGwErrorReason()));
                }
            }
            return result;
        } else if (response != null) {
            errorReason = resolveInitPaymentError(response);
        }
        result.setMessage(errorReason);
        return result;
    }

    public PaymentResponse finalPayOrder(Shop shop, Order order) throws SafechargeException {
        String userTokenId = String.valueOf(order.getCustomerId());
        String clientUniqueId = String.valueOf(order.getId());
        String clientRequestId = String.valueOf(order.getId());
        PayOrderVo vo = JSONUtil.toBean(order.getPayInfo(), PayOrderVo.class);
        // payInfo 已脱敏，卡号仅用于日志展示
        String maskedCardNo = vo != null && vo.getCardInfo() != null ? vo.getCardInfo().getCardNumber() : null;
        String cardFinger = vo != null && vo.getCardInfo() != null ? vo.getCardInfo().getCardFinger() : null;
        Customer customer = customerService.getById(order.getCustomerId());
        User user = userService.getById(customer.getUserId());
        com.games.payment.domain.Card cardDb = resolveCardForOrder(cardFinger, order.getUserPaymentOptionId(),
                user.getId(), acquirerId);
        String cardBrand = cardDb != null ? cardDb.getCardBrand() : null;
        Safecharge safecharge = cardBrand != null ? config.init(cardBrand) : config.init();
        // Customer customer = customerService.getById(order.getCustomerId());
        // User user = userService.getById(customer.getUserId());
        String userEmail = vo.getCardInfo().getEmail();
        String userPhone = "";

        String currency = currencyService.getById(order.getCurrencyId()).getCode();
        BigDecimal fiatAmount = order.getFiatAmount().setScale(2, RoundingMode.UP);
        String amount = fiatAmount.toString();

        UserAddress billingAddress = vo.toUserAddress(userEmail, userPhone);

        PaymentOption paymentOption = new PaymentOption();
        String userPaymentOptionId = order.getUserPaymentOptionId();
        if (StrUtil.isBlank(userPaymentOptionId) && cardDb != null) {
            userPaymentOptionId = cardDb.getToken();
        }
        if (StrUtil.isBlank(userPaymentOptionId)) {
            throw new BusinessException("UserPaymentOptionId is missing");
        }
        paymentOption.setUserPaymentOptionId(userPaymentOptionId);
        DeviceDetails deviceDetails = new DeviceDetails();
        String ip = order.getPaymentIp();
        deviceDetails.setIpAddress(ip);
        String relatedTransactionId = order.getTransactionId();

        DynamicDescriptor dynamicDescriptor = new DynamicDescriptor();
        dynamicDescriptor.setMerchantName(PLANT_NAME + shop.getDescriptor());

        RecipientDetails recipientDetails = new RecipientDetails();
        recipientDetails.setFirstName(vo.getCardInfo().getFirstName());
        recipientDetails.setLastName(vo.getCardInfo().getLastName());

        PaymentResponse payResponse = safecharge.payment(userTokenId, clientUniqueId, clientRequestId, paymentOption,
                null,
                currency, amount, null, null, deviceDetails, null, null,
                billingAddress, dynamicDescriptor, null, null, NuveiCons.defaultUrlDetails(config.getGatewayPayUrl()),
                null,
                null, null, relatedTransactionId, Constants.TransactionType.Sale, null, null, null,
                null, null, null, null, null,
                null, null, null, null, recipientDetails, null, null,
                null, null, null);

        apiLogService.saveLog(ApiType.PAYMENT, ApiStatus.SUCC, order.getId(), order.getSsoOrderId(), maskedCardNo,
                payResponse.getParam(), payResponse.getJson(), shop.getId(), ip);

        // 累加支付成功金额
        updateShopLimit(shop.getId(), fiatAmount);
        return payResponse;
    }

    private String resolveInitPaymentError(InitPaymentResponse response) {
        if (response.getErrCode() != 0 && StrUtil.isNotBlank(response.getReason())) {
            return response.getReason();
        }
        if (response.getGwErrorCode() != 0 && StrUtil.isNotBlank(response.getGwErrorReason())) {
            return response.getGwErrorReason();
        }
        return null;
    }

    private Pair<String, com.games.payment.domain.Card> dealUserToken(Order order, Card card,
            PaymentOption paymentOption,
            List<com.games.payment.domain.Card> cards) {
        return dealUserToken(order, card, paymentOption, cards, false);
    }

    private Pair<String, com.games.payment.domain.Card> dealUserToken(Order order, Card card,
            PaymentOption paymentOption,
            List<com.games.payment.domain.Card> cards, boolean isResume) {
        String userTokenId = String.valueOf(order.getCustomerId());
        if (!isResume && (cards.isEmpty() || cards.size() < configService.getMaxMappingCount())) {
            return Pair.of(String.valueOf(order.getCustomerId()), null);
        }
        Triple<String, String, com.games.payment.domain.Card> resolvedToken = resolveUserPaymentOptionId(order, cards,
                card);
        log.info("订单:{},使用卡:{},命中token支付", order.getId(), SignUtil.maskPan(card.getCardNumber()));
        if (StrUtil.isNotBlank(resolvedToken.getLeft()) && StrUtil.isNotBlank(resolvedToken.getMiddle())) {
            paymentOption.setUserPaymentOptionId(resolvedToken.getLeft());
            paymentOption.setCard(null);
            userTokenId = resolvedToken.getMiddle();
        }
        return Pair.of(userTokenId, resolvedToken.getRight());
    }

    private Triple<String, String, com.games.payment.domain.Card> resolveUserPaymentOptionId(Order order,
            List<com.games.payment.domain.Card> cards,
            Card card) {
        BigDecimal currentAmount = order.getFiatAmount();
        String cardNumber = card.getCardNumber();
        Long orderCustomerId = order.getCustomerId();
        for (com.games.payment.domain.Card cardDb : cards) {
            Long userID = cardDb.getUserId();
            Long customerId = cardService.getCustomerIdByCardNumberAndUserID(cardNumber, userID, acquirerId,
                    order.getMerchantId());
            boolean overLimit = cardPaymentLimitService.isOverLimit(customerId, currentAmount);
            if (overLimit) {
                log.info("cardNumber: {}, 用户:{} 超限，切换到下一个token: {}", SignUtil.maskPan(cardNumber), customerId,
                        cardDb.getToken());
                continue;
            }
            if (!customerId.equals(orderCustomerId)) {
                order.setCustomerId(customerId);
            }
            return Triple.of(cardDb.getToken(), String.valueOf(customerId), cardDb);
        }
        // 如果全部都超限了，也要取一张卡进行支付
        com.games.payment.domain.Card lastCard = cards.get(cards.size() - 1);
        Long userID = lastCard.getUserId();
        Long customerId = cardService.getCustomerIdByCardNumberAndUserID(cardNumber, userID, acquirerId,
                order.getMerchantId());
        return Triple.of(lastCard.getToken(), String.valueOf(customerId), lastCard);
    }

    /**
     * 保存或更新卡信息
     */
    private com.games.payment.domain.Card saveOrUpdateCardInfo(
            List<com.games.payment.domain.Card> cardDbs,
            PayOrderVo payVo,
            Card card,
            CardResponse cardResponse,
            String userEmail,
            Long userId,
            String userPaymentOptionId,
            DeviceDetails deviceDetails,
            String copyEmail) {
        // 由于现在card表里面的token有为空的数据，需要做个兼容逻辑，先更新
        if (cardDbs.size() == 1) {
            com.games.payment.domain.Card cardDb = cardDbs.get(0);
            if (StrUtil.isBlank(cardDb.getRemark())) {
                if (cardDb.getUserId() == null || !cardDb.getUserId().equals(userId)) {
                    cardDb.setUserId(userId);
                }
                cardDb.setToken(userPaymentOptionId);
                cardDb.setCardholderEmailAddress(userEmail);
                cardDb.setCreatedAt(new Date());
                cardDb.setRemark("updated");
                cardService.updateById(cardDb);
                return cardDb;
            }

        }
        boolean duplicateTokenExists = StrUtil.isNotBlank(userPaymentOptionId)
                && cardDbs.stream().anyMatch(db -> Objects.equals(userPaymentOptionId, db.getToken()));
        if (!duplicateTokenExists && (cardDbs.isEmpty() || cardDbs.size() < configService.getMaxMappingCount())) {
            // 创建新卡
            CardInfoVo cardInfo = payVo.getCardInfo();
            com.games.payment.domain.Card cardDb = com.games.payment.domain.Card.init(cardInfo.getCardNumber());
            BeanUtil.copyProperties(cardInfo, com.games.payment.domain.Card.class);
            cardDb.setExpiryYear(Long.parseLong(card.getExpirationYear()));
            cardDb.setExpiryMonth(Long.parseLong(card.getExpirationMonth()));
            cardDb.setCardholderEmailAddress(userEmail);
            cardDb.setCardholderName(card.getCardHolderName());
            cardDb.setUserId(userId);
            cardDb.setAcquirerId(1L);
            cardDb.setToken(userPaymentOptionId);
            cardDb.setBin(cardResponse.getBin());
            cardDb.setIssuerCountry(cardResponse.getIssuerCountry());
            String cardType = StrUtil.isNotBlank(cardResponse.getCardType()) ? cardResponse.getCardType() : "UNKOWN";
            cardDb.setCardType(cardType);
            cardDb.setCardBrand(cardResponse.getCardBrand());
            cardDb.setScheme(deviceDetails.getIpAddress());
            cardService.save(cardDb);
            return cardDb;
        } else {
            // 更新现有卡
            Map<String, Long> emailCounts = cardDbs.stream()
                    .collect(Collectors.groupingBy(com.games.payment.domain.Card::getCardholderEmailAddress,
                            Collectors.counting()));
            boolean hasDuplicateEmail = emailCounts.values().stream().anyMatch(count -> count > 1);
            boolean emailUpdated = false; // 保证只更新一次
            for (com.games.payment.domain.Card cardDb : cardDbs) {
                boolean needUpdate = false;
                String responseCardType = StrUtil.isNotBlank(cardResponse.getCardType()) ? cardResponse.getCardType()
                        : "UNKOWN";

                if (!Objects.equals(cardDb.getCardType(), responseCardType)) {
                    cardDb.setCardType(responseCardType);
                    needUpdate = true;
                }
                if (!Objects.equals(cardDb.getCardBrand(), cardResponse.getCardBrand())) {
                    cardDb.setCardBrand(cardResponse.getCardBrand());
                    needUpdate = true;
                }
                if (hasDuplicateEmail
                        && !emailUpdated
                        && StrUtil.isNotBlank(copyEmail)
                        && emailCounts.getOrDefault(cardDb.getCardholderEmailAddress(), 0L) > 1
                        && !Objects.equals(cardDb.getCardholderEmailAddress(), copyEmail)) {
                    cardDb.setCardholderEmailAddress(copyEmail);
                    emailUpdated = true;
                    needUpdate = true;
                }
                if (needUpdate) {
                    cardService.updateById(cardDb);
                }
            }
            if (duplicateTokenExists) {
                return cardDbs.stream()
                        .filter(db -> Objects.equals(userPaymentOptionId, db.getToken()))
                        .findFirst()
                        .orElse(null);
            }
            return null;
        }
    }

    public RefundOrderResult refund(Order order, RefundOrderVo vo) {
        String errorReason = null;
        String clientUniqueId = String.valueOf(order.getId());
        String clientRequestId = String.valueOf(order.getId());
        PayOrderVo payOrderVo = JSONUtil.toBean(order.getPayInfo(), PayOrderVo.class);
        String cardFinger = payOrderVo != null && payOrderVo.getCardInfo() != null
                ? payOrderVo.getCardInfo().getCardFinger()
                : null;
        com.games.payment.domain.Card cardDb = resolveCardForOrder(cardFinger, order.getUserPaymentOptionId(),
                null, acquirerId);
        String cardBrand = cardDb != null ? cardDb.getCardBrand() : null;
        Safecharge safecharge = cardBrand != null ? config.init(cardBrand) : config.init();
        String refundAmount = String.valueOf(vo.getRefundAmount());
        String currency = vo.getCurrency();
        String transactionId = order.getTransactionId();
        JSONObject refundRequestParam = JSONUtil.createObj()
                .set("clientUniqueId", clientUniqueId)
                .set("clientRequestId", clientRequestId)
                .set("refundAmount", refundAmount)
                .set("currency", currency)
                .set("transactionId", transactionId)
                .set("refundSsoId", vo.getRefundSsoId());
        RefundTransactionResponse refundResponse = null;
        boolean isRefundSuc = false;
        try {
            refundResponse = safecharge.refundTransaction(clientUniqueId, clientRequestId,
                    null, refundAmount, null, null, currency, null, null,
                    null, transactionId, null, null, null, null);
        } catch (SafechargeException e) {
            errorReason = e.getMessage();
        }
        log.info("Nuvei refund response, orderId={}, response={}", order.getId(), refundResponse);
        String param = refundRequestParam.toString();
        String resJson;
        ApiStatus status = ApiStatus.SUCC;
        if (refundResponse == null) {
            status = ApiStatus.ERR;
            resJson = errorReason;
        } else {
            if (StrUtil.isNotBlank(refundResponse.getParam())) {
                param = refundResponse.getParam();
            }
            resJson = StrUtil.isNotBlank(refundResponse.getJson()) ? refundResponse.getJson()
                    : JSONUtil.toJsonStr(refundResponse);
            isRefundSuc = refundResponse.getStatus() == Constants.APIResponseStatus.SUCCESS
                    && refundResponse.getGwErrorCode() == 0 && refundResponse.getErrCode() == 0;
            if (!isRefundSuc) {
                status = ApiStatus.FAIL;
            }
        }
        apiLogService.saveLog(ApiType.REFUND_ORDER, status, order.getId(), order.getSsoOrderId(), null, param,
                resJson, order.getMerchantId(), order.getPaymentIp());
        if (refundResponse == null) {
            return new RefundOrderResult(errorReason);
        }
        RefundOrderResult refundOrderResult = new RefundOrderResult();
        refundOrderResult.setTransactionId(refundResponse.getTransactionId());
        if (isRefundSuc) {
            refundOrderResult.setSuccess(true);
        } else {
            if (StrUtil.isNotBlank(refundResponse.getReason())) {
                errorReason = refundResponse.getReason();
            } else if (StrUtil.isNotBlank(refundResponse.getGwErrorReason())) {
                errorReason = refundResponse.getGwErrorReason();
            }
            refundOrderResult.setSuccess(false);
            refundOrderResult.setMessage(errorReason);
        }
        return refundOrderResult;
    }

    private com.games.payment.domain.Card resolveCardForOrder(String cardFinger, String token, Long userId,
            Long acquirerId) {
        try {
            if (StrUtil.isNotBlank(cardFinger)) {
                com.games.payment.domain.Card card = cardService.lambdaQuery()
                        .eq(com.games.payment.domain.Card::getCardFinger, cardFinger)
                        .eq(acquirerId != null, com.games.payment.domain.Card::getAcquirerId, acquirerId)
                        .eq(userId != null, com.games.payment.domain.Card::getUserId, userId)
                        .last("limit 1")
                        .one();
                if (card != null) {
                    return card;
                }
            }
            if (StrUtil.isNotBlank(token)) {
                return cardService.lambdaQuery()
                        .eq(com.games.payment.domain.Card::getToken, token)
                        .eq(acquirerId != null, com.games.payment.domain.Card::getAcquirerId, acquirerId)
                        .eq(userId != null, com.games.payment.domain.Card::getUserId, userId)
                        .last("limit 1")
                        .one();
            }
        } catch (Exception e) {
            log.warn("resolveCardForOrder error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 更新商户限额，累加支付成功金额
     * 
     * @param shopId     商户ID
     * @param fiatAmount 法币金额
     */
    private void updateShopLimit(Long shopId, BigDecimal fiatAmount) {
        ShopLimit shopLimit = shopLimitService.getByShopId(shopId);
        if (shopLimit != null) {
            shopLimit.setValue(shopLimit.getValue().add(fiatAmount));
            shopLimit.setUpdateTime(new Date());
            shopLimitService.updateById(shopLimit);
        } else {
            shopLimit = new ShopLimit();
            shopLimit.setShopId(shopId);
            BigDecimal limitValue = shopLimit.getValue() == null ? BigDecimal.ZERO : shopLimit.getValue();
            shopLimit.setValue(limitValue.add(fiatAmount));
            shopLimitService.save(shopLimit);
        }
    }

    public OpenOrderVo openOrder(String amount, String currency) throws SafechargeException {
        // for openOrder
        String clientUniqueId = "<unique transaction ID in merchant system>";
        String clientRequestId = "<unique request ID in merchant system>";
        // String currency = "USD";
        // String amount = "200";
        String userTokenId = RandomUtil.randomString(9);

        Safecharge safecharge = config.init();
        SafechargeResponse response = safecharge.openOrder(userTokenId, clientRequestId,
                clientUniqueId, null, null, null, null, currency, amount, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null);
        String sessionToken = response.getSessionToken();
        OpenOrderVo vo = new OpenOrderVo();
        vo.setSessionToken(sessionToken);
        vo.setMerchantId(response.getMerchantId());
        vo.setMerchantSiteId(response.getMerchantSiteId());
        vo.setAmount(amount);
        vo.setCurrency(currency);
        return vo;
    }

    public CardDetailsResponse getCardDetails(String clientUniqueId, String clientRequestId, String cardNumber) {
        Safecharge safecharge = config.init();
        try {
            return safecharge.getCardDetails(clientUniqueId, clientRequestId, cardNumber);
        } catch (SafechargeException e) {
            log.error("Failed to get card details for clientUniqueId: {}, error: {}", clientUniqueId, e.getMessage(),
                    e);
        }
        return null;
    }

    public String getCardBrand(String clientUniqueId, String clientRequestId, String cardNumber) {
        CardDetailsResponse cardDetails = getCardDetails(clientUniqueId, clientRequestId, cardNumber);
        if (cardDetails != null) {
            com.games.payment.domain.Card card = cardService.getCardByCardNumber(cardNumber, acquirerId);
            if (card != null && (card.getCardBrand() == null || !card.getCardBrand().equals(cardDetails.getBrand()))) {
                card.setCardBrand(cardDetails.getBrand());
                cardService.updateById(card);
            }
            return cardDetails.getBrand();
        }
        return null;
    }
}
