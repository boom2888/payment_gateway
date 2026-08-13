package com.games.web.controller;

import com.games.api.dto.CreateOrderDto;
import com.games.api.vo.CreateOrderVo;
import com.games.common.annotation.LogApi;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "Stripe Page")
@Slf4j
@Controller
public class StripeViewController extends BaseShopApiController {

    @GetMapping("/")
    public String index(Model model) {
        return "index"; // 对应 templates/index.ftl
    }

}
