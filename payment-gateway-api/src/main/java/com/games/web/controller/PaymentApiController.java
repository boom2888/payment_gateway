package com.games.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.games.api.PaymentInService;
import com.games.api.bean.PayOrderResult;
import com.games.api.dto.*;
import com.games.api.enums.OrderStatus;
import com.games.api.vo.*;
import com.games.common.annotation.LogApi;
import com.games.common.core.domain.R;
import com.games.common.exception.NuveiException;
import com.games.common.utils.SignUtil;
import com.games.framework.web.service.TokenService;
import com.games.nuvei.NuveiApiService;
import com.games.nuvei.QuoteService;
import com.games.nuvei.vo.OpenOrderVo;
import com.games.nuvei.vo.QuoteResult;
import com.games.payment.domain.Customer;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.domain.User;
import com.games.payment.service.BackService;
import com.games.payment.service.ICustomerService;
import com.games.payment.service.IOrderService;
import com.games.payment.service.IUserService;
import com.games.shift4.Shift4CallbackService;
import com.games.shift4.Shift4Config;
import com.games.shift4.Shift4ApiService;
import com.games.shift4.utils.Shift4HttpUtils;
import com.games.shift4.utils.Shift4SignUtils;
import com.games.tg.TelegramService;
import com.safecharge.exception.SafechargeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "PaymentApi")
@Slf4j
@RestController
public class PaymentApiController extends BaseShopApiController {
    @Autowired
    private PaymentInService inService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private BackService backService;
    @Resource
    private QuoteService quoteService;
    @Autowired
    TelegramService telegramService;
    @Autowired
    private Shift4ApiService shift4ApiService;
    @Autowired
    private Shift4Config shift4Config;
    @Autowired
    private Shift4CallbackService shift4CallbackService;

    @ApiOperation("1. Create Order")
    @GetMapping("/create")
    @ApiResponse(code = 200, message = "Create order result", response = CreateOrderDto.class)
    @LogApi("Create Order")
    public Object createOrder(@Validated CreateOrderVo req) {
        req.check();
        Shop shop = getCheckedShop(req);
        Order order = inService.createOrder(shop, req);
        CreateOrderDto dto = BeanUtil.copyProperties(order, CreateOrderDto.class);
        dto.setCurrency(req.getCurrency());
        dto.setCrypto(req.getCryptoCurrency());
        dto.setOrderNo(order.getId() + "");
        dto.setStatus(OrderStatus.fromCode(order.getStatus()).name());
        dto.setType(req.getOrderType());
        boolean isNftOrder = req.getCreateType() != null && req.getCreateType() == 1;
        if (shop.getIsRedirect() == 1 || shop.getIsRedirect() == 2 || isNftOrder) {
            Customer customer = customerService.getById(order.getCustomerId());
            User user = userService.getById(customer.getUserId());
            String token = tokenService.createToken(user.toLoginUser());
            String paymentUrl = nuveiConfig.getGatewayPayUrl() + "?orderNo=" + dto.getOrderNo() + "&token=" + token;
            if (shop.getIsRedirect() == 1 || isNftOrder) {
                return new RedirectView(paymentUrl);
            } else {
                return R.okData(paymentUrl);
            }
        }
        return R.ok(dto);
    }

    // https://docs.nuvei.com/documentation/integration/testing/testing-cards/
//    @ApiOperation("2. Pay Order")
//    @PostMapping("/pay")
//    @ApiResponse(code = 200, message = "Pay order result", response = PayOrderDto.class)
//    @LogApi("Pay Order")
//    public R<PayOrderRs> payOrder(@Validated @RequestBody PayOrderVo req) throws SafechargeException {
//        String reqJson = JSONObject.toJSONString(req);
//        log.info("Pay request (JSON): {}", reqJson);
//        req.check(true);
//        Order dbOrder = orderService.getById(req.getOrderNo());
//        return inService.payOrder(getCheckedShop(req), dbOrder, req);
//    }

