package com.games.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.games.api.enums.FromSource;
import com.games.api.enums.OrderStatus;
import com.games.common.exception.NuveiException;
import com.games.common.utils.SignUtil;
import com.games.payment.domain.Currency;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.service.ICurrencyService;
import com.games.payment.service.IOrderService;
import com.games.payment.service.IShopService;
import com.games.web.mock.WynOrderVo;
import com.stripe.Stripe;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Stripe Bank Transfer")
@Slf4j
@RestController
public class StripeController extends BaseAppUserController {
//http://localhost:8800/api/createStripePayment
    //https://deepay.ai/api/callbackStripe
    @Autowired
    private IOrderService orderService;
    @Autowired
    ICurrencyService currencyService;
    @Autowired
    IShopService shopService;
    @Value("${wynpay.apiUrl:}")
    private String apiUrl;
    @Value("${stripe.apiKey:}")
    private String apiKey;
    @Value("${stripe.endpointSecret:}")
    private String endpointSecret;
    @Value("${stripe.connectEndpointSecret:}")
    private String connectEndpointSecret;


    @ApiOperation("1. Create Payment")
    @GetMapping("/createStripePayment")
    public Map<String, String> createPayment(Long orderNo) throws Exception {
        Order dbOrder = orderService.getById(orderNo);
        requireOrderOwner(dbOrder);
        Stripe.apiKey = apiKey;
        Currency currency = currencyService.getById(dbOrder.getCurrencyId());
        Shop shop = shopService.getById(dbOrder.getMerchantId());
        boolean isStripeConnetc = StrUtil.equals(shop.getFromSource(), FromSource.STRIPE_CONNECT.name());
        String shopStripeAccount = shop.getStripeAccount();

        Long amount = dbOrder.getFiatAmount().multiply(BigDecimal.valueOf(100)).longValue();
        Long amountFee =  isStripeConnetc ? dbOrder.getFiatAmount().multiply(BigDecimal.valueOf(100)).multiply(shop.getStripeFee()).longValue() : 0L;

        PaymentIntentCreateParams.Builder builder = PaymentIntentCreateParams.builder();
        builder.setAmount(amount);
        if (isStripeConnetc) {
            builder.setApplicationFeeAmount(amountFee);
        }
        builder.setCurrency(currency.getCode());
        builder.addPaymentMethodType("card");
        PaymentIntentCreateParams params = builder.build();

        // 4. 关键：配置 RequestOptions，指定交易所在的子商户账户
        RequestOptions requestOptions = RequestOptions.builder()
                .setStripeAccount(shopStripeAccount)
                .build();

        PaymentIntent intent = isStripeConnetc ? PaymentIntent.create(params, requestOptions) : PaymentIntent.create(params);
        dbOrder.setAcquirerId(intent.getId());
        dbOrder.setTransactionId(intent.getId());
        orderService.updateById(dbOrder);
        Map<String, String> result = new HashMap<>();
        result.put("clientSecret", intent.getClientSecret());
        if(isStripeConnetc){
            result.put("shopStripeAccount", shopStripeAccount);
        }
        return result;
    }

