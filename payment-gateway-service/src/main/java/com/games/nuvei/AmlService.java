package com.games.nuvei;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.games.api.bean.AmlResult;
import com.games.common.utils.MessageUtils;
import com.games.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AmlService {

    @Value("${aml.service:}")
    private String amlServiceUrl;

    public AmlResult getAMLScore(String address, String addressType) {
        String url = amlServiceUrl + "/get-score";

        try {
            Map<String, String> params = new HashMap<>();
            params.put("address", address);
            params.put("addressType", addressType);
            String body = JSONUtil.toJsonStr(params);

            HttpResponse response = HttpRequest.post(url)
                    .header("Content-Type", ContentType.JSON.getValue())
                    .body(body)
                    .timeout(5000)
                    .execute();

            String result = response.body();
            log.info("AML 返回结果: {}", result);

            return JSONUtil.toBean(result, AmlResult.class);
        } catch (Exception e) {
            log.error("调用 AML 服务失败: {}", e.getMessage(), e);
            throw new RuntimeException(MessageUtils.message("aml.service.call.failed"));
        }
    }

}
