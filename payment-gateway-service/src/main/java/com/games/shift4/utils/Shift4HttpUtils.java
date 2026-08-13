package com.games.shift4.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.games.shift4.vo.Shift4ReqVo;
import com.games.shift4.vo.Shift4ResVo;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Shift4 HTTP 工具类
 * 提供基于 {@link Shift4ReqVo} 的通用请求方法
 */
@Slf4j
public class Shift4HttpUtils {

    private Shift4HttpUtils() {
    }

    /**
     * 执行 HTTP 请求（带签名）
     * <p>
     * - 使用 {@code signatureKey} 计算签名，并写入参数 K<br>
     * - 请求体为 application/x-www-form-urlencoded，返回值为 Shift4ResVo 对象
     *
     * @param url          Shift4 API 地址
     * @param params       请求参数（不包含签名）
     * @param signatureKey 签名密钥
     * @param logPrefix    日志前缀（可为空）
     * @return Shift4ResVo 响应对象
     * @throws IllegalArgumentException 当 URL、签名密钥或参数 Map 为空时
     */
    public static Shift4ResVo executeRequest(String url,
            Map<String, String> params,
            String signatureKey,
            String logPrefix) {
        // 参数验证
        if (StrUtil.isBlank(url)) {
            throw new IllegalArgumentException("请求 URL 不能为空");
        }
        if (StrUtil.isBlank(signatureKey)) {
            throw new IllegalArgumentException("signatureKey(签名密钥)不能为空");
        }
        if (params == null) {
            throw new IllegalArgumentException("请求参数 Map 不能为空");
        }

        // 计算签名并添加到参数中
        Map<String, String> signedParams = new java.util.HashMap<>(params);
        String signature = Shift4SignUtils.calculateSignature(signedParams, signatureKey);
        signedParams.put("K", signature);

        // 构建请求体
        String requestBody = Shift4SignUtils.toFormUrlEncoded(signedParams);

        // 日志前缀
        String prefix = StrUtil.isNotBlank(logPrefix) ? logPrefix : "Shift4请求";
        log.info("{} - 发送请求", prefix);

        // 发送 HTTP POST 请求
        HttpResponse response = HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(requestBody)
                .timeout(60000)
                .execute();

        String responseBody = response.body();
        log.info("{} - 收到响应", prefix);

        // 解析响应为 Map，再转换为 Shift4ResVo
        Map<String, String> responseMap = parseResponse(responseBody);
        return Shift4SignUtils.toResVo(responseMap);
    }

    /**
     * 执行 HTTP 请求（带签名）- 返回原始 Map
     * <p>
     * 与 {@link #executeRequest} 相同，但返回原始 Map 而非 Shift4ResVo。
     * 用于需要访问原始响应数据进行签名验证、日志记录等场景。
     *
     * @param url          Shift4 API 地址
     * @param params       请求参数（不包含签名）
     * @param signatureKey 签名密钥
     * @param logPrefix    日志前缀（可为空）
     * @return 原始响应参数 Map
     */
    public static Map<String, String> executeRequestRaw(String url,
            Map<String, String> params,
            String signatureKey,
            String logPrefix) {
        // 参数验证
        if (StrUtil.isBlank(url)) {
            throw new IllegalArgumentException("请求 URL 不能为空");
        }
        if (StrUtil.isBlank(signatureKey)) {
            throw new IllegalArgumentException("signatureKey(签名密钥)不能为空");
        }
        if (params == null) {
            throw new IllegalArgumentException("请求参数 Map 不能为空");
        }

        // 计算签名并添加到参数中
        Map<String, String> signedParams = new java.util.HashMap<>(params);
        String signature = Shift4SignUtils.calculateSignature(signedParams, signatureKey);
        signedParams.put("K", signature);

        // 构建请求体
        String requestBody = Shift4SignUtils.toFormUrlEncoded(signedParams);

        // 日志前缀
        String prefix = StrUtil.isNotBlank(logPrefix) ? logPrefix : "Shift4请求";
        log.info("{} - 发送请求", prefix);

        // 发送 HTTP POST 请求
        HttpResponse response = HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(requestBody)
                .timeout(60000)
                .execute();

        String responseBody = response.body();
        log.info("{} - 收到响应", prefix);

        // 返回原始 Map
        return parseResponse(responseBody);
    }

    /**
     * 使用 SaleReqVo 构造请求并发送 HTTP POST 请求
     * <p>
     * - 根据字段及 @JsonProperty 注解自动生成参数名<br>
     * - 使用 {@code signatureKey} 计算签名，并写入参数 K<br>
     * - 请求体为 application/x-www-form-urlencoded，返回值为 Shift4ResVo 对象
     *
     * @param url          Shift4 接口地址
     * @param signatureKey 配置中的认证密钥（{@code Shift4Config.signatureKey}）
     * @param request      请求对象 {@link Shift4ReqVo}
     * @param logPrefix    日志前缀（可为空）
     * @return Shift4ResVo 响应对象
     */
    public static Shift4ResVo executeSaleRequest(String url,
            String signatureKey,
            Shift4ReqVo request,
            String logPrefix) {
        if (request == null) {
            throw new IllegalArgumentException("SaleReqVo 不能为空");
        }
        if (StrUtil.isBlank(url)) {
            throw new IllegalArgumentException("请求 URL 不能为空");
        }
        if (StrUtil.isBlank(signatureKey)) {
            throw new IllegalArgumentException("signatureKey(认证密钥)不能为空");
        }

        String body = buildSaleRequestBody(signatureKey, request);
        String prefix = StrUtil.isNotBlank(logPrefix) ? logPrefix : "Shift4通用请求";
        log.info("{} - 发送请求", prefix);

        HttpResponse response = HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(body)
                .timeout(60000)
                .execute();

        String responseBody = response.body();
        log.info("{} - 收到响应", prefix);

        Map<String, String> responseMap = parseResponse(responseBody);
        return Shift4SignUtils.toResVo(responseMap);
    }

    /**
     * 使用 SaleReqVo 构造请求并发送 HTTP POST 请求（无日志前缀）
     */
    public static Shift4ResVo executeSaleRequest(String url,
            String signatureKey,
            Shift4ReqVo request) {
        return executeSaleRequest(url, signatureKey, request, null);
    }

    /**
     * 构建 SaleReqVo 对应的表单请求体（不实际发送 HTTP）
     */
    static String buildSaleRequestBody(String signatureKey, Shift4ReqVo request) {
        return Shift4SignUtils.toFormUrlEncoded(request, signatureKey);
    }

    /**
     * 解析 URL-encoded 响应为 Map
     */
    public static Map<String, String> parseResponse(String responseBody) {
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isBlank(responseBody)) {
            return params;
        }

        String[] pairs = responseBody.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                    params.put(key, value);
                } catch (Exception e) {
                    log.warn("解析响应参数失败: {}", pair, e);
                }
            }
        }

        return params;
    }
}
