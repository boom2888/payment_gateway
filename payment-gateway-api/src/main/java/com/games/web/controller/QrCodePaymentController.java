//package com.games.web.controller;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.extra.servlet.ServletUtil;
//import cn.hutool.json.JSONUtil;
//import com.games.api.QrcodePaymentService;
//import com.games.api.dto.CaratCreateOrderDto;
//import com.games.api.dto.CreateOrderDto;
//import com.games.api.dto.OrderCallbackDto;
//import com.games.api.dto.OrderInfoSignDto;
//import com.games.api.enums.OrderStatus;
//import com.games.api.factory.PaymentFactory;
//import com.games.api.vo.QrCodeCreateOrderVo;
//import com.games.caratpay.CaratPayApiService;
//import com.games.common.annotation.LogApi;
//import com.games.common.core.domain.R;
//import com.games.payment.domain.Order;
//import com.games.payment.domain.Shop;
//import com.games.payment.service.BackService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Map;
//
//@Api(tags = "caratPay")
//@Slf4j
//@RestController
//@RequestMapping("/qrcode")
//public class QrCodePaymentController extends BaseShopApiController {
//
//    @Resource
//    private QrcodePaymentService inService;
//    @Resource
//    private PaymentFactory paymentFactory;
//    @Resource
//    private CaratPayApiService caratPayApiService;
//    @Resource
//    private BackService backService;
//
//    @ApiOperation("1. Create Order")
//    @GetMapping("/create")
//    @ApiResponse(code = 200, message = "Create order result", response = CreateOrderDto.class)
//    @LogApi("创建订单")
//    public Object createOrder(@Validated QrCodeCreateOrderVo req) {
//        req.check();
//        Shop shop = getCheckedShop(req);
//        Order order = inService.createOrder(shop, req);
//        CaratCreateOrderDto dto = BeanUtil.copyProperties(order, CaratCreateOrderDto.class);
//        dto.setCurrency(req.getCurrency());
//        dto.setOrderNo(order.getId()+"");
//        dto.setStatus(OrderStatus.fromCode(order.getStatus()).name());
//        // 将 CaratPay 返回的付款链接透传给前端（二维码/表单跳转）
//        dto.setPayUrl(order.getPayParams());
//        return R.ok(dto);
//    }
//
//    @ApiOperation("2. Order Status Callback - by Provider")
//    @PostMapping("/callBack/{provider}/order")
//    @ApiResponse(code = 200, message = "Order status callback", response = OrderCallbackDto.class)
//    @LogApi("订单状态回调-按渠道")
//    public Object orderCallbackByProvider(
//            @PathVariable("provider") String provider,
//            @RequestBody String rawBody,
//            @RequestHeader Map<String, String> headers) {
//        log.info("收到订单状态回调[{}] body: {}", provider, rawBody);
//        log.info("headers: {}", headers);
////        OrderCallbackDto result = paymentFactory.getPaymentStrategy(provider.toLowerCase()).
//        OrderCallbackDto result = caratPayApiService.handleCallback(rawBody, headers);
//        backService.oneOrderCallBack(result.getOrderId());
//        return "success";
//    }
//
//    @ApiOperation("3. Mock - Order Callback")
//    @ResponseBody
//    @PostMapping("/callBack/order/mock")
//    public R mockOrderBack(HttpServletRequest request)
//    {
//        String body = ServletUtil.getBody(request);
//        System.out.println("模拟回调订单收到数据: " + body);
//        List<OrderInfoSignDto> orderInfoSignResList = JSONUtil.toList(body, OrderInfoSignDto.class);
//        return R.ok(orderInfoSignResList);
//    }
//}
