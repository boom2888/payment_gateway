package com.games.wynpay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.games.common.utils.MessageUtils;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.games.api.dto.CreateOrderDto;
import com.games.api.dto.PayOrderRs;
import com.games.api.vo.ApiBaseReq;
import com.games.api.vo.CreateOrderVo;
import com.games.api.vo.PayOrderVo;
import com.games.common.exception.BusinessException;
import com.games.common.utils.SignUtil;
import com.games.wynpay.enums.WynApiType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class WynPayApiUtils {
    private static final String HTTP_CODE = "code";

//    public CreateOrderDto makeOrder(CreateOrderVo vo, WynPayConfig config, WynApiType apiType){
//        Map<String, Object> logicParam = SignUtil.getParams(vo);
//        String sign =  SignUtil.sign(config.getMerchantKey(), vo.getSignParams());
//        logicParam.put("sign", sign);
//        String reqUrl = config.getApiUrl() + apiType.getPath();
//        String fullUrl = SignUtil.convertToUrlParams(logicParam, reqUrl);
//        String responce = HttpUtil.get(fullUrl);
//        return JSONUtil.toBean(responce, CreateOrderDto.class);
//    }

    public CreateOrderDto makeOrder(CreateOrderVo vo, WynPayConfig config){
        JSONObject jsonObject = getRequest(true, vo, config, WynApiType.CREATE_ORDER);
        JSONObject data = jsonObject.getJSONObject("data");
        return data.toJavaObject(CreateOrderDto.class);
    }

    public PayOrderRs payOrder(PayOrderVo vo, WynPayConfig config){
        JSONObject jsonObject = getRequest(false, vo, config, WynApiType.PAY_ORDER);
        JSONObject data = jsonObject.getJSONObject("data");
        return data.toJavaObject(PayOrderRs.class);
    }

    private JSONObject getRequest(boolean isGet, ApiBaseReq vo, WynPayConfig config, WynApiType apiType){
        String merId = config.getMerchantId();
        String merKey = config.getMerchantKey();
        if(StrUtil.isBlank(merId)){
            throw BusinessException.api("merchant.id.not.configured");
        }
        vo.setMerchantId(merId);

        Map<String, Object> params = vo.getParams();
        String baseUrl = config.getApiUrl() + apiType.getPath();
        try{
            String sign = SignUtil.sign(merKey, vo.getSignParams());
            params.put("sign", sign);

            String fullUrl = SignUtil.convertToUrlParams(params, baseUrl);
            log.info("Calling WynPay endpoint: {}", baseUrl);

            String resBody;
            try (HttpResponse httpResponse = isGet
                    ? HttpRequest.get(fullUrl).timeout(60000).execute()
                    : HttpRequest.post(baseUrl)
                    .header("Content-Type", "application/json")  // 指定为 JSON 类型
                    .body(JSONUtil.toJsonStr(params))           // 使用 JSON 字符串作为请求体
                    .timeout(60000)
                    .execute()) {

                resBody = httpResponse.body();
            }
            log.info("Res=>{}", resBody);

            JSONObject respJson = JSON.parseObject(resBody);
            if (!respJson.get(HTTP_CODE).equals(HttpStatus.HTTP_OK)) {
                log.error("调用接口失败,失败原因：{}", respJson);
                String msg = respJson.getString("msg");
                if(StrUtil.isNotBlank(msg)){
                    throw new BusinessException(msg);
                }
                throw BusinessException.api("api.call.failed");
            }
            return respJson;
        }catch (BusinessException e){
            throw e;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException("CHAIN_API_ERR");
        }
    }

}
