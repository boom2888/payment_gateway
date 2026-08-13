//package com.games.web.controller;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.games.api.dto.CreateOrderDto;
//import com.games.api.vo.CreateOrderVo;
//import com.games.common.annotation.LogApi;
//import com.games.common.core.domain.R;
//import com.games.common.utils.SignUtil;
//import com.games.nuvei.QuoteService;
//import com.games.nuvei.vo.CryptoCurrencyVo;
//import com.games.nuvei.vo.QuoteResult;
//import com.games.payment.domain.Crypto;
//import com.games.payment.domain.Currency;
//import com.games.payment.domain.Shop;
//import com.games.payment.service.ICryptoService;
//import com.games.payment.service.ICurrencyService;
//import com.games.payment.service.IShopService;
//import com.games.web.core.config.SwapConfig;
//import com.games.wynpay.swap.SwapState;
//import com.games.wynpay.swap.domain.SwapOrder;
//import com.games.wynpay.swap.service.ISwapOrderService;
//import com.games.wynpay.swap.vo.NewSwapVo;
//import com.games.wynpay.swap.vo.SwapPriceVo;
//import com.games.wynpay.swap.vo.WynOrderVo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import jakarta.annotation.Resource;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Api(tags = "Swap")
//@Slf4j
//@RestController
//public class SwapController {
//    @Autowired
//    ICurrencyService currencyService;
//    @Autowired
//    ICryptoService cryptoService;
//    @Resource
//    private QuoteService quoteService;
//    @Autowired
//    SwapConfig swapConfig;
//    @Autowired
//    protected IShopService shopService;
//    @Autowired
//    protected ISwapOrderService swapOrderService;
//
//
//    @ApiOperation("Get Fiat Currencies")
//    @ResponseBody
//    @GetMapping("/getCurrency")
//    public R<List<CryptoCurrencyVo>> getCurrency() {
//        List<Currency> useAbleList = currencyService.getUseAbleList();
//        List<CryptoCurrencyVo> voList = useAbleList.stream().map(Currency::toVo).collect(Collectors.toList());
//        return R.ok(voList);
//    }
//
//    @ApiOperation("Get Crypto Currencies")
//    @ResponseBody
//    @GetMapping("/getCrypto")
//    public R<List<CryptoCurrencyVo>> getCrypto() {
//        List<Crypto> useAbleList = cryptoService.getUseAbleList();
//        List<CryptoCurrencyVo> mergedList = useAbleList.stream()
//                .collect(Collectors.groupingBy(Crypto::getShortName))
//                .entrySet()
//                .stream()
//                .map(e -> new CryptoCurrencyVo(
//                        e.getKey(),
//                        e.getValue().get(0).getName(),
//                        e.getValue().get(0).getIcon(),
//                        e.getValue().stream().map(Crypto::getChain).collect(Collectors.toSet())
//                ))
//                .collect(Collectors.toList());
//        return R.ok(mergedList);
//    }
//
//    @ApiOperation("Get Quote")
//    @ResponseBody
//    @GetMapping("/getPrice")
//    public R<SwapPriceVo> getPrice(String crypto, String currency) {
//        Crypto cryptoType = cryptoService.getBySymbol(crypto);
//        BigDecimal quantity = cryptoType.getIsUtxoBased();
////        String sideValue = StrUtil.equalsIgnoreCase("SELL", side) ? "sell" : "buy";
//        QuoteResult resultBuy = quoteService.getQuote("buy", cryptoType.getB2c2Ticker(), currency, quantity, "B2C2");
//        QuoteResult resultSell = quoteService.getQuote("sell", cryptoType.getB2c2Ticker(), currency, quantity, "B2C2");
////        BigDecimal sellPrice = BigDecimal.ONE.divide(resultSell.getLiquidityQuote(), 10, RoundingMode.HALF_UP);
//        SwapPriceVo vo = new SwapPriceVo(resultBuy.getLiquidityQuote(), resultSell.getLiquidityQuote());
//        return R.ok(vo);
//    }
//
//    @ApiOperation("1. Create Swap Order")
//    @GetMapping("/createSwapOrder")
//    @ApiResponse(code = 200, message = "Create order result", response = CreateOrderDto.class)
//    @LogApi("创建订单")
//    public Object createSwapOrder(@Validated NewSwapVo req) {
//        SwapOrder swapOrder = BeanUtil.copyProperties(req, SwapOrder.class);
//        swapOrder.setState(SwapState.NO.name());
//        swapOrder.setBuySell(req.getSide());
//        swapOrder.setCreateTime(new Date());
//        swapOrder.setContact(req.getCrypto());
//        swapOrder.setShopId(swapConfig.getSwapShopId());
//        swapOrderService.save(swapOrder);
//
//        Shop shop = shopService.getById(swapConfig.getSwapShopId());
//        WynOrderVo vo = new WynOrderVo(swapOrder, shop);
//
//        Map<String, Object> fieldMap = BeanUtil.beanToMap(vo, false, true);
//        fieldMap.put("createType", 2);
//        String wnyPayUrl = swapConfig.getSwapPayUrl();
//        String fullUrl = SignUtil.convertToUrlParams(fieldMap, wnyPayUrl);
//        String res = HttpUtil.get(fullUrl);
//        System.out.println("创建订单返回:" + res);
//        JSONObject resObj = JSONUtil.parseObj(res);
//        String orderNo = resObj.getJSONObject("data").getStr("orderNo");
//        return R.okData(orderNo);
//    }
//
//}
