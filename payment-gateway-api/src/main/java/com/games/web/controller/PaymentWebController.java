package com.games.web.controller;

import com.games.api.PaymentInService;
import com.games.api.dto.OrderInfoDto;
import com.games.api.dto.PayOrderDto;
import com.games.api.dto.PayOrderRs;
import com.games.api.vo.PayOrderVo;
import com.games.common.annotation.LogApi;
import com.games.common.core.domain.R;
import com.games.common.enums.BusinessProfileStatus;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.service.IOrderService;
import com.safecharge.exception.SafechargeException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@Api(tags = "WynPayWeb")
@Slf4j
@RestController
public class PaymentWebController extends BaseAppUserController{
    @Autowired
    private PaymentInService inService;
    @Autowired
    private IOrderService orderService;

    @ApiOperation("2. Pay Order")
    @PostMapping("/web/pay")
    @ApiResponse(code = 200, message = "Pay order result", response = PayOrderRs.class)
    @LogApi("Web Pay Order")
    public R<PayOrderRs> payOrder(@Validated @RequestBody PayOrderVo req) throws SafechargeException {
        req.check(false);
        Order dbOrder = orderService.getById(req.getOrderNo());
        requireOrderOwner(dbOrder);
        Shop shop = shopService.getInfoById(dbOrder.getMerchantId());
        boolean businessProfileOk = shop.getBusinessProfileStatus() != null &&
                shop.getBusinessProfileStatus().equals(BusinessProfileStatus.OK.getCode());
        if (!businessProfileOk) {
            throw new RuntimeException("Merchant review status is not approved. Please contact support.");
        }

        // Check merchant switch status
        boolean shopSwitch = shop.getStatus() != null && shop.getStatus() == 0;
        if (!shopSwitch) {
            throw new RuntimeException("Channel authorization is not enabled yet. Please contact support.");
        }
        return inService.payOrder(shop, dbOrder, req);
    }

    @ApiOperation("3. Order Status")
    @GetMapping("/web/orderInfo")
    @ApiResponse(code = 200, message = "Order info", response = OrderInfoDto.class)
    public R<OrderInfoDto> orderInfo(String orderNo) {
//        Long userId = getLoginUserId();
        Order dbOrder = orderService.getById(orderNo);
        requireOrderOwner(dbOrder);
        OrderInfoDto orderInfoDto = inService.orderInfo(dbOrder);
        return R.ok(orderInfoDto);
    }
}