    @ApiOperation("1. Mock Purchase")
    @GetMapping("/buy")
    public Object buy(BigDecimal price, String currency, String productName) throws Exception {
        Shop shop = shopService.getById(1);
        WynOrderVo vo = new WynOrderVo(shop, price, currency);
        if(StrUtil.isNotBlank(productName)) {
            vo.setProductName(productName);
        }
        String wnyPayUrl = apiUrl + "/api/create";
        Map<String, Object> signParams = SignUtil.getParams(vo);
        String fullUrl = SignUtil.convertToUrlParams(signParams, wnyPayUrl);
        System.out.println(DateUtil.formatDateTime(new Date()) + "生成新订单:" + fullUrl);
        Map<String, String> result = new HashMap<>();
        result.put("payUrl", fullUrl);
        return result;
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = request.getReader();
            char[] buff = new char[1024];
            int len;
            while ((len = reader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }


    @ApiOperation("Stripe Payment Callback")
    @PostMapping("/callbackStripe")
    public ResponseEntity<String> callbackStripePlant(HttpServletRequest request) {
        return callbackStripe(request, false);
    }

    @ApiOperation("Stripe Connect Callback")
    @PostMapping("/connectCallbackStripe")
    public ResponseEntity<String> connectCallbackStripe(HttpServletRequest request) {
        return callbackStripe(request, true);
    }


    public ResponseEntity<String> callbackStripe(HttpServletRequest request, boolean isConnect) {
        Stripe.apiKey = apiKey;
        String payload;
        try {
            payload = getRequestBody(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("读取失败");
        }
        String sigHeader = request.getHeader("Stripe-Signature");
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, isConnect ? connectEndpointSecret : endpointSecret);
        } catch (Exception e) {
            System.out.println("签名验证失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("签名错误");
        }

        // 统一处理对象
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = deserializer.getObject().orElse(null);

        // fallback 方案（关键）
        if (stripeObject == null) {
            String json = event.getData().getObject().toJson();
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    stripeObject = PaymentIntent.GSON.fromJson(json, PaymentIntent.class);
                    break;
                case "payment_intent.payment_failed":
                    stripeObject = PaymentIntent.GSON.fromJson(json, PaymentIntent.class);
                    break;
                case "charge.succeeded":
                case "charge.refunded":
                    stripeObject = Charge.GSON.fromJson(json, Charge.class);
                    break;
                case "refund.created":
                    stripeObject = Refund.GSON.fromJson(json, Refund.class);
                    break;
                default:
                    System.out.println("未知事件类型: " + event.getType());
            }
        }

        switch (event.getType()) {

            /**
             * ✅ 支付成功
             */
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

                String paymentIntentId = paymentIntent.getId();
                Long amount = paymentIntent.getAmount(); // 总金额（分）
                Long received = paymentIntent.getAmountReceived(); // 实收金额
                String currency = paymentIntent.getCurrency();
                String status = paymentIntent.getStatus();
                boolean isPaySucc  = true;
                System.out.println("✅ 支付成功");
                System.out.println("订单ID: " + paymentIntentId);
                System.out.println("金额: " + amount);
                System.out.println("实收: " + received);
                System.out.println("币种: " + currency);
                System.out.println("状态: " + status);

                Order order = orderService.getByAcquirerId(paymentIntentId);
                if (order == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found");
                }
                // TODO: 更新订单状态
                Order updateOrder = new Order();
                updateOrder.setId(order.getId());
                updateOrder.setUpdateAt(new Date());
                updateOrder.setStatus(isPaySucc ? OrderStatus.COMPLETED.getCode() : OrderStatus.DEPOSIT_DECLINED.getCode(),
                        "Stripe回调更新订单状态");
                orderService.updateOrderExcludeStatus(updateOrder, OrderStatus.COMPLETED.getCode());
                break;
            /**
             * ❌ 支付失败
             */
            case "payment_intent.payment_failed":
                PaymentIntent failedIntent = (PaymentIntent) stripeObject;
                System.out.println("❌ 支付失败");
                System.out.println("订单ID: " + failedIntent.getId());
                String error = "";
                if (failedIntent.getLastPaymentError() != null) {
                    error = failedIntent.getLastPaymentError().getMessage();
                    System.out.println("失败原因: " + failedIntent.getLastPaymentError().getMessage());
                }
                String paymentIntentId2 = failedIntent.getId();
                Order orderDb = orderService.getByAcquirerId(paymentIntentId2);
                if (orderDb == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found");
                }

                Order updateFailedOrder = new Order();
                updateFailedOrder.setId(orderDb.getId());
                updateFailedOrder.setUpdateAt(new Date());
                updateFailedOrder.setFailReasons(error);
                updateFailedOrder.setStatus(OrderStatus.DEPOSIT_DECLINED.getCode(),"Stripe回调更新订单状态");
                orderService.updateOrderExcludeStatus(updateFailedOrder, OrderStatus.DEPOSIT_DECLINED.getCode());
                break;

            /**
             * 💸 退款（推荐用这个）
             */
            case "charge.refunded":
                Charge charge = (Charge) stripeObject;

                System.out.println("💸 发生退款");
                System.out.println("支付ID: " + charge.getPaymentIntent());
                System.out.println("退款金额: " + charge.getAmountRefunded());

                // TODO: 更新退款状态
                break;

            /**
             * 💸 单笔退款事件
             */
            case "refund.created":
                Refund refund = (Refund) stripeObject;

                System.out.println("💸 创建退款");
                System.out.println("退款ID: " + refund.getId());
                System.out.println("退款金额: " + refund.getAmount());

                break;

            /**
             * 💰 手续费（重点）
             * 需要监听 balance_transaction
             */
            case "charge.succeeded":
                Charge successCharge = (Charge) stripeObject;

                String balanceTxId = successCharge.getBalanceTransaction();

                if (balanceTxId != null) {
                    try {
                        BalanceTransaction bt = BalanceTransaction.retrieve(balanceTxId);

                        Long fee = bt.getFee();              // 手续费
                        Long net = bt.getNet();              // 实际到账
                        Long gross = bt.getAmount();         // 总金额
                        System.out.println("支付ID: " + successCharge.getPaymentIntent());
                        System.out.println("💰 费用信息");
                        System.out.println("总金额: " + gross);
                        System.out.println("手续费: " + fee);
                        System.out.println("净收入: " + net);

                    } catch (Exception e) {
                        System.out.println("获取手续费失败: " + e.getMessage());
                    }
                }
                break;

            default:
                System.out.println("未处理事件: " + event.getType());
        }

        return ResponseEntity.ok("success");
    }

}
