package com.games.shift4;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.games.api.bean.PayOrderResult;
import com.games.api.bean.RefundOrderResult;
import com.games.api.dto.TdsDto;
import com.games.api.enums.*;
import com.games.api.vo.*;
import com.games.common.constant.Constants;
import com.games.common.constant.Shift4TdsConstants;
import com.games.common.enums.Shift4OperationCode;
import com.games.common.enums.Shift4ReqEnums;
import com.games.common.exception.BusinessException;
import com.games.common.exception.Shift4Exception;
import com.games.common.utils.DateUtils;
import com.games.common.utils.SignUtil;
import com.games.common.utils.CountryCodeUtils;
import com.games.nuvei.CardPaymentLimitService;
import com.games.nuvei.NuveiCons;
import com.games.nuvei.QuoteService;
import com.games.nuvei.vo.QuoteResult;
import com.games.payment.domain.*;
import com.games.payment.domain.Currency;
import com.games.payment.service.*;
import com.games.shift4.utils.Shift4HttpUtils;
import com.games.shift4.utils.Shift4SignUtils;
import com.games.shift4.vo.Shift4ResVo;
import com.games.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Shift4 Payment Gateway API 服务类
 * 实现支付、退款等核心功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Shift4ApiService {

    private final Shift4Config config;
    private final IApiLogService apiLogService;
    private final ICurrencyService currencyService;
    private final QuoteService quoteService;
    private final ICardService cardService;
    private final ICustomerService customerService;
    private final IUserService userService;
    private final ISysConfigService configService;
    private final CardPaymentLimitService cardPaymentLimitService;

    /**
     * Shift4 收单机构ID
     */
    private static final Long SHIFT4_ACQUIRER_ID = 2L;

    /**
     * 创建订单（S4不需要单独的创建订单接口，订单在支付时创建）
     * 这里返回一个基本的订单对象
     */
    public Order makeOrder(Shop shop, MerchantFeeModel feeModel, CreateOrderVo vo, Crypto crypto) {
        boolean isEu = NuveiCons.EU_COUNTRIES.contains(shop.getCountry());
        boolean isUsd = "USD".equals(vo.getCurrency());
        BigDecimal orderAmount = vo.getOrderAmount();
        QuoteResult quote;
        String token = crypto.getB2c2Ticker();
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

        BigDecimal fee = feeRate.multiply(orderAmount).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal rollingFee = feeModel.calculateRollingFee(orderAmount);
        BigDecimal gatewayFee = feeModel.calculateGatewayFee(orderAmount);

        String include = vo.getInclude();
        if (StrUtil.isBlank(include)) {
            throw new BusinessException("include field must not be empty");
        }
        FeeIncludeOption includeFee = FeeIncludeOption.valueOf(include);
        BigDecimal cryptoAmount = quoteService.getCrypto(orderAmount,
                BigDecimal.valueOf(1.0D).divide(quote.getLiquidityQuote(), 10, RoundingMode.HALF_UP), fee,
                includeFee);
        Order order = BeanUtil.copyProperties(vo, Order.class, "customerId");
        order.setFiatAmount(vo.getOrderAmount());
        order.setCryptoAmount(cryptoAmount);
        order.setProcessingFee(fee);
        order.setGatewayFee(gatewayFee);
        order.setRollingFee(rollingFee);
        order.setStatus(OrderStatus.WAITING_FOR_DEPOSIT.getCode(), "S4默认新订单");
        return order;
    }

    /**
     * 支付订单
     * 操作代码 [1] - Sale（授权+捕获）
     * 当 Token 开关打开时，使用 operation code 23 或 11 完成支付
     * 3DS 开关只影响是否传递 3DS 相关参数，不影响 operation code 的选择
     */
    public PayOrderResult pay(Shop shop, Order order, PayOrderVo vo) {
        try {
            // 验证 AFT 参数
            validateAftParameters(vo);

            String cardNumber = vo.getCardInfo().getCardNumber();
            List<Card> cards = cardService.getCardsByCardNumber(cardNumber, SHIFT4_ACQUIRER_ID);

            // 检查是否启用 Token 支付（Token 开关打开时，无论 3DS 是否开启，都走 code 23 或 code 11）
            // 3DS 开关只在 buildPaymentParams 中影响是否传递 3DS 相关参数
            if (configService.shift4SaleWithTokenIsOpen()) {
                // 开关打开：只走 code 23 或 code 11，不走 code 1
                return dealTokenWithSale(shop, order, vo, cards);
            }

            // 普通支付 (operation code 1)
            Map<String, String> params = buildPaymentParams(shop, order, vo, Shift4OperationCode.SALE.getCode(), null);
            Shift4ResVo resVo = executeRequest(
                    "S4支付",
                    params,
                    ApiType.PAYMENT,
                    order,
                    shop.getId(),
                    vo.getCardInfo().getCardNumber(),
                    vo.getDeviceDetail().getIpAddress());

            PayOrderResult result = buildPayOrderResult(resVo, order);

            // 支付成功后保存卡信息
            if (result.isSucc() && result.getOrderStatus() == OrderStatus.COMPLETED) {
                try {
                    // 普通支付 (code 1) 不返回 token，只有 code 23 返回 token
                    // 如果没有 token，不保存卡记录，避免产生无效数据
                    String token = resVo.getToken();
                    if (StrUtil.isNotBlank(token)) {
                        Card savedCard = saveOrUpdateCardInfo(order, vo, resVo, vo.getDeviceDetail().getIpAddress(),
                                token);
                        result.setCardInfo(savedCard);
                    } else {
                        log.info("S4普通支付成功但无Token，跳过保存卡信息 - 订单ID: {}", order.getId());
                    }
                } catch (Exception e) {
                    log.error("S4保存卡信息失败 - 订单ID: {}", order.getId(), e);
                }
            }

            return result;
        } catch (Exception e) {
            log.error("S4支付异常 - 订单ID: {}", order.getId(), e);
            return new PayOrderResult("S4支付异常: " + e.getMessage());
        }
    }

    /**
     * 退款订单
     * 操作代码 [5] - Referral Credit（退款已捕获的交易）
     * 参数说明：
     * - a1: 订单号 (order.id)
     * - a4: 退款金额
     * - a5: 退款币种
     * - g2: sale返回的z1，存储在order.transactionId
     */
    public RefundOrderResult refund(Order order, RefundOrderVo vo) {
        try {
            Shift4ReferralOptions options = new Shift4ReferralOptions();
            options.setCurrency(vo.getCurrency());

            options.setResponseId(order.getTransactionId());

            Shift4ReferralResult result = referralCredit(order, vo.getRefundAmount(), options);

            RefundOrderResult refundResult = new RefundOrderResult();
            refundResult.setSuccess(result.isSuccess());
            refundResult.setTransactionId(result.getProcessorTransactionId());
            refundResult.setMessage(result.getMessage());

            return refundResult;
        } catch (Exception e) {
            log.error("S4退款异常 - 订单ID: {}", order.getId(), e);
            RefundOrderResult result = new RefundOrderResult();
            result.setSuccess(false);
            result.setMessage("S4退款异常: " + e.getMessage());
            return result;
        }
    }

    /**
     * 操作代码 [5] - Referral Credit（转介退款）
     * 用于退款已捕获的交易
     */
    public Shift4ReferralResult referralCredit(Order order, BigDecimal amount, Shift4ReferralOptions options) {
        return processReferralOperation(Shift4OperationCode.REFERRAL_CREDIT.getCode(), order, amount,
                options);
    }

    /**
     * 操作代码 [23] - Create Token with Sale
     * 创建Token并同时进行支付
     */
    public PayOrderResult createTokenWithSale(Shop shop, Order order, PayOrderVo vo) {
        try {
            // 验证 AFT 参数（如果提供了 AFT 信息）
            validateAftParameters(vo);

            Map<String, String> params = buildPaymentParams(shop, order, vo,
                    Shift4OperationCode.CREATE_TOKEN_WITH_SALE.getCode(), null);
            Shift4ResVo resVo = executeRequest(
                    "S4创建Token并支付",
                    params,
                    ApiType.PAYMENT,
                    order,
                    shop.getId(),
                    vo.getCardInfo().getCardNumber(),
                    vo.getDeviceDetail().getIpAddress());

            PayOrderResult result = buildPayOrderResult(resVo, order);

            // 保存Token信息（如果成功且返回了token）
            if (result.isSucc()) {
                String token = resVo.getToken();
                if (StrUtil.isNotBlank(token)) {
                    result.setToken(token);
                    log.info("S4创建Token成功 - 订单ID: {}", order.getId());

                    // 保存 Token 和卡信息到 customer_card_token 表
                    try {
                        Card savedCard = saveOrUpdateCardInfo(order, vo, resVo, vo.getDeviceDetail().getIpAddress(),
                                token);
                        result.setCardInfo(savedCard);
                    } catch (Exception e) {
                        log.error("S4保存Token卡信息失败 - 订单ID: {}", order.getId(), e);
                    }
                } else if (result.getOrderStatus() == OrderStatus.TDS_PENDING) {
                    // 3DS 触发时不预创建卡记录，等 3DS 回调有 token 时再保存
                    log.info("S4触发3DS，等待3DS回调返回Token - 订单ID: {}", order.getId());
                }
            }

            return result;
        } catch (Exception e) {
            log.error("S4创建Token并支付异常 - 订单ID: {}", order.getId(), e);
            return new PayOrderResult("S4创建Token并支付异常: " + e.getMessage());
        }
    }

    /**
     * 操作代码 [101] - Past Transaction Retrieval
     * 查询历史交易信息（统一入口方法）
     * 
     */
    public Shift4TransactionQueryResult retrievePastTransaction(String requestId, String responseId) {
        if (StrUtil.isAllBlank(requestId, responseId)) {
            Shift4TransactionQueryResult result = new Shift4TransactionQueryResult();
            result.setSuccess(false);
            result.setMessage("查询历史交易至少需要提供 requestId 或 responseId 之一");
            return result;
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put(Shift4ReqEnums.MERCHANT_ID.getCode(), config.getMerchantId());
            params.put(Shift4ReqEnums.OPERATION_CODE.getCode(),
                    Shift4OperationCode.PAST_TRANSACTION_RETRIEVAL.getCode());

            if (StrUtil.isNotBlank(requestId)) {
                params.put(Shift4ReqEnums.REQUEST_ID.getCode(), requestId);
                params.put(Shift4ReqEnums.ORIGINAL_REQUEST_ID.getCode(), requestId);
            }
            if (StrUtil.isNotBlank(responseId)) {
                params.put(Shift4ReqEnums.RESPONSE_ID.getCode(), responseId);
            }

            Map<String, String> response = Shift4HttpUtils.executeRequestRaw(
                    config.getUrl(),
                    params,
                    config.getSignatureKey(),
                    "S4查询历史交易 - requestId: " + requestId + ", responseId: " + responseId);

            Shift4TransactionQueryResult result = buildTransactionQueryResult(Shift4SignUtils.toResVo(response));
            result.setRawResponse(response);
            return result;
        } catch (Exception e) {
            log.error("S4查询历史交易异常 - requestId: {}, responseId: {}", requestId, responseId, e);
            Shift4TransactionQueryResult result = new Shift4TransactionQueryResult();
            result.setSuccess(false);
            result.setMessage("查询异常: " + e.getMessage());
            return result;
        }
    }

    /**
     * 操作代码 [92] - 3DS Completions
     * 完成3DS认证后的交易
     */
    public PayOrderResult complete3DS(Order order, String cRes, String ipAddress) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(Shift4ReqEnums.MERCHANT_ID.getCode(), config.getMerchantId());
            params.put(Shift4ReqEnums.OPERATION_CODE.getCode(), Shift4OperationCode.TDS_COMPLETIONS.getCode());
            params.put(Shift4ReqEnums.REQUEST_ID.getCode(), String.valueOf(order.getId()));

            // 3DS认证响应
            params.put(Shift4ReqEnums.C_RES.getCode(), cRes);

            // 原始交易的响应ID
            if (StrUtil.isNotBlank(order.getOrderUuid())) {
                params.put(Shift4ReqEnums.RESPONSE_ID.getCode(), order.getOrderUuid());
            }

            // IP地址
            if (StrUtil.isNotBlank(ipAddress)) {
                params.put(Shift4ReqEnums.IP_ADDRESS.getCode(), ipAddress);
            }

            Shift4ResVo resVo = executeRequest(
                    "S4完成3DS认证",
                    params,
                    ApiType.PAYMENT,
                    order,
                    order.getMerchantId(),
                    null,
                    ipAddress);

            return buildPayOrderResult(resVo, order);
        } catch (Exception e) {
            log.error("S4完成3DS认证异常 - 订单ID: {}", order.getId(), e);
            return new PayOrderResult("S4完成3DS认证异常: " + e.getMessage());
        }
    }

    /**
     * 操作代码 [92] - 3DS Completions (DFP流程)
     * 完成3DS Method设备指纹收集后的交易
     * 
     * @param order      订单
     * @param originalZ1 原始交易的z1 (Shift4交易ID)
     * @param tdsTrxId   3DS交易ID (3ds_trxid)
     * @param compInd    完成指示器 (Y/N)
     * @param ipAddress  客户IP地址
     */
    public PayOrderResult complete3DSWithDfp(Order order, String originalZ1,
            String tdsTrxId, String compInd, String ipAddress) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(Shift4ReqEnums.MERCHANT_ID.getCode(), config.getMerchantId());
            params.put(Shift4ReqEnums.OPERATION_CODE.getCode(), Shift4OperationCode.TDS_COMPLETIONS.getCode());
            params.put(Shift4ReqEnums.REQUEST_ID.getCode(), "3ds" + order.getId());

            // 根据API规范：使用g5(原z1)、3ds_trxid、3ds_compind
            params.put(Shift4ReqEnums.REFERRED_TRANSACTION_ID.getCode(), originalZ1);
            params.put(Shift4ReqEnums.TDS_TRXID.getCode(), tdsTrxId);
            params.put(Shift4ReqEnums.TDS_COMPIND.getCode(), compInd);

            // IP地址
            if (StrUtil.isNotBlank(ipAddress)) {
                params.put(Shift4ReqEnums.IP_ADDRESS.getCode(), ipAddress);
            }

            log.info("S4发送3DS Completion(DFP) - 订单ID: {}, g5: {}, 3ds_trxid: {}, 3ds_compind: {}",
                    order.getId(), originalZ1, tdsTrxId, compInd);

            Shift4ResVo resVo = executeRequest(
                    "S4完成3DS认证(DFP)",
                    params,
                    ApiType.PAYMENT,
                    order,
                    order.getMerchantId(),
                    null,
                    ipAddress);

            return buildPayOrderResult(resVo, order);
        } catch (Exception e) {
            log.error("S4完成3DS认证(DFP)异常 - 订单ID: {}", order.getId(), e);
            return new PayOrderResult("S4完成3DS认证(DFP)异常: " + e.getMessage());
        }
    }

    /**
     * 查询订单交易状态
     * 用于在订单信息查询时，确认订单在 Shift4 端的实际状态
     *
     * @param order 订单
     * @return true 如果交易成功，false 否则
     */
    public boolean queryOrderTransactionStatus(Order order) {
        if (order == null) {
            return false;
        }
        String requestId = String.valueOf(order.getId());
        Shift4TransactionQueryResult result = retrievePastTransaction(requestId, null);

        if (result.isSuccess() && result.getRawResponse() != null) {
            // 检查原始交易是否成功 (_z2=0)
            boolean originalSuccess = isOriginalTransactionSuccess(result.getRawResponse());
            if (originalSuccess) {
                log.info("S4查询订单交易状态成功 - 订单ID: {}", order.getId());
                return true;
            }
        }
        return false;
    }

    /**
     * 处理 Token 复用逻辑（开关打开时调用）
     * - 如果卡记录数量 < 阈值，或者有卡的 token 为空 → 使用 code 23 获取 Token
     * - 如果卡记录数量 >= 阈值，且所有卡都有 token → 使用 code 11 复用 Token
     *
     * @return 支付结果（不会返回 null，开关打开后必须走 code 23 或 code 11）
     */
    private PayOrderResult dealTokenWithSale(Shop shop, Order order, PayOrderVo vo, List<Card> cards) {
        String cardNumber = vo.getCardInfo().getCardNumber();
        BigDecimal currentAmount = order.getFiatAmount();
        Long orderCustomerId = order.getCustomerId();
        int maxCount = configService.getShift4MaxMappingCount();
        log.info("S4 Token支付参数 - 订单ID: {}, 卡数量: {}, 阈值(maxCount): {}", order.getId(), cards.size(), maxCount);
        // 检查是否有任何卡的 token 为空
        boolean hasEmptyToken = cards.isEmpty() || cards.size() < maxCount
                || cards.stream().anyMatch(c -> StrUtil.isBlank(c.getToken()));

        if (hasEmptyToken) {
            // 需要获取新 Token: 使用 code 23 (Create Token with Sale)
            log.info("S4获取Token并支付 [code 23] - 订单ID: {}, 原因: {}", order.getId(),
                    cards.isEmpty() ? "无卡记录" : cards.size() < maxCount ? "卡数量未达阈值" : "存在空Token");
            return createTokenWithSale(shop, order, vo);
        }

        // 所有卡都有 Token: 使用 code 11 (Use Token - Sale)
        // 遍历卡记录，找到未超限的卡
        for (Card cardDb : cards) {
            Long userId = cardDb.getUserId();
            Long customerId = cardService.getCustomerIdByCardNumberAndUserID(cardNumber, userId, SHIFT4_ACQUIRER_ID,
                    order.getMerchantId());

            // 检查是否超限
            boolean overLimit = cardPaymentLimitService.isOverLimit(customerId, currentAmount);
            if (overLimit) {
                log.info("S4 cardNumber: {}, 用户:{} 超限，切换到下一张卡", SignUtil.maskPan(cardNumber), customerId);
                continue;
            }

            // 更新订单的 customerId（如果不同）
            if (!customerId.equals(orderCustomerId)) {
                order.setCustomerId(customerId);
            }

            String token = cardDb.getToken();
            log.info("S4使用Token支付 [code 11] - 订单ID: {}, 卡ID: {}",
                    order.getId(), cardDb.getId());
            return saleUsingToken(order, token, vo.getDeviceDetail().getIpAddress());
        }

        // 所有卡都超限，使用最后一张卡进行支付
        Card lastCard = cards.get(cards.size() - 1);
        Long userId = lastCard.getUserId();
        Long customerId = cardService.getCustomerIdByCardNumberAndUserID(cardNumber, userId, SHIFT4_ACQUIRER_ID,
                order.getMerchantId());

        if (!customerId.equals(orderCustomerId)) {
            order.setCustomerId(customerId);
        }

        String token = lastCard.getToken();
        log.info("S4所有卡超限，使用最后一张卡Token支付 [code 11] - 订单ID: {}", order.getId());
        return saleUsingToken(order, token, vo.getDeviceDetail().getIpAddress());
    }

    /**
     * 操作代码 [11] - Use Token – Sale
     * 使用已有的 Token 进行支付（不需要传卡号，更安全）
     * 
     * 必填参数: M, K, O=11, a1, a4, a5, g1
     */
    private PayOrderResult saleUsingToken(Order order, String token, String ipAddress) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(Shift4ReqEnums.MERCHANT_ID.getCode(), config.getMerchantId());
            params.put(Shift4ReqEnums.OPERATION_CODE.getCode(), Shift4OperationCode.USE_TOKEN_SALE.getCode());
            params.put(Shift4ReqEnums.REQUEST_ID.getCode(), String.valueOf(order.getId()));
            params.put(Shift4ReqEnums.AMOUNT.getCode(), toMinorUnits(order.getFiatAmount()));
            params.put(Shift4ReqEnums.CURRENCY.getCode(), resolveCurrencyCode(order, null));
            params.put(Shift4ReqEnums.TOKEN.getCode(), token);

            Shift4ResVo resVo = executeRequest(
                    "S4使用Token支付",
                    params,
                    ApiType.PAYMENT,
                    order,
                    null, // shopId 可选
                    null, // cardNumber 不需要传
                    ipAddress);

            return buildPayOrderResult(resVo, order);
        } catch (Exception e) {
            log.error("S4使用Token支付异常 - 订单ID: {}", order.getId(), e);
            return new PayOrderResult("S4使用Token支付异常: " + e.getMessage());
        }
    }

    /**
     * 构建支付请求参数
     */
    private Map<String, String> buildPaymentParams(Shop shop, Order order, PayOrderVo vo,
            String operationCode, Shift4AuthorizationOptions options) {
        Map<String, String> params = new HashMap<>();
        params.put(Shift4ReqEnums.MERCHANT_ID.getCode(), config.getMerchantId());
        params.put(Shift4ReqEnums.OPERATION_CODE.getCode(), operationCode);

        String requestId = options != null && StrUtil.isNotBlank(options.getRequestId())
                ? options.getRequestId()
                : String.valueOf(order.getId());
        params.put(Shift4ReqEnums.REQUEST_ID.getCode(), requestId);

        params.put(Shift4ReqEnums.AMOUNT.getCode(), toMinorUnits(order.getFiatAmount()));
        params.put(Shift4ReqEnums.CURRENCY.getCode(), resolveCurrencyCode(order, null));

        // 卡信息
        params.put(Shift4ReqEnums.CARD_NUMBER.getCode(), vo.getCardInfo().getCardNumber());
        params.put(Shift4ReqEnums.CARD_EXPIRY_MONTH.getCode(),
                normalizeCardExpiryMonth(vo.getCardInfo().getExpiryMonth()));
        params.put(Shift4ReqEnums.CARD_EXPIRY_YEAR.getCode(), vo.getCardInfo().getExpiryYear().substring(2));
        params.put(Shift4ReqEnums.CARD_CVV.getCode(), vo.getCardInfo().getCvv());

        String cardholderName = vo.getCardInfo().getFirstName() + " " + vo.getCardInfo().getLastName();
        params.put(Shift4ReqEnums.CARDHOLDER_NAME.getCode(), cardholderName);
        params.put(Shift4ReqEnums.CARDHOLDER_EMAIL.getCode(), vo.getCardInfo().getEmail());

        if (vo.getAddressInfo() != null) {
            if (StrUtil.isNotBlank(vo.getAddressInfo().getAddressLine1())) {
                String address = vo.getAddressInfo().getAddressLine1();
                String[] parts = address.split("\\s+", 2);
                if (parts.length > 0 && parts[0].matches("\\d+")) {
                    params.put(Shift4ReqEnums.ADDRESS_HOUSE_NUMBER.getCode(), parts[0]);
                    if (parts.length > 1) {
                        params.put(Shift4ReqEnums.ADDRESS_STREET.getCode(), parts[1]);
                    }
                } else {
                    params.put(Shift4ReqEnums.ADDRESS_STREET.getCode(), address);
                }
            }
            if (StrUtil.isNotBlank(vo.getAddressInfo().getCity())) {
                params.put(Shift4ReqEnums.ADDRESS_CITY.getCode(), vo.getAddressInfo().getCity());
            }
            if (StrUtil.isNotBlank(vo.getAddressInfo().getState())) {
                params.put(Shift4ReqEnums.ADDRESS_STATE.getCode(), vo.getAddressInfo().getState());
            }
            if (StrUtil.isNotBlank(vo.getAddressInfo().getCountry())) {
                params.put(Shift4ReqEnums.ADDRESS_COUNTRY.getCode(), vo.getAddressInfo().getCountry());
            }
            if (StrUtil.isNotBlank(vo.getAddressInfo().getZip())) {
                params.put(Shift4ReqEnums.ADDRESS_ZIP.getCode(), vo.getAddressInfo().getZip());
            }
        }
        // 3ds信息
        boolean shop3DsOpen = Constants.TDS_ON.equals(shop.getIs3dsOn());
        if (shop3DsOpen) {
            params.put(Shift4ReqEnums.TDS_INITIATE.getCode(), Shift4TdsConstants.TDS_INITIATE_MERCHANT);

            // 3DS 挑战窗口大小
            String challengeWindowSize = vo.getDeviceDetail() != null
                    && StrUtil.isNotBlank(vo.getDeviceDetail().getChallengeWindowSize())
                            ? vo.getDeviceDetail().getChallengeWindowSize()
                            : Shift4TdsConstants.TDS_CHALLENGE_WINDOW_SIZE_DEFAULT;
            params.put(Shift4ReqEnums.TDS_CHALLENGE_WINDOW_SIZE.getCode(), challengeWindowSize);

            // 3DS 购买日期 (格式: yyyyMMddHHmmss)
            params.put(Shift4ReqEnums.TDS_PURCHASE_DATE.getCode(),
                    DateUtil.format(new Date(), "yyyyMMddHHmmss"));

            // 3DS 交易类型
            params.put(Shift4ReqEnums.TDS_TRANSTYPE.getCode(), Shift4TdsConstants.TDS_TRANSTYPE_GOODS_SERVICE);

            // 3DS 重定向URL
            if (vo.getDeviceDetail() != null && StrUtil.isNotBlank(vo.getDeviceDetail().getTdsRedirectUrl())) {
                params.put(Shift4ReqEnums.TDS_REDIRECT_URL.getCode(), vo.getDeviceDetail().getTdsRedirectUrl());
            }

            // 浏览器信息（从 BrowserInfoVo 获取，如果没有则使用默认值）
            BrowserInfoVo browserInfo = vo.getBrowserInfo();

            String browserAcceptHeader = browserInfo != null && StrUtil.isNotBlank(browserInfo.getAcceptHeader())
                    ? browserInfo.getAcceptHeader()
                    : Shift4TdsConstants.TDS_BROWSER_ACCEPT_HEADER_DEFAULT;
            params.put(Shift4ReqEnums.TDS_BROWSER_ACCEPT_HEADER.getCode(), browserAcceptHeader);

            String browserTz = browserInfo != null && StrUtil.isNotBlank(browserInfo.getTimeZone())
                    ? DateUtils.convertTimezoneToMinutes(browserInfo.getTimeZone(),
                            Shift4TdsConstants.TDS_BROWSER_TZ_DEFAULT)
                    : Shift4TdsConstants.TDS_BROWSER_TZ_DEFAULT;
            params.put(Shift4ReqEnums.TDS_BROWSER_TZ.getCode(), browserTz);

            String browserUserAgent = browserInfo != null && StrUtil.isNotBlank(browserInfo.getUserAgent())
                    ? browserInfo.getUserAgent()
                    : Shift4TdsConstants.TDS_BROWSER_USER_AGENT_DEFAULT;
            params.put(Shift4ReqEnums.BROWSER_USER_AGENT.getCode(), browserUserAgent);

            String browserLanguage = browserInfo != null && StrUtil.isNotBlank(browserInfo.getLanguage())
                    ? browserInfo.getLanguage()
                    : Shift4TdsConstants.TDS_BROWSER_LANGUAGE_DEFAULT;
            params.put(Shift4ReqEnums.BROWSER_LANGUAGE.getCode(), browserLanguage);

            String browserColorDepth = browserInfo != null && StrUtil.isNotBlank(browserInfo.getColorDepth())
                    ? browserInfo.getColorDepth()
                    : Shift4TdsConstants.TDS_BROWSER_COLOR_DEPTH_DEFAULT;
            params.put(Shift4ReqEnums.TDS_BROWSER_COLOR_DEPTH.getCode(), browserColorDepth);

            String browserScreenWidth = browserInfo != null && StrUtil.isNotBlank(browserInfo.getScreenWidth())
                    ? browserInfo.getScreenWidth()
                    : Shift4TdsConstants.TDS_BROWSER_SCREEN_WIDTH_DEFAULT;
            params.put(Shift4ReqEnums.TDS_BROWSER_SCREEN_WIDTH.getCode(), browserScreenWidth);

            String browserScreenHeight = browserInfo != null && StrUtil.isNotBlank(browserInfo.getScreenHeight())
                    ? browserInfo.getScreenHeight()
                    : Shift4TdsConstants.TDS_BROWSER_SCREEN_HEIGHT_DEFAULT;
            params.put(Shift4ReqEnums.TDS_BROWSER_SCREEN_HEIGHT.getCode(), browserScreenHeight);

            String browserJavaEnabled = browserInfo != null && StrUtil.isNotBlank(browserInfo.getJavaEnabled())
                    ? browserInfo.getJavaEnabled()
                    : Shift4TdsConstants.TDS_BROWSER_JAVA_ENABLED_DEFAULT;
            params.put(Shift4ReqEnums.TDS_BROWSER_JAVA_ENABLED.getCode(), browserJavaEnabled);
        }

        if (vo.getDeviceDetail() != null && StrUtil.isNotBlank(vo.getDeviceDetail().getIpAddress())) {
            params.put(Shift4ReqEnums.IP_ADDRESS.getCode(), vo.getDeviceDetail().getIpAddress());
        }

        // AFT (Account Funding Transaction) 参数
        // 直接从 cardInfo 和 addressInfo 中获取 AFT 相关参数
        if (vo.getCardInfo() != null || vo.getAddressInfo() != null) {
            // 获取收款人国家代码（用于后续条件判断）
            String recipientCountryCode = null;
            if (vo.getAddressInfo() != null && StrUtil.isNotBlank(vo.getAddressInfo().getCountry())) {
                recipientCountryCode = CountryCodeUtils.convertToIso3CountryCode(vo.getAddressInfo().getCountry());
            }

            // Recipient 信息 (j组) - 使用卡信息和地址信息
            if (vo.getCardInfo() != null) {
                // j2: 收款人账号/PAN - 使用卡号
                if (StrUtil.isNotBlank(vo.getCardInfo().getCardNumber())) {
                    params.put(Shift4ReqEnums.RECIPIENT_ACCOUNT_NUMBER.getCode(), vo.getCardInfo().getCardNumber());
                }

                // j5: 收款人名 - 使用卡信息中的名
                if (StrUtil.isNotBlank(vo.getCardInfo().getFirstName())) {
                    params.put(Shift4ReqEnums.RECIPIENT_FIRST_NAME.getCode(), vo.getCardInfo().getFirstName());
                }

                // j13: 收款人姓 - 使用卡信息中的姓
                if (StrUtil.isNotBlank(vo.getCardInfo().getLastName())) {
                    params.put(Shift4ReqEnums.RECIPIENT_SURNAME.getCode(), vo.getCardInfo().getLastName());
                }
            }

            if (vo.getAddressInfo() != null) {
                // j6, j7 — 仅当涉及 CA/US/COL/NIC 的 AFT 时强制要求
                if (recipientCountryCode != null && CountryCodeUtils.isRecipientAddressRequired(recipientCountryCode)) {
                    // j6: 收款人街道地址
                    if (StrUtil.isNotBlank(vo.getAddressInfo().getAddressLine1())) {
                        params.put(Shift4ReqEnums.RECIPIENT_STREET_ADDRESS.getCode(),
                                vo.getAddressInfo().getAddressLine1());
                    }

                    // j7: 收款人城市
                    if (StrUtil.isNotBlank(vo.getAddressInfo().getCity())) {
                        params.put(Shift4ReqEnums.RECIPIENT_CITY.getCode(), vo.getAddressInfo().getCity());
                    }

                    // j8: 收款人地区
                    if (StrUtil.isNotBlank(vo.getAddressInfo().getState())) {
                        params.put(Shift4ReqEnums.RECIPIENT_STATE.getCode(), vo.getAddressInfo().getState());
                    }

                }

                // j9: 收款人国家代码 - 使用 country 字段转换为 ISO 3166-1 alpha-3 格式
                if (recipientCountryCode != null) {
                    params.put(Shift4ReqEnums.RECIPIENT_COUNTRY_CODE.getCode(), recipientCountryCode);
                }
            }

            // Sender 信息 (s组) - 使用卡信息和地址信息
            if (vo.getCardInfo() != null) {
                // s10: 付款人名 - 使用卡信息中的名
                if (StrUtil.isNotBlank(vo.getCardInfo().getFirstName())) {
                    params.put(Shift4ReqEnums.SENDER_FIRST_NAME.getCode(), vo.getCardInfo().getFirstName());
                }

                // s11: 付款人姓 - 使用卡信息中的姓
                if (StrUtil.isNotBlank(vo.getCardInfo().getLastName())) {
                    params.put(Shift4ReqEnums.SENDER_SURNAME.getCode(), vo.getCardInfo().getLastName());
                }

                // s18: 资金来源 — 仅对美国的 AFT 强制（当 j9=US 时）
                if (recipientCountryCode != null && CountryCodeUtils.isUs(recipientCountryCode)) {
                    String sourceOfFunds = StrUtil.isNotBlank(vo.getCardInfo().getSourceOfFunds())
                            ? vo.getCardInfo().getSourceOfFunds()
                            : "02";
                    params.put(Shift4ReqEnums.SENDER_SOURCE_OF_FUNDS.getCode(), sourceOfFunds);
                }

                // s19: 发送人出生日期 - 使用卡信息中的值，否则生成随机值
                String dateOfBirth = StrUtil.isNotBlank(vo.getCardInfo().getDateOfBirth())
                        ? vo.getCardInfo().getDateOfBirth()
                        : generateRandomAdultBirthDate();
                params.put(Shift4ReqEnums.SENDER_DATE_OF_BIRTH.getCode(), dateOfBirth);
            }

            if (vo.getAddressInfo() != null) {
                // s12: 付款人街道地址 - 使用地址信息
                if (StrUtil.isNotBlank(vo.getAddressInfo().getAddressLine1())) {
                    params.put(Shift4ReqEnums.SENDER_STREET_ADDRESS.getCode(), vo.getAddressInfo().getAddressLine1());
                }

                // s13: 付款人城市 - 使用地址信息
                if (StrUtil.isNotBlank(vo.getAddressInfo().getCity())) {
                    params.put(Shift4ReqEnums.SENDER_CITY.getCode(), vo.getAddressInfo().getCity());
                }

                // s14: 发送人州代码 — 仅对美国和加拿大的 AFT 交易强制要求
                if (recipientCountryCode != null && CountryCodeUtils.isUsOrCanada(recipientCountryCode)) {
                    if (StrUtil.isNotBlank(vo.getAddressInfo().getState())) {
                        params.put(Shift4ReqEnums.SENDER_STATE_CODE.getCode(), vo.getAddressInfo().getState());
                    }
                }

                // s15: 付款人国家代码 - 转换为ISO 3166-1 alpha-3格式
                if (StrUtil.isNotBlank(vo.getAddressInfo().getCountry())) {
                    String senderCountryCode = CountryCodeUtils
                            .convertToIso3CountryCode(vo.getAddressInfo().getCountry());
                    params.put(Shift4ReqEnums.SENDER_COUNTRY_CODE.getCode(), senderCountryCode);
                }

                // s20: 发送人邮政编码 — 仅对美国和加拿大的 AFT 交易强制要求
                if (recipientCountryCode != null && CountryCodeUtils.isUsOrCanada(recipientCountryCode)) {
                    if (StrUtil.isNotBlank(vo.getAddressInfo().getZip())) {
                        params.put(Shift4ReqEnums.SENDER_POSTAL_CODE.getCode(), vo.getAddressInfo().getZip());
                    }
                }
            }

            // s17: 发送人参考号 — 仅对南非相关交易的 AFT 强制要求
            if (recipientCountryCode != null && CountryCodeUtils.isSouthAfrica(recipientCountryCode)) {
                params.put(Shift4ReqEnums.SENDER_REFERENCE_NUMBER.getCode(), config.getMerchantId());
            }
        }

        return params;
    }

    private String normalizeCardExpiryMonth(String month) {
        if (StrUtil.isBlank(month)) {
            return month;
        }
        String trimmed = month.trim();
        if (trimmed.length() == 1) {
            return "0" + trimmed;
        }
        return trimmed;
    }

    /**
     * 执行请求并记录日志
     * 当网络请求失败时，会自动调用 [101] 操作查询历史交易确认订单状态
     * 
     * @return Shift4ResVo 响应对象
     */
    private Shift4ResVo executeRequest(String logPrefix,
            Map<String, String> params,
            ApiType apiType,
            Order order,
            Long shopId,
            String cardNumber,
            String ipAddress) {
        Long orderId = order != null ? order.getId() : null;
        String requestId = params.get(Shift4ReqEnums.REQUEST_ID.getCode());

        // 构建请求体用于日志记录（使用JSON格式）
        Map<String, String> signedParams = new HashMap<>(params);
        String signature = Shift4SignUtils.calculateSignature(signedParams, config.getSignatureKey());
        signedParams.put(Shift4ReqEnums.SIGNATURE.getCode(), signature);
        String requestBody = Shift4SignUtils.toJson(signedParams);

        Map<String, String> responseParams;
        String responseBody;

        try {
            // 使用 Shift4HttpUtils 发送 HTTP 请求
            responseParams = Shift4HttpUtils.executeRequestRaw(
                    config.getUrl(),
                    params,
                    config.getSignatureKey(),
                    logPrefix + " - 订单ID: " + orderId);
            responseBody = Shift4SignUtils.toJson(responseParams);
        } catch (Exception e) {
            log.error("{}HTTP请求异常 - 订单ID: {}, requestId: {}", logPrefix, orderId, requestId, e);

            // 网络请求失败，尝试通过 [101] 查询历史交易确认订单是否已创建
            if (StrUtil.isNotBlank(requestId)) {
                log.info("{}尝试查询历史交易确认订单状态 - requestId: {}", logPrefix, requestId);
                Shift4TransactionQueryResult queryResult = retrievePastTransaction(requestId, null);

                if (queryResult.isSuccess() && queryResult.getRawResponse() != null
                        && isOriginalTransactionSuccess(queryResult.getRawResponse())) {
                    log.info("{}查询到历史交易记录，原始交易成功 - requestId: {}", logPrefix, requestId);

                    // 将下划线前缀的字段映射回正常字段名，供后续处理使用
                    responseParams = extractOriginalTransactionData(queryResult.getRawResponse());
                    responseBody = Shift4SignUtils.toJson(responseParams);

                    // 记录 API 日志（查询到的结果）
                    Long resolvedShopId = shopId != null ? shopId : (order != null ? order.getMerchantId() : null);
                    apiLogService.saveLog(
                            apiType,
                            ApiStatus.SUCC,
                            order != null ? order.getId() : null,
                            order != null ? order.getSsoOrderId() : null,
                            cardNumber,
                            requestBody,
                            responseBody + " (recovered via [101])",
                            resolvedShopId,
                            ipAddress);

                    return Shift4SignUtils.toResVo(responseParams);
                } else {
                    log.info("{}查询历史交易：查询成功={}, 原始交易成功={} - requestId: {}",
                            logPrefix, queryResult.isSuccess(),
                            queryResult.getRawResponse() != null
                                    && isOriginalTransactionSuccess(queryResult.getRawResponse()),
                            requestId);
                }
            }

            // 记录失败日志
            Long resolvedShopId = shopId != null ? shopId : (order != null ? order.getMerchantId() : null);
            apiLogService.saveLog(
                    apiType,
                    ApiStatus.ERR,
                    order != null ? order.getId() : null,
                    order != null ? order.getSsoOrderId() : null,
                    cardNumber,
                    requestBody,
                    "网络请求异常: " + e.getMessage(),
                    resolvedShopId,
                    ipAddress);

            throw new BusinessException("Shift4请求失败: " + e.getMessage());
        }

        // 构建响应体用于日志记录（使用JSON格式）
        // 记录 API 日志
        Long resolvedShopId = shopId != null ? shopId : (order != null ? order.getMerchantId() : null);
        apiLogService.saveLog(
                apiType,
                isSuccess(responseParams) ? ApiStatus.SUCC : ApiStatus.FAIL,
                order != null ? order.getId() : null,
                order != null ? order.getSsoOrderId() : null,
                cardNumber,
                requestBody,
                responseBody,
                resolvedShopId,
                ipAddress);

        // 验证响应签名
        if (responseParams.containsKey(Shift4ReqEnums.SIGNATURE.getCode()) &&
                !Shift4SignUtils.verifySignature(responseParams, config.getSignatureKey())) {
            log.warn("{}响应签名验证失败 - 订单ID: {}", logPrefix, orderId);
        }

        return Shift4SignUtils.toResVo(responseParams);
    }

    /**
     * 构建转介操作参数
     */
    private Map<String, String> buildReferralParams(Order order,
            String operationCode,
            BigDecimal amount,
            Shift4ReferralOptions options) {
        Map<String, String> params = new HashMap<>();
        params.put(Shift4ReqEnums.MERCHANT_ID.getCode(), config.getMerchantId());
        params.put(Shift4ReqEnums.OPERATION_CODE.getCode(), operationCode);

        String requestId = options != null && StrUtil.isNotBlank(options.getRequestId())
                ? options.getRequestId()
                : generateRequestId(order);
        params.put(Shift4ReqEnums.REQUEST_ID.getCode(), requestId);

        String currencyCode = resolveCurrencyCode(order, options != null ? options.getCurrency() : null);
        params.put(Shift4ReqEnums.CURRENCY.getCode(), currencyCode);

        BigDecimal resolvedAmount = amount != null
                ? amount
                : (options != null ? options.getAmount() : null);
        if (resolvedAmount != null) {
            params.put(Shift4ReqEnums.AMOUNT.getCode(), toMinorUnits(resolvedAmount));
        }

        if (options != null && options.getPaymentSourceType() != null) {
            params.put(Shift4ReqEnums.PAYMENT_SOURCE_TYPE.getCode(), options.getPaymentSourceType().toString());
        }

        String responseId = options != null ? options.getResponseId() : null;
        if (StrUtil.isBlank(responseId) && order != null) {
            responseId = order.getOrderUuid();
        }
        if (StrUtil.isBlank(responseId)) {
            throw new BusinessException("缺少原交易响应ID(g2)");
        }
        params.put(Shift4ReqEnums.RESPONSE_ID.getCode(), responseId);

        if (options != null) {
            if (StrUtil.isNotBlank(options.getAuthorizationCode())) {
                params.put(Shift4ReqEnums.AUTHORIZATION_CODE.getCode(), options.getAuthorizationCode());
            }
            if (StrUtil.isNotBlank(options.getOriginalRequestId())) {
                params.put(Shift4ReqEnums.ORIGINAL_REQUEST_ID.getCode(), options.getOriginalRequestId());
            }
            if (StrUtil.isNotBlank(options.getInitialTransactionId())) {
                params.put(Shift4ReqEnums.INITIAL_TRANSACTION_ID.getCode(), options.getInitialTransactionId());
            }
            if (StrUtil.isNotBlank(options.getMerchantReferenceNumber())) {
                params.put(Shift4ReqEnums.MERCHANT_REFERENCE_NUMBER.getCode(), options.getMerchantReferenceNumber());
            }
            if (StrUtil.isNotBlank(options.getSellerInformation())) {
                params.put(Shift4ReqEnums.SELLER_INFORMATION.getCode(), options.getSellerInformation());
            }
            if (StrUtil.isNotBlank(options.getDynamicDescriptor())) {
                params.put(Shift4ReqEnums.DYNAMIC_DESCRIPTOR.getCode(), options.getDynamicDescriptor());
            }
            if (StrUtil.isNotBlank(options.getEchoField())) {
                params.put(Shift4ReqEnums.ECHO_FIELD.getCode(), options.getEchoField());
            }
            if (StrUtil.isNotBlank(options.getRoutingProcessorCode())) {
                params.put(Shift4ReqEnums.ROUTING_PROCESSOR_CODE.getCode(), options.getRoutingProcessorCode());
            }
            if (StrUtil.isNotBlank(options.getRoutingProcessorMid())) {
                params.put(Shift4ReqEnums.ROUTING_PROCESSOR_MID.getCode(), options.getRoutingProcessorMid());
            }
            if (options.getRoutingSequence() != null) {
                params.put(Shift4ReqEnums.ROUTING_SEQUENCE.getCode(), options.getRoutingSequence().toString());
            }
            if (options.getSkipFraudCheck() != null) {
                params.put(Shift4ReqEnums.SKIP_FRAUD_CHECK.getCode(), options.getSkipFraudCheck() ? "1" : "0");
            }
            if (options.getFraudThreshold() != null) {
                params.put(Shift4ReqEnums.FRAUD_THRESHOLD.getCode(), options.getFraudThreshold().toString());
            }
            if (StrUtil.isNotBlank(options.getCryptoCurrencyType())) {
                params.put(Shift4ReqEnums.CRYPTO_CURRENCY_TYPE.getCode(), options.getCryptoCurrencyType());
            }
            if (StrUtil.isNotBlank(options.getCardExpiryMonth())) {
                params.put(Shift4ReqEnums.CARD_EXPIRY_MONTH.getCode(), options.getCardExpiryMonth());
            }
            if (StrUtil.isNotBlank(options.getCardExpiryYear())) {
                params.put(Shift4ReqEnums.CARD_EXPIRY_YEAR.getCode(), options.getCardExpiryYear());
            }
        }

        return params;
    }

    private Shift4ReferralResult processReferralOperation(
            String operationCode,
            Order order,
            BigDecimal amount,
            Shift4ReferralOptions options) {
        if (order == null) {
            throw new BusinessException("缺少订单信息，无法执行S4转介操作");
        }
        Map<String, String> params = buildReferralParams(order, operationCode, amount, options);
        Shift4ResVo resVo = executeRequest(
                "S4转介退款",
                params,
                ApiType.PAYMENT,
                order,
                order.getMerchantId(),
                null,
                null);
        return buildReferralResult(resVo);
    }

    /**
     * 判断响应是否成功
     */
    private boolean isSuccess(Map<String, String> responseParams) {
        // z2是响应代码，0表示成功
        String z2 = responseParams.get(Shift4ReqEnums.RESPONSE_CODE.getCode());
        return "0".equals(z2);
    }

    /**
     * 判断 [101] 查询响应中的原始交易是否成功
     * 原始交易的响应码存储在 _z2 字段中
     */
    private boolean isOriginalTransactionSuccess(Map<String, String> queryResponse) {
        String originalZ2 = queryResponse.get("_" + Shift4ReqEnums.RESPONSE_CODE.getCode());
        return "0".equals(originalZ2);
    }

    /**
     * 从 [101] 查询响应中提取原始交易数据
     * 将下划线前缀的字段映射回正常字段名，供后续处理（如 buildPayOrderResult）使用
     * 例如：_z1 -> z1, _z2 -> z2, _z39 -> z39
     */
    private Map<String, String> extractOriginalTransactionData(Map<String, String> queryResponse) {
        Map<String, String> originalData = new HashMap<>();
        for (Map.Entry<String, String> entry : queryResponse.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("_")) {
                // 去掉下划线前缀，映射回正常字段名
                String normalizedKey = key.substring(1);
                originalData.put(normalizedKey, entry.getValue());
            }
        }
        log.debug("从[101]响应中提取原始交易数据: {}", Shift4SignUtils.toJson(originalData));
        return originalData;
    }

    /**
     * 构建支付结果
     */
    private PayOrderResult buildPayOrderResult(Shift4ResVo resVo, Order order) {
        PayOrderResult result = new PayOrderResult();

        String z2 = resVo.getResponseCode(); // 响应代码
        String z3 = resVo.getResponseMessage(); // 响应消息
        String z39 = resVo.getProcessorTransactionId(); // 处理器交易ID
        String z1 = resVo.getTransactionId(); // Shift4交易ID

        // 检查是否需要3DS认证 (z2=6或06 表示待认证)
        String tdsStatus = resVo.getTdsStatus(); // 3DS状态
        String acsUrl = resVo.getTdsAcsUrl();
        String tdsMethodUrl = resVo.getTdsMethodUrl(); // 3DS Method URL (DFP流程)
        String tdsTrxId = resVo.getTdsTrxId(); // 3DS交易ID
        boolean is3dsPending = "6".equals(z2) || "06".equals(z2);

        // 如果响应码表示待认证，检查是DFP流程还是Challenge流程
        if (is3dsPending) {
            result.setSucc(true);
            result.setTransactionId(z1);
            result.setTds(true);
            result.setOrderStatus(OrderStatus.TDS_PENDING);

            // 检查是否是3DS Method (DFP)流程
            if (StrUtil.isNotBlank(tdsMethodUrl)) {
                // DFP流程：返回数据给前端，由前端在iframe中执行
                String notificationUrl = config.getTdsMethodNotificationUrl();
                if (StrUtil.isBlank(notificationUrl)) {
                    throw new Shift4Exception("未配置3DS Method通知URL (tdsMethodNotificationUrl)，请在系统配置中设置");
                }

                // 构建threeDSMethodData JSON并Base64编码
                String methodDataJson = String.format(
                        "{\"threeDSMethodNotificationURL\":\"%s\",\"threeDSServerTransID\":\"%s\"}",
                        notificationUrl, tdsTrxId);
                String encodedData = java.util.Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(methodDataJson.getBytes(java.nio.charset.StandardCharsets.UTF_8));

                TdsDto tdsDto = TdsDto.forDfp(tdsMethodUrl, encodedData, tdsTrxId, z1);
                result.setTdsDto(tdsDto);
                // 将3ds_trxid存储到relatedTransactionId，用于后续3DS Completion
                result.setRelatedTransactionId(tdsTrxId);

                log.info("S4需要3DS Method(DFP)认证 - z2: {}, 3DS Method URL: {}, 3DS交易ID: {}",
                        z2, tdsMethodUrl, tdsTrxId);
                return result;
            }

            // Challenge流程
            if (StrUtil.isNotBlank(acsUrl)) {
                TdsDto tdsDto = new TdsDto(acsUrl, null);
                result.setTdsDto(tdsDto);
                log.info("S4需要3DS认证(Challenge) - z2: {}, ACS URL: {}, 3DS状态: {}", z2, acsUrl, tdsStatus);
            }
            return result;
        }

        if ("0".equals(z2)) {
            // 成功
            result.setSucc(true);
            result.setTransactionId(z1);
            result.setOrderStatus(OrderStatus.COMPLETED);
        } else {
            // 失败
            result.setSucc(false);
            result.setMessage(z3 != null ? z3 : "支付失败，响应代码: " + z2);
            result.setOrderStatus(OrderStatus.DEPOSIT_DECLINED);

            // 即使失败，也保存交易ID
            if (StrUtil.isNotBlank(z1)) {
                result.setSuperOrderId(z1);
            }
            if (StrUtil.isNotBlank(z39)) {
                result.setTransactionId(z39);
            }
        }

        return result;
    }

    private Shift4ReferralResult buildReferralResult(Shift4ResVo resVo) {
        Shift4ReferralResult result = new Shift4ReferralResult();
        String z2 = resVo.getResponseCode();
        result.setResponseCode(z2);
        result.setMessage(resVo.getResponseMessage() != null ? resVo.getResponseMessage() : "");
        result.setShift4TransactionId(resVo.getTransactionId());
        result.setProcessorTransactionId(resVo.getProcessorTransactionId());
        result.setUpdatedAmount(resVo.getUpdatedAmount());
        result.setSuccess("0".equals(z2));
        result.setRawResponse(null);
        return result;
    }

    /**
     * 保存或更新卡信息
     * 
     * @param order     订单信息
     * @param vo        支付请求参数
     * @param resVo     Shift4 响应
     * @param ipAddress IP 地址
     * @param token     Shift4 Token（可为空，仅 createTokenWithSale 操作会传入）
     */
    private Card saveOrUpdateCardInfo(Order order, PayOrderVo vo, Shift4ResVo resVo, String ipAddress, String token) {
        CardInfoVo cardInfo = vo.getCardInfo();
        String cardNumber = cardInfo.getCardNumber();

        // 从响应中获取卡品牌代码 (b2)
        String cardBrandCode = resVo.getCardType();
        String cardBrand = resolveCardBrand(cardBrandCode);

        // 查询是否已存在此卡
        List<Card> existingCards = cardService.getCardsByCardNumber(cardNumber, SHIFT4_ACQUIRER_ID);

        // 获取用户信息
        Customer customer = customerService.getById(order.getCustomerId());
        Long userId = customer != null ? customer.getUserId() : null;

        // 获取邮箱：优先使用支付请求中的邮箱，如果为空则从 User 表获取
        String email = cardInfo.getEmail();
        if (StrUtil.isBlank(email) && userId != null) {
            User user = userService.getById(userId);
            if (user != null) {
                email = user.getEmail();
                log.info("S4使用User表邮箱 - 订单ID: {}, 邮箱: {}", order.getId(), email);
            }
        }

        // 检查是否存在重复的 Token
        boolean duplicateTokenExists = StrUtil.isNotBlank(token)
                && existingCards.stream().anyMatch(db -> Objects.equals(token, db.getToken()));

        // 只有当不存在重复 Token 且卡数量未达阈值时才创建新卡
        if (!duplicateTokenExists && (existingCards.isEmpty()
                || existingCards.size() < configService.getShift4MaxMappingCount())) {
            // 创建新卡
            Card card = Card.init(cardNumber);
            card.setUserId(userId);
            card.setAcquirerId(SHIFT4_ACQUIRER_ID);
            card.setCardholderName(cardInfo.getFirstName() + " " + cardInfo.getLastName());
            card.setCardholderEmailAddress(email);
            card.setExpiryMonth(Long.parseLong(cardInfo.getExpiryMonth()));
            card.setExpiryYear(Long.parseLong(cardInfo.getExpiryYear()));
            card.setBin(cardNumber.substring(0, 6));
            card.setScheme(ipAddress); // 存储最后一次成功IP
            card.setCardBrand(cardBrand); // 设置卡品牌
            card.setCardType("UNKNOWN"); // 返回体没有字段标识卡是信用卡还是借记卡，设置为未知
            card.setRemark("Shift4");
            // 保存 Token（如果有）
            if (StrUtil.isNotBlank(token)) {
                card.setToken(token);
            }

            cardService.save(card);
            log.info("S4保存新卡信息成功 - 订单ID: {}, 卡号: {}****{}, 卡品牌: {}, Token状态: {}",
                    order.getId(), cardNumber.substring(0, 6), cardNumber.substring(cardNumber.length() - 4),
                    cardBrand, StrUtil.isNotBlank(token) ? "已保存" : "无");
            return card;
        } else if (!existingCards.isEmpty()) {
            // 更新现有卡
            Card existingCard = existingCards.get(0);
            boolean needUpdate = false;

            // 更新 Token（如果有新 Token 且与现有不同）
            if (StrUtil.isNotBlank(token) && !Objects.equals(existingCard.getToken(), token)) {
                existingCard.setToken(token);
                needUpdate = true;
                log.info("S4更新卡Token - 订单ID: {}, 卡ID: {}", order.getId(), existingCard.getId());
            }

            // 更新最后成功IP
            if (!Objects.equals(existingCard.getScheme(), ipAddress)) {
                existingCard.setScheme(ipAddress);
                needUpdate = true;
            }

            // 如果用户ID为空，更新用户ID
            if (existingCard.getUserId() == null && userId != null) {
                existingCard.setUserId(userId);
                needUpdate = true;
            }

            // 如果卡品牌为空或不同，更新卡品牌
            if (StrUtil.isBlank(existingCard.getCardBrand())
                    || !Objects.equals(existingCard.getCardBrand(), cardBrand)) {
                existingCard.setCardBrand(cardBrand);
                needUpdate = true;
            }

            // 如果邮箱为空或不同，更新邮箱
            if (StrUtil.isNotBlank(email) && !Objects.equals(existingCard.getCardholderEmailAddress(), email)) {
                existingCard.setCardholderEmailAddress(email);
                needUpdate = true;
            }

            if (needUpdate) {
                cardService.updateById(existingCard);
                log.info("S4更新卡信息成功 - 订单ID: {}, 卡ID: {}, 卡品牌: {}", order.getId(), existingCard.getId(), cardBrand);
            }
            return existingCard;
        }

        // 达到卡数量上限且无重复 Token，返回 null
        log.info("S4卡数量已达上限，不创建新卡 - 订单ID: {}, 当前数量: {}", order.getId(), existingCards.size());
        return null;
    }

    /**
     * 从3DS回调中保存Token
     * 当code 23触发3DS认证时，token在3DS完成后的回调中返回
     * 此方法从订单的payInfo中提取卡信息，并保存token到数据库
     *
     * @param order         订单
     * @param token         token字符串 (g1)
     * @param cardBrandCode 卡品牌代码 (b2)
     * @param ipAddress     客户IP地址
     */
    public void saveTokenFrom3DSCallback(Order order, String token, String cardBrandCode, String ipAddress) {
        if (order == null || StrUtil.isBlank(token)) {
            log.warn("S4 3DS回调保存Token - 参数无效: orderId={}",
                    order != null ? order.getId() : null);
            return;
        }

        try {
            // 从订单的payInfo中解析卡信息
            String payInfoJson = order.getPayInfo();
            if (StrUtil.isBlank(payInfoJson)) {
                log.warn("S4 3DS回调保存Token - 订单payInfo为空: orderId={}", order.getId());
                return;
            }

            JSONObject payInfo = JSONUtil.parseObj(payInfoJson);
            JSONObject cardInfoJson = payInfo.getJSONObject("cardInfo");
            if (cardInfoJson == null) {
                log.warn("S4 3DS回调保存Token - payInfo中无cardInfo: orderId={}", order.getId());
                return;
            }

            String cardFinger = cardInfoJson.getStr("cardFinger");
            String cardNumber = cardInfoJson.getStr("cardNumber");
            String cardBrand = resolveCardBrand(cardBrandCode);

            // 必须有 cardFinger 才能正确识别卡（掩码 cardNumber 可能对应多张不同的卡）
            if (StrUtil.isBlank(cardFinger)) {
                log.warn("S4 3DS回调保存Token - cardFinger为空，无法识别卡: orderId={}", order.getId());
                return;
            }

            // 通过 cardFinger 查找现有卡记录
            List<Card> existingCards = cardService.getCardsByCardFinger(cardFinger, SHIFT4_ACQUIRER_ID);

            // 检查是否有重复的 token
            boolean duplicateTokenExists = existingCards.stream()
                    .anyMatch(c -> Objects.equals(token, c.getToken()));
            if (duplicateTokenExists) {
                log.info("S4 3DS回调保存Token - Token已存在，跳过: orderId={}", order.getId());
                return;
            }

            if (!existingCards.isEmpty()) {
                // 只更新 token 为空的卡记录
                Card cardWithEmptyToken = existingCards.stream()
                        .filter(c -> StrUtil.isBlank(c.getToken()))
                        .findFirst()
                        .orElse(null);

                if (cardWithEmptyToken != null) {
                    cardWithEmptyToken.setToken(token);
                    if (StrUtil.isNotBlank(ipAddress)) {
                        cardWithEmptyToken.setScheme(ipAddress);
                    }
                    if (StrUtil.isNotBlank(cardBrand)) {
                        cardWithEmptyToken.setCardBrand(cardBrand);
                    }
                    cardService.updateById(cardWithEmptyToken);
                    log.info("S4 3DS回调更新Token成功 - 订单ID: {}, 卡ID: {}",
                            order.getId(), cardWithEmptyToken.getId());
                } else {
                    // 所有卡都已有 token，跳过更新
                    log.info("S4 3DS回调保存Token - 所有卡记录都已有Token，跳过更新: orderId={}", order.getId());
                }
            } else {

                // 通过 cardFinger 查询该卡的所有有效 token 记录数量，检查是否达到阈值
                long tokenCount = cardService.getCardsByCardFinger(cardFinger, SHIFT4_ACQUIRER_ID)
                        .stream()
                        .filter(c -> StrUtil.isNotBlank(c.getToken()))
                        .count();
                int maxCount = configService.getShift4MaxMappingCount();
                if (tokenCount >= maxCount) {
                    log.info("S4 3DS回调保存Token - 该卡Token数量已达阈值({}/{}), 不创建新卡: orderId={}, cardFinger={}",
                            tokenCount, maxCount, order.getId(), cardFinger);
                    return;
                }

                // 创建新卡记录
                Customer customer = customerService.getById(order.getCustomerId());
                Long userId = customer != null ? customer.getUserId() : null;

                String email = cardInfoJson.getStr("email");
                if (StrUtil.isBlank(email) && userId != null) {
                    User user = userService.getById(userId);
                    if (user != null) {
                        email = user.getEmail();
                    }
                }

                // 创建新卡（使用掩码格式的卡号，因为 cardFinger 已经保存了）
                Card card = new Card();
                card.setCardFinger(cardFinger);
                card.setUserId(userId);
                card.setAcquirerId(SHIFT4_ACQUIRER_ID);
                String firstName = cardInfoJson.getStr("firstName");
                String lastName = cardInfoJson.getStr("lastName");
                card.setCardholderName((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : ""));
                card.setCardholderEmailAddress(email);
                String expiryMonth = cardInfoJson.getStr("expiryMonth");
                String expiryYear = cardInfoJson.getStr("expiryYear");
                if (StrUtil.isNotBlank(expiryMonth)) {
                    card.setExpiryMonth(Long.parseLong(expiryMonth));
                }
                if (StrUtil.isNotBlank(expiryYear)) {
                    card.setExpiryYear(Long.parseLong(expiryYear));
                }

                // 从掩码卡号提取 bin（格式: 529991****0015）
                String bin = null;
                if (StrUtil.isNotBlank(cardNumber)) {
                    // 掩码格式: 前6位 + **** + 后4位
                    if (cardNumber.contains("*")) {
                        String[] parts = cardNumber.split("\\*+");
                        if (parts.length >= 1 && parts[0].length() >= 6) {
                            bin = parts[0].substring(0, 6);
                        }
                    } else if (cardNumber.length() >= 16) {
                        bin = cardNumber.substring(0, 6);
                    }
                }

                card.setCardNumber(cardNumber); // 保存掩码格式的卡号
                card.setBin(bin);
                card.setScheme(ipAddress);
                card.setCardBrand(cardBrand);
                card.setCardType("UNKNOWN");
                card.setRemark("Shift4-3DS");
                card.setToken(token);
                card.setCreatedAt(new Date());

                cardService.save(card);
                log.info("S4 3DS回调保存新卡成功 - 订单ID: {}", order.getId());
            }
        } catch (Exception e) {
            log.error("S4 3DS回调保存Token异常 - 订单ID: {}", order.getId(), e);
        }
    }

    /**
     * 解析卡品牌代码
     * b2 字段的值映射到卡品牌名称
     */
    private String resolveCardBrand(String cardBrandCode) {
        if (StrUtil.isBlank(cardBrandCode)) {
            return "Unknown";
        }
        switch (cardBrandCode) {
            case "1":
                return "Visa";
            case "2":
                return "Mastercard";
            case "3":
                return "American Express";
            case "4":
                return "Isracard";
            case "9":
                return "Maestro";
            case "10":
                return "JCB";
            case "12":
                return "Discover";
            case "13":
                return "Diners";
            default:
                return "Unknown";
        }
    }

    private String resolveCurrencyCode(Order order, String overrideCode) {
        if (StrUtil.isNotBlank(overrideCode)) {
            return overrideCode;
        }
        if (order == null || order.getCurrencyId() == null) {
            throw new BusinessException("缺少订单币种信息");
        }
        Currency currency = currencyService.getById(order.getCurrencyId());
        if (currency == null) {
            throw new BusinessException("Currency not found for order: " + order.getId());
        }
        return currency.getCode();
    }

    private String toMinorUnits(BigDecimal amount) {
        BigDecimal scaled = amount.setScale(2, RoundingMode.HALF_UP);
        return scaled.multiply(BigDecimal.valueOf(100)).toBigInteger().toString();
    }

    private String generateRequestId(Order order) {
        if (order != null && order.getId() != null) {
            return order.getId() + "-r" + System.currentTimeMillis();
        }
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 构建交易查询结果
     */
    private Shift4TransactionQueryResult buildTransactionQueryResult(Shift4ResVo resVo) {
        Shift4TransactionQueryResult result = new Shift4TransactionQueryResult();

        String z2 = resVo.getResponseCode();
        result.setResponseCode(z2);
        result.setSuccess("0".equals(z2));
        result.setMessage(resVo.getResponseMessage() != null ? resVo.getResponseMessage() : "");

        if (result.isSuccess()) {
            result.setShift4TransactionId(resVo.getTransactionId());
            result.setProcessorTransactionId(resVo.getProcessorTransactionId());
        }

        result.setRawResponse(null);
        return result;
    }

    /**
     * 验证 AFT (Account Funding Transaction) 参数
     * 根据 Shift4 AFT 规范验证必填和条件必填参数
     * 必填参数（所有AFT交易）：j2, j5, j13, s10, s11, s12, s13, s15, j9, s19
     * 条件必填参数：
     * - s14: 仅对美国和加拿大的 AFT 交易强制要求
     * - s17: 仅对南非相关交易的 AFT 强制要求（系统自动处理）
     * - s18: 对 AFT 有条件要求，并且仅在美国的 AFT 中强制（当 j9=USA 时）
     * - j6, j7: 仅当收款人=付款人或涉及 CA/US/COL/NIC 的 AFT 时强制要求
     * 
     * @param vo 支付订单请求对象
     * @throws BusinessException 当必要参数缺失时抛出异常
     */
    private void validateAftParameters(PayOrderVo vo) {
        // 基本验证：检查卡信息是否完整
        if (vo.getCardInfo() == null) {
            throw new BusinessException("AFT交易缺少卡信息 (cardInfo)");
        }

        CardInfoVo cardInfo = vo.getCardInfo();
        AddressInfoVo addressInfo = vo.getAddressInfo();

        // 检查地址信息是否存在（AFT交易必需）
        if (addressInfo == null) {
            throw new BusinessException("AFT交易缺少地址信息 (addressInfo)");
        }

        // ========== 必填参数验证（所有AFT交易都需要） ==========

        // j2: 收款人账号/PAN (cardNumber) - 必填
        if (StrUtil.isBlank(cardInfo.getCardNumber())) {
            throw new BusinessException("AFT交易缺少必填参数：银行卡号 (cardInfo.cardNumber)");
        }

        // j5: 收款人名 (firstName) - 必填
        if (StrUtil.isBlank(cardInfo.getFirstName())) {
            throw new BusinessException("AFT交易缺少必填参数：持卡人名 (cardInfo.firstName)");
        }

        // j13: 收款人姓 (lastName) - 必填
        if (StrUtil.isBlank(cardInfo.getLastName())) {
            throw new BusinessException("AFT交易缺少必填参数：持卡人姓 (cardInfo.lastName)");
        }

        // s10: 付款人名 (firstName) - 必填
        // 注意：这里复用 cardInfo.firstName，在实际业务中付款人和收款人可能是同一人
        if (StrUtil.isBlank(cardInfo.getFirstName())) {
            throw new BusinessException("AFT交易缺少必填参数：付款人名 (cardInfo.firstName)");
        }

        // s11: 付款人姓 (lastName) - 必填
        if (StrUtil.isBlank(cardInfo.getLastName())) {
            throw new BusinessException("AFT交易缺少必填参数：付款人姓 (cardInfo.lastName)");
        }

        // s12: 付款人街道地址 - 必填
        if (StrUtil.isBlank(addressInfo.getAddressLine1())) {
            throw new BusinessException("AFT交易缺少必填参数：付款人街道地址 (addressInfo.addressLine1)");
        }

        // s13: 付款人城市 - 必填
        if (StrUtil.isBlank(addressInfo.getCity())) {
            throw new BusinessException("AFT交易缺少必填参数：付款人城市 (addressInfo.city)");
        }

        // s15: 付款人国家代码 - 必填
        if (StrUtil.isBlank(addressInfo.getCountry())) {
            throw new BusinessException("AFT交易缺少必填参数：付款人国家代码 (addressInfo.country)");
        }

        // 转换国家代码为3位格式
        String recipientCountryCode = CountryCodeUtils.convertToIso3CountryCode(addressInfo.getCountry());

        // j9: 收款人国家代码 - 必填（复用 addressInfo.country）

        // s19: 发送人出生日期 - 必填（如果用户未提供，系统会自动生成）
        if (StrUtil.isNotBlank(cardInfo.getDateOfBirth())) {
            validateAndParseDateOfBirth(cardInfo.getDateOfBirth());
        }

        // ========== 条件必填参数验证 ==========

        // s14: 发送人州代码 - 仅对美国和加拿大的 AFT 交易强制要求
        if (CountryCodeUtils.isUsOrCanada(recipientCountryCode)) {
            if (StrUtil.isBlank(addressInfo.getState())) {
                throw new BusinessException("AFT交易缺少条件必填参数：美国/加拿大交易需要州代码 (addressInfo.state)");
            }
        }

        // s20: 发送人邮政编码 - 仅对美国和加拿大的 AFT 交易强制要求
        if (CountryCodeUtils.isUsOrCanada(recipientCountryCode)) {
            if (StrUtil.isBlank(addressInfo.getZip())) {
                throw new BusinessException("AFT交易缺少条件必填参数：美国/加拿大交易需要邮政编码 (addressInfo.zip)");
            }
        }

        // s17: 发送人参考号 - 仅对南非相关交易的 AFT 强制要求
        // 注意：系统会自动使用商户号，这里不需要用户提供，无需验证

        // s18: 资金来源 - 对 AFT 有条件要求，并且仅在美国的 AFT 中强制（当 j9=USA 时）
        if ("USA".equals(recipientCountryCode)) {
            if (StrUtil.isBlank(cardInfo.getSourceOfFunds())) {
                // 美国交易如果用户未提供，系统会使用默认值"02"，这里给出提示
                log.info("美国AFT交易未提供资金来源，将使用默认值'02'(借记卡)");
            } else {
                // 验证格式：01-信用卡,02-借记卡,03-预付卡,04-现金,05-借记/存款账户,06-信用账户,25-移动货币账户
                if (!cardInfo.getSourceOfFunds().matches("^(01|02|03|04|05|06|25)$")) {
                    throw new BusinessException(
                            "AFT交易参数错误：资金来源 (cardInfo.sourceOfFunds) 值无效，支持的值：01-信用卡,02-借记卡,03-预付卡,04-现金,05-借记/存款账户,06-信用账户,25-移动货币账户");
                }
            }
        }

        // j6, j7: 收款人街道地址和城市 - 仅当收款人=付款人或涉及 CA/US/COL/NIC 的 AFT 时强制要求
        if (CountryCodeUtils.isRecipientAddressRequired(recipientCountryCode)) {
            // j6: 收款人街道地址（复用 addressInfo.addressLine1）
            if (StrUtil.isBlank(addressInfo.getAddressLine1())) {
                throw new BusinessException("AFT交易缺少条件必填参数：CA/US/COL/NIC交易需要收款人街道地址 (addressInfo.addressLine1)");
            }

            // j7: 收款人城市（复用 addressInfo.city）
            if (StrUtil.isBlank(addressInfo.getCity())) {
                throw new BusinessException("AFT交易缺少条件必填参数：CA/US/COL/NIC交易需要收款人城市 (addressInfo.city)");
            }
            // j8: 收款人州代码（复用 addressInfo.state）
            if (StrUtil.isBlank(addressInfo.getState())) {
                throw new BusinessException("AFT交易缺少条件必填参数：CA/US/COL/NIC交易需要收款人州代码 (addressInfo.state)");
            }
        }

        log.info("AFT参数验证通过 - 国家: {}, 条件验证: US/CA州代码={}, 美国资金来源={}, 收款人地址={}",
                recipientCountryCode,
                CountryCodeUtils.isUsOrCanada(recipientCountryCode),
                "USA".equals(recipientCountryCode),
                CountryCodeUtils.isRecipientAddressRequired(recipientCountryCode));
    }

    /**
     * 生成随机成年人生日日期
     * 格式：MMDDYYYY
     * 年龄范围：18-65岁
     * 
     * @return 格式化的生日字符串
     */
    private String generateRandomAdultBirthDate() {
        // 生成18-65岁之间的随机年龄
        int minAge = 18;
        int maxAge = 65;
        int age = minAge + (int) (Math.random() * (maxAge - minAge + 1));

        // 计算出生年份
        int currentYear = java.time.LocalDate.now().getYear();
        int birthYear = currentYear - age;

        // 生成随机月份（1-12）
        int month = 1 + (int) (Math.random() * 12);

        // 生成随机日期（1-28，避免月份天数问题）
        int day = 1 + (int) (Math.random() * 28);

        // 格式化为 MMDDYYYY
        return String.format("%02d%02d%d", month, day, birthYear);
    }

    /**
     * 验证并解析出生日期
     * 格式：MMDDYYYY
     *
     * @param dateOfBirth 出生日期字符串
     * @throws BusinessException 当格式或值无效时抛出异常
     */
    private void validateAndParseDateOfBirth(String dateOfBirth) {
        // 验证格式：MMDDYYYY
        if (!dateOfBirth.matches("^\\d{8}$")) {
            throw new BusinessException("AFT交易参数格式错误：出生日期 (cardInfo.dateOfBirth) 必须是MMDDYYYY格式，如：01151990");
        }
        // 验证月份范围（01-12）
        String monthStr = dateOfBirth.substring(0, 2);
        int month = Integer.parseInt(monthStr);
        if (month < 1 || month > 12) {
            throw new BusinessException("AFT交易参数错误：出生日期月份无效 (cardInfo.dateOfBirth)，月份必须在01-12之间");
        }
        // 验证日期范围（01-31）
        String dayStr = dateOfBirth.substring(2, 4);
        int day = Integer.parseInt(dayStr);
        if (day < 1 || day > 31) {
            throw new BusinessException("AFT交易参数错误：出生日期日期无效 (cardInfo.dateOfBirth)，日期必须在01-31之间");
        }
    }
}
