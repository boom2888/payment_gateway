package com.games.web.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.Resource;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.common.constant.Constants;
import com.games.common.utils.StringUtils;
import com.games.common.utils.sign.Base64;
import com.games.common.utils.uuid.IdUtils;
import com.games.payment.domain.XeFromResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.redis.RedisCache;
import org.springframework.web.client.RestTemplate;

/**
 * 验证码操作处理
 * 
 * @author lor
 */
@Api(tags = "验证码")
@RestController
public class CaptchaController
{
    @Value("${xe.username:}")
    private String xeUsername;

    @Value("${xe.password:}")
    private String xePassword;
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;
    
    // 验证码类型
    @Value("${ruoyi.captchaType}")
    private String captchaType;

    /**
     * 生成验证码
     */
    @ApiOperation("获取验证码")
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }

    @GetMapping("xe")
    public AjaxResult index(@NotNull String side,
                        @NotNull String crypto,
                        @NotNull String currency,
                        @NotNull Integer quantity) {
        RestTemplate restTemplate = new RestTemplate();

        //from=CAD&to=USD,GBP&amount=1
        StringBuilder url = new StringBuilder("https://xecdapi.xe.com/v1/convert_from.json/?");
        if ("buy".equals(side)) {
            url.append("from=").append(currency).append("&to=").append(crypto);
        }else {
            url.append("from=").append(crypto).append("&to=").append(currency);
        }
        url.append("&amount=").append(quantity);
        if (StringUtils.isEmpty(xeUsername) || StringUtils.isEmpty(xePassword)) {
            return AjaxResult.error("XE service is not configured");
        }

        // 创建 Basic Auth 请求头
        String auth = xeUsername + ":" + xePassword;
        byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建请求实体（可选：添加请求体）
        HttpEntity<String> request = new HttpEntity<>("{}", headers);

        // 发起请求
        ResponseEntity<XeFromResponse> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET, // 或 POST、PUT 等
                request,
                XeFromResponse.class
        );

//        System.out.println("Response: " + response.getBody());
        XeFromResponse json = response.getBody();
        Assert.notNull(json, "No data found");
        Assert.notEmpty(json.getTo(), "No data found");
        AjaxResult ajax = AjaxResult.success();
        ajax.put("liquidityQuote", json.getTo().get(0).getMid());
        return ajax;
    }
}