    @ApiOperation("3. Order Status")
    @GetMapping("/orderInfo")
    @ApiResponse(code = 200, message = "Order info", response = OrderInfoDto.class)
    public R<OrderInfoDto> orderInfo(@Validated OrderInfoVo req) {
        Shop shop = getCheckedShop(req);
        Order dbOrder = orderService.getById(req.getOrderNo());
        if (dbOrder == null) {
            dbOrder = orderService.getOrderByMerchantOrderId(req.getOrderNo(), req.getMerchantId());
        }
        if (dbOrder == null) {
            return R.fail(550, "order not exists");
        }
        if (!shop.getId().equals(dbOrder.getMerchantId())) {
            return R.fail(403, "order access denied");
        }
        OrderInfoDto orderInfoDto = inService.orderInfo(dbOrder);
        return R.ok(orderInfoDto);
    }

//    // nuvei - 3DS authentication callback
//    @ApiOperation("5. 3DS Authentication Callback")
//    @PostMapping("/callBack/3ds")
//    @ApiResponse(code = 200, message = "3DS callback result", response = ThreedsCallbackDto.class)
//    public Object threedsCallback(@RequestBody String cres) {
//        System.out.println("Received 3DS callback body: " + cres);
//        byte[] decodedBytes = Base64Decoder.decode(cres);
//        String jsonStr = new String(decodedBytes);
//        System.out.println("Received 3DS callback jsonStr: " + jsonStr);
//        // Fix possible garbled text after decoding
//        jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1);
//        System.out.println("Received 3DS callback jsonStr2: " + jsonStr);
//        TdsCallbackVo req = JSONUtil.toBean(jsonStr, TdsCallbackVo.class);
//
//        Order order = orderService.getByAcquirerId(req.getAcsTransID());
//        if (order == null) {
//            throw new NuveiException("Order not found!");
//        }
//        if (!StrUtil.equalsAny(order.getStatus(), OrderStatus.TDS_PENDING.getCode())) {
//            throw new NuveiException("Order status is not TDS_PENDING error!");
//        }
//        Shop shop = shopService.getById(order.getMerchantId());
//        if (shop == null) {
//            throw new NuveiException("Merchant data not found!");
//        }
//        boolean isSucc = StrUtil.equals("Y", req.getTransStatus());
//        ThreedsCallbackDto dto = inService.threedsCallback(order, shop, isSucc);
//
//        // API endpoint to notify merchant of 3DS authentication result
//        backService.tdsOrderCallBack(order.getId(), dto.getError());
//
//        // Web redirect URL
//        String jumpShopUrl = dto.getError() ? shop.getDefaultRedirectFailureUrl() : shop.getDefaultRedirectSuccessUrl();
//        System.out.println("Received 3DS callback redirect: " + jumpShopUrl);
//        Order orderUpdated = orderService.getById(order.getId());
//        Map<String, Object> signParams = backService.getOrderSignParams(shop, orderUpdated);
//        String jumpFullUrl = SignUtil.convertToUrlParams(signParams, jumpShopUrl);
//        System.out.println("3DS callback redirect with params: " + jumpFullUrl);
//        return new RedirectView(jumpFullUrl);
//    }
//
//    @ApiOperation("Shift4 3DS Authentication Callback")
//    @PostMapping("/callBack/3ds/shift")
//    @ApiResponse(code = 200, message = "3DS callback result", response = ThreedsCallbackDto.class)
//    public Object threedsCallbackShift4(@RequestBody String body) {
//        log.info("Received Shift4 3DS callback raw body: {}", body);
//        if (StrUtil.isBlank(body)) {
//            log.warn("Received empty Shift4 3DS callback body");
//            return R.fail(400, "empty body");
//        }
//
//        // Shift4 callback is form-urlencoded; normalize whitespace/newlines first
//        String normalizedBody = body.replaceAll("\\s+", "");
//        Map<String, String> params = Shift4HttpUtils.parseResponse(normalizedBody);
//
//        String requestId = params.get("a1");
//        String responseCode = params.get("z2");
//        String shift4TransactionId = params.get("z1");
//        String tdsStatus = params.get("3ds_status");
//        log.info("Received Shift4 3DS callback - a1:{}, z2:{}, z1:{}, 3ds_status:{}", requestId, responseCode, shift4TransactionId,
//                tdsStatus);
//
//        // Do not process order when signature verification fails to avoid forged callbacks
//        if (!Shift4SignUtils.verifySignature(params, shift4Config.getSignatureKey())) {
//            log.warn("Shift4 3DS callback signature verification failed - a1:{}, z2:{}, z1:{}", requestId, responseCode, shift4TransactionId);
//            return R.fail(401, "invalid signature");
//        }
//
//        Order order = shift4CallbackService.resolveCallbackOrder(params);
//        if (order == null) {
//            log.warn("Shift4 3DS callback order not found - a1:{}, z1:{}, 3ds_trxid:{}", requestId, shift4TransactionId,
//                    params.get("3ds_trxid"));
//            return R.fail(550, "order not exists");
//        }
//
//        Shop shop = shopService.getById(order.getMerchantId());
//        if (shop == null) {
//            log.warn("Shift4 3DS callback merchant not found - orderId:{}, merchantId:{}", order.getId(), order.getMerchantId());
//            return R.fail(551, "merchant not exists");
//        }
//
//        // Use gateway response code as primary rule: z2=0 means success
//        boolean isPaySucc = StrUtil.equals("0", responseCode);
//        if (StrUtil.isBlank(responseCode)) {
//            // Fallback: if z2 is missing, infer from 3DS fields
//            isPaySucc = StrUtil.equalsIgnoreCase("Y", params.get("3ds_status"))
//                    && StrUtil.equalsIgnoreCase("Y", params.get("3ds_valid_payment"));
//        }
//
//        // Update status only when order is TDS_PENDING to avoid duplicate/invalid changes
//        boolean canUpdate = StrUtil.equalsAny(order.getStatus(), OrderStatus.TDS_PENDING.getCode());
//        if (canUpdate) {
//            Order updateOrder = new Order();
//            updateOrder.setId(order.getId());
//            updateOrder.setUpdateAt(new Date());
//
//            if (StrUtil.isNotBlank(shift4TransactionId)) {
//                updateOrder.setTransactionId(shift4TransactionId);
//            }
//            String tdsTrxId = params.get("3ds_trxid");
//            if (StrUtil.isNotBlank(tdsTrxId)) {
//                updateOrder.setRelatedTransactionId(tdsTrxId);
//            }
//            if (!isPaySucc) {
//                String message = params.get("z3");
//                if (StrUtil.isNotBlank(message)) {
//                    updateOrder.setFailReasons(message);
//                }
//            }
//
//            updateOrder.setStatus(isPaySucc ? OrderStatus.COMPLETED.getCode() : OrderStatus.DEPOSIT_DECLINED.getCode(),
//                    "Update order status from Shift4 3DS callback");
//
//            orderService.updateOrderExcludeStatus(updateOrder, OrderStatus.COMPLETED.getCode());
//
//            // Save token after successful 3DS payment (if callback includes g1)
//            if (isPaySucc) {
//                String token = params.get("g1");
//                if (StrUtil.isNotBlank(token)) {
//                    String cardBrandCode = params.get("b2");
//                    String ipAddress = order.getPaymentIp();
//                    try {
//                        shift4ApiService.saveTokenFrom3DSCallback(order, token, cardBrandCode, ipAddress);
//                        log.info("Shift4 3DS callback token saved - orderId:{}, token:{}", order.getId(), token);
//                    } catch (Exception e) {
//                        log.error("Shift4 3DS callback token save failed - orderId:{}", order.getId(), e);
//                    }
//                } else {
//                    log.info("Shift4 3DS callback returned no token (g1) - orderId:{}", order.getId());
//                }
//            }
//        } else {
//            log.info("Shift4 3DS callback order status is not TDS_PENDING, skip update - orderId:{}, status:{}", order.getId(), order.getStatus());
//        }
//
//        Order orderUpdated = orderService.getById(order.getId());
//        boolean isFail = orderUpdated == null
//                || !StrUtil.equals(orderUpdated.getStatus(), OrderStatus.COMPLETED.getCode());
//
//        // API notification to merchant for 3DS authentication result
//        try {
//            backService.tdsOrderCallBack(order.getId(), isFail);
//        } catch (Exception e) {
//            log.error("Shift4 3DS callback notify merchant failed - orderId:{}, isFail:{}", order.getId(), isFail, e);
//        }
//
//        // Web redirect URL (with signed params)
//        String jumpShopUrl = isFail ? shop.getDefaultRedirectFailureUrl() : shop.getDefaultRedirectSuccessUrl();
//        if (StrUtil.isBlank(jumpShopUrl)) {
//            return R.ok(isFail ? "FAIL" : "SUCCESS");
//        }
//        Map<String, Object> signParams = backService.getOrderSignParams(shop,
//                orderUpdated != null ? orderUpdated : order);
//        String jumpFullUrl = SignUtil.convertToUrlParams(signParams, jumpShopUrl);
//        log.info("Shift4 3DS callback redirect - orderId:{}, isFail:{}, url:{}", order.getId(), isFail, jumpShopUrl);
//        return new RedirectView(jumpFullUrl);
//    }
//
//    @ApiOperation("Shift4 3DS DFP Start Notification")
//    @PostMapping("/callBack/dfp/start")
//    @ApiResponse(code = 200, message = "DFP start notification result")
//    public R<String> dfpStart(@RequestParam Long orderNo) {
//        log.info("Received DFP start notification - orderNo: {}", orderNo);
//
//        Order order = orderService.getById(orderNo);
//        if (order == null) {
//            return R.fail(550, "Order does not exist");
//        }
//        if (!StrUtil.equalsAny(order.getStatus(), OrderStatus.TDS_PENDING.getCode())) {
//            return R.fail(551, "Order status is not TDS_PENDING");
//        }
//
//        // Record DFP start time into updateAt
//        order.setUpdateAt(new Date());
//        orderService.updateById(order);
//
//        log.info("DFP start time recorded - orderNo: {}, time: {}", orderNo, order.getUpdateAt());
//        return R.ok("DFP start time recorded");
//    }
//
//    @ApiOperation("Shift4 3DS DFP Completion Callback (ACS Callback)")
//    @PostMapping("/callBack/3ds/dfp")
//    @ApiResponse(code = 200, message = "DFP completion callback result")
//    public R<?> dfpCallback(@RequestBody String body) {
//        log.info("Received ACS DFP callback raw data: {}", body);
//
//        // Parse ACS callback payload, expected format: threeDSMethodData=eyJ...
//        // Decoded format: {"threeDSServerTransID":"xxx"}
//        String tdsTrxId = null;
//
//        try {
//            if (body.contains("threeDSMethodData")) {
//                // form-urlencoded format: threeDSMethodData=xxx
//                String encoded = body.replace("threeDSMethodData=", "");
//                String decoded = new String(java.util.Base64.getUrlDecoder().decode(encoded));
//                log.info("Decoded threeDSMethodData: {}", decoded);
//                cn.hutool.json.JSONObject json = JSONUtil.parseObj(decoded);
//                tdsTrxId = json.getStr("threeDSServerTransID");
//            }
//        } catch (Exception e) {
//            log.warn("Failed to parse ACS callback data: {}", e.getMessage());
//        }
//
//        log.info("Parse result - tdsTrxId: {}", tdsTrxId);
//
//        if (StrUtil.isBlank(tdsTrxId)) {
//            log.warn("Unable to parse tdsTrxId from callback data");
//            return R.fail(552, "Unable to parse tdsTrxId");
//        }
//
//        // Find order by relatedTransactionId (3ds_trxid)
//        Order order = orderService.getOrderByRelatedTransactionId(tdsTrxId);
//        if (order == null) {
//            log.warn("Order not found by tdsTrxId: {}", tdsTrxId);
//            return R.fail(550, "Order does not exist");
//        }
//        log.info("Order found by tdsTrxId - orderId: {}, tdsTrxId: {}", order.getId(), tdsTrxId);
//
//        if (!StrUtil.equalsAny(order.getStatus(), OrderStatus.TDS_PENDING.getCode())) {
//            return R.fail(551, "Order status is not TDS_PENDING");
//        }
//
//        // Check whether it completed within 10 seconds by comparing now and order.updateAt
//        Date dfpStartTime = order.getUpdateAt();
//        Date now = new Date();
//        long elapsedMs = now.getTime() - dfpStartTime.getTime();
//        boolean within10Seconds = elapsedMs <= 10000; // 10 seconds = 10000 milliseconds
//
//        String compInd = within10Seconds
//                ? com.games.common.constant.Shift4TdsConstants.TDS_COMPIND_YES
//                : com.games.common.constant.Shift4TdsConstants.TDS_COMPIND_NO;
//
//        log.info("DFP elapsed: {}ms, completed within 10s: {}, 3ds_compind: {}", elapsedMs, within10Seconds, compInd);
//
//        // Get originalZ1 from order (z1 is stored in transactionId)
//        String originalZ1 = order.getTransactionId();
//
//        // Complete payment via complete3DSWithDfp
//        PayOrderResult result = shift4ApiService.complete3DSWithDfp(order, originalZ1, tdsTrxId, compInd, null);
//
//        // Update order status
//        if (result.isSucc() && result.getOrderStatus() != null) {
//            order.setStatus(result.getOrderStatus().getCode(), "Updated after DFP completion");
//            orderService.updateById(order);
//        }
//
//        // Build response
//        PayOrderDto payOrderDto = BeanUtil.copyProperties(order, PayOrderDto.class);
//        payOrderDto.setStatus(result.getOrderStatus() != null ? result.getOrderStatus().name() : null);
//
//        return R.ok(new PayOrderRs(result.isTds(), result.getTdsDto(), payOrderDto));
//    }
//
//    // nuvei - order status callback https://api.wynpay.io/api/callBack/order
//    // https://prodnft.wynpay.io/api/open/pay/callback/batch
//    @ApiOperation("4. Order Status Callback")
//    @PostMapping("/callBack/order")
//    @ApiResponse(code = 200, message = "Order status callback", response = ThreedsCallbackDto.class)
//    public R<OrderCallbackDto> orderCallback(ThreedsCallbackVo req) {
//        System.out.println("Received order status callback body: " + JSONUtil.toJsonStr(req));
//        OrderCallbackDto threedsCallbackDto = inService.orderCallback(req);
//        backService.oneOrderCallBack(threedsCallbackDto.getOrderId());
//        return R.ok(threedsCallbackDto);
//    }
//
//    // https://api.wynpay.io/api/callBack/order/mock
//    @ApiOperation("Mock - Order Callback")
//    @ResponseBody
//    @PostMapping("/callBack/order/mock")
//    public R mockOrderBack(HttpServletRequest request) {
//        String body = ServletUtil.getBody(request);
//        System.out.println("Mock callback order received data: " + body);
//        List<OrderInfoSignDto> orderInfoSignResList = JSONUtil.toList(body, OrderInfoSignDto.class);
//        return R.ok(orderInfoSignResList);
//    }
//
//    @ApiOperation("Mock - 3DS Callback")
//    @ResponseBody
//    @GetMapping("/callBack/3ds/mock/{state}")
//    public Object mock3dsBack(@PathVariable String state, String orderNo) {
//        telegramService.pushMessage("-4963535149", "Mock message sent");// broadcast
//        // telegramService.pushMessage("6992256803", "Mock message sent"); // personal
//
//        // https://api.wynpay.io/api/callBack/3ds/mock/success
//        // https://api.wynpay.io/api/callBack/3ds/mock/error
//        System.out.println(StrUtil.format("Mock 3DS callback {},{}", state, orderNo));
//        // return new RedirectView("https://api.wynpay.io/success");
//        return R.okData("success");
//    }
//
    @ApiOperation("Get Liquidity Quote")
    @ResponseBody
    @GetMapping("/getQuote")
    public R<QuoteResult> getQuote(String side, String crypto, String currency, Double quantity) {
        return R.ok(quoteService.getQuote(side, crypto, currency, BigDecimal.valueOf(quantity), "B2C2"));
    }
//
//    @ApiOperation("Order Refund")
//    @PostMapping("/refund")
//    @ApiResponse(code = 200, message = "Order info", response = RefundOrderDto.class)
//    @LogApi("Order Refund")
//    public R<RefundOrderDto> refundOrder(@Validated @RequestBody RefundOrderVo req) {
//        getCheckedShop(req);
//        Order dbOrder = orderService.getById(req.getOrderNo());
//        if (dbOrder == null) {
//            dbOrder = orderService.getOrderByMerchantOrderId(req.getOrderNo(), req.getMerchantId());
//        }
//        if (dbOrder == null) {
//            return R.fail(550, "order not exists");
//        }
//        return R.ok(inService.refundOrder(dbOrder, req));
//    }
//
//    @ApiOperation("Query Refund Details")
//    @GetMapping("/getRefundDetails")
//    @ApiResponse(code = 200, message = "Order info", response = RefundOrderDto.class)
//    public R<RefundOrderDto> getRefundDetails(@Validated RefundDetailVo req) {
//        getCheckedShop(req);
//        Order dbOrder = orderService.getById(req.getOrderNo());
//        if (dbOrder == null) {
//            dbOrder = orderService.getOrderByMerchantOrderId(req.getOrderNo(), req.getMerchantId());
//        }
//        if (dbOrder == null) {
//            return R.fail(550, "order not exists");
//        }
//        return R.ok(inService.getRefundDetail(dbOrder, req));
//    }
//
//    @ApiOperation("Merchant Balance Query")
//    @ApiResponse(code = 200, message = "Merchant balance result", response = MerchantBalanceDto.class)
//    @GetMapping("/balance")
//    public R<MerchantBalanceDto> getMerchantBalance(@Validated MerchantBalanceVo req) {
//        getCheckedShop(req);
//        Shop shop = shopService.getById(req.getMerchantId());
//        return R.ok(inService.getBalance(shop));
//    }
//
//    @GetMapping("/getSign")
//    public R<String> getSign(SignInfoVo req) {
//        Order dbOrder = orderService.getById(req.getOrderNo());
//        Shop shop;
//        if (req.getMerchantId() != null) {
//            shop = shopService.getById(req.getMerchantId());
//        } else {
//            shop = shopService.getById(dbOrder.getMerchantId());
//        }
//        return R.ok(SignUtil.sign(shop.getMerchantHashSecretKey(), req.getOrderNo()));
//    }
//
//    @Autowired
//    NuveiApiService nuveiApiService;
//
//    @ApiOperation("Place Order")
//    @GetMapping("/openOrder")
//    @ApiResponse(code = 200, message = "Place order result", response = R.class)
//    public R<OpenOrderVo> openOrder(String amount, String currency) throws SafechargeException {
//        OpenOrderVo vo = nuveiApiService.openOrder(amount, currency);
//        return R.okData(vo);
//    }

    @ApiOperation("Health Check")
    @GetMapping("/common/safeCheck")
    @ApiResponse(code = 200, message = "Health check result", response = R.class)
    public R<String> safeCheck() {
        return R.ok();
    }
}
