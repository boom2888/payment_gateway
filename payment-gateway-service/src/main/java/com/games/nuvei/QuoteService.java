package com.games.nuvei;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.games.api.enums.FeeIncludeOption;
import com.games.common.constant.HttpStatus;
import com.games.common.core.redis.RedisCache;
import com.games.common.core.text.Convert;
import com.games.common.utils.StringUtils;
import com.games.common.utils.uuid.IdUtils;
import com.games.nuvei.vo.QuoteResult;
import com.games.system.service.ISysConfigService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Data
@Service
@Slf4j
public class QuoteService {

    @Value("${b2c2.url:}")
    private String b2c2Url;

    @Value("${b2c2.apiKey:}")
    private String apiToken;

    @Value("${wynpay.isSignDev:false}")
    private Boolean isSignDev;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取报价信息
     *
     * @param side     交易方向：'buy' 买入 或 'sell' 卖出
     * @param crypto   加密货币代码，如 'BTC', 'ETH', 'UST', 'USC' 等
     * @param currency 法定货币代码，如 'USD', 'EUR', 'CNY' 等
     * @param quantity 交易数量（根据 side 决定是加密货币数量还是法币数量）
     * @return QuoteResult 报价结果
     */
    public QuoteResult getQuote(String side, String crypto, String currency, BigDecimal quantity, String quoteSource) {
        String func = "api-create-sso-order";
        String fullMsgName = "getQuoteInput";
        String rfqId = IdUtils.simpleUUID();
        String cacheKey = "RATE_" + crypto +"_" + currency;
        if(isSignDev){
            return new QuoteResult(BigDecimal.valueOf(0.8D), "dev", null);
        }
        // 特殊处理 USC/UST 对 USD 情况
        if (("UST".equalsIgnoreCase(crypto) || "USC".equalsIgnoreCase(crypto))
                && "USD".equalsIgnoreCase(currency)) {
            return new QuoteResult(BigDecimal.valueOf(1.0D), "b2c2", null);
        }
        if(!StrUtil.equalsIgnoreCase(quoteSource, "B2C2")){
            return new QuoteResult(BigDecimal.valueOf(1.0D), "self", null);
        }

        // 构造请求参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("side", side);
        paramMap.put("instrument", crypto + currency + ".SPOT");
        paramMap.put("client_rfq_id", rfqId);
        paramMap.put("quantity", String.valueOf(quantity));

        try {
            String url = b2c2Url + "/request_for_quote/";

            HttpResponse response = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("Authorization", StringUtils.format("Token {}", apiToken))
                    .body(JSONUtil.toJsonStr(paramMap))
                    .timeout(5000)
                    .execute();
            System.out.println(StringUtils.format("b2c2 url:{}", url));
            System.out.println(StringUtils.format("Token {}", apiToken));
            log.info("B2C2 quote response status: {}", response.getStatus());

            if (response.getStatus() != HttpStatus.SUCCESS && response.getStatus() != HttpStatus.CREATED) {
                throw new RuntimeException("Quote service returned non-200 status: " + response.getStatus());
            }

            Map<String, Object> respMap = JSONUtil.toBean(
                    response.body(),
                    new TypeReference<Map<String, Object>>(){},
                    false
            );
            // 注意，当前按照流动性报价传0.01获取的，因为返回的单位是分，所以不用*100，直接当元用
            BigDecimal liquidityQuote = BigDecimal.valueOf(MapUtil.getDouble(respMap, "price"));
            redisCache.setCacheObject(cacheKey, liquidityQuote.toString(), 10 , TimeUnit.DAYS);
            return new QuoteResult(liquidityQuote, null);

        } catch (Exception e) {
            log.error("[GET_QUOTE_FAILURE] func={}, msgName={}, err={}, request={}",
                    func, fullMsgName, e.getMessage(), JSONUtil.toJsonStr(paramMap), e);

            // fallback
            if ("UST".equalsIgnoreCase(crypto) && "USD".equalsIgnoreCase(currency)) {
                return new QuoteResult(BigDecimal.valueOf(1.0D), "b2c2", null);
            }
            String cacheRate = Convert.toStr(redisCache.getCacheObject(cacheKey), "1");
            log.info("读区汇率使用了汇率缓存:{}={}", cacheKey, cacheRate);
            return new QuoteResult(new BigDecimal(cacheRate), null);
        }
    }

    /**
     * 计算加密货币数量
     *
     * @param fiatAmount      法币金额
     * @param exchangeRate    汇率（1 法币 = exchangeRate 加密货币）
     * @param fee             手续费（法币单位）
     * @param feeIncludeOption 手续费选项
     * @return 加密货币数量（BigDecimal，保留5位小数）
     */
    public BigDecimal getCrypto(BigDecimal fiatAmount, BigDecimal exchangeRate, BigDecimal fee, FeeIncludeOption feeIncludeOption) {
        if (fiatAmount.doubleValue() == 0 || exchangeRate.doubleValue() == 0) {
            return BigDecimal.ZERO.setScale(5, RoundingMode.HALF_UP);
        }
        switch (feeIncludeOption) {
            case INCLUDE:
                fiatAmount = fiatAmount.subtract(fee).max(BigDecimal.ZERO);
                break;
            case EXCLUDE:
            case MERCHANT:
                // nothing changes
                break;
            default:
                return BigDecimal.ZERO.setScale(5, RoundingMode.HALF_UP);
        }
        BigDecimal result = fiatAmount.multiply(exchangeRate);
        return result.max(BigDecimal.ZERO).setScale(5, RoundingMode.HALF_UP);
    }

    /**
     * 计算法币数量
     *
     * @param cryptoAmount     加密货币数量
     * @param exchangeRate     汇率（1 加密货币 = exchangeRate 法币）
     * @param fee              手续费（法币单位）
     * @param feeIncludeOption 手续费选项
     * @return 法币数量（BigDecimal，保留2位小数）
     */
    public BigDecimal getFiat(BigDecimal cryptoAmount, BigDecimal exchangeRate, BigDecimal fee, FeeIncludeOption feeIncludeOption) {
        if (cryptoAmount.doubleValue() == 0 || exchangeRate.doubleValue() == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal baseFiatAmount = cryptoAmount.multiply(exchangeRate);

        switch (feeIncludeOption) {
            case INCLUDE:
                baseFiatAmount = baseFiatAmount.subtract(fee).max(BigDecimal.ZERO);
                break;
            case EXCLUDE:
            case MERCHANT:
                // nothing changes
                break;
            default:
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return baseFiatAmount.setScale(2, RoundingMode.HALF_UP);
    }



}
