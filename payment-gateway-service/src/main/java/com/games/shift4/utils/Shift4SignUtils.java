package com.games.shift4.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.games.common.exception.Shift4Exception;
import com.games.shift4.vo.Shift4ReqVo;
import com.games.shift4.vo.Shift4ResVo;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Shift4 Payment Gateway 签名工具类
 * 根据API规范计算SHA256签名
 */
@Slf4j
public class Shift4SignUtils {

    /**
     * 计算请求签名
     * 
     * @param params       请求参数Map（不包含K参数）
     * @param signatureKey 商户签名密钥
     * @return SHA256签名字符串（64字符）
     */
    public static String calculateSignature(Map<String, String> params, String signatureKey) {
        try {
            // 1. 移除 K 参数（如果存在）
            Map<String, String> signParams = new TreeMap<>(params);
            signParams.remove("K");

            // 2. 按参数名字母顺序排序并拼接 value（TreeMap 自动按 key 排序）
            StringBuilder signString = new StringBuilder();
            for (String value : signParams.values()) {
                if (value == null) {
                    value = "";
                }

                // 替换特殊字符 < > " ' ( ) \ 为空格
                value = value.replaceAll("[<>\"'()\\\\]", " ");

                // 去除首尾空格
                value = value.trim();

                signString.append(value);
            }

            // 3. 追加签名密钥
            signString.append(signatureKey);

            // 4. 计算 SHA256 哈希
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(signString.toString().getBytes(StandardCharsets.UTF_8));

            // 5. 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 算法不可用", e);
            throw new Shift4Exception("SHA-256 算法不可用", e);
        }
    }

    /**
     * 验证响应签名
     * 
     * @param responseParams 响应参数Map
     * @param signatureKey   商户签名密钥
     * @return 签名是否有效
     */
    public static boolean verifySignature(Map<String, String> responseParams, String signatureKey) {
        String receivedSignature = responseParams.get("K");
        if (StrUtil.isBlank(receivedSignature)) {
            return false;
        }

        // 移除响应中的K参数后计算签名
        Map<String, String> paramsWithoutK = new HashMap<>(responseParams);
        paramsWithoutK.remove("K");

        String calculatedSignature = calculateSignature(paramsWithoutK, signatureKey);

        return receivedSignature.equalsIgnoreCase(calculatedSignature);
    }

    /**
     * 将Map转换为URL编码的表单字符串
     */
    public static String toFormUrlEncoded(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> {
                    try {
                        String key = java.net.URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
                        String value = java.net.URLEncoder.encode(
                                entry.getValue() != null ? entry.getValue() : "",
                                StandardCharsets.UTF_8.toString());
                        return key + "=" + value;
                    } catch (Exception e) {
                        log.error("URL编码失败: {}", entry, e);
                        return "";
                    }
                })
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.joining("&"));
    }

    /**
     * 将Map转换为JSON字符串
     * 用于日志记录，使请求和响应参数更易读
     *
     * @param params 参数Map
     * @return JSON格式字符串
     */
    public static String toJson(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "{}";
        }
        return JSONUtil.toJsonStr(params);
    }

    /**
     * 基于 SaleReqVo 构造参数并生成 URL 编码表单字符串（包含签名计算）
     * <p>
     * - 使用字段名或 @JsonProperty 作为参数名<br>
     * - 使用 {@code signatureKey} 计算签名，并写入参数 K
     *
     * @param vo           请求对象
     * @param signatureKey 签名密钥（Shift4Config.signatureKey）
     * @return application/x-www-form-urlencoded 格式字符串
     */
    public static String toFormUrlEncoded(Shift4ReqVo vo, String signatureKey) {
        if (vo == null) {
            throw new IllegalArgumentException("SaleReqVo 不能为空");
        }
        if (StrUtil.isBlank(signatureKey)) {
            throw new IllegalArgumentException("signatureKey(认证密钥)不能为空");
        }

        Map<String, String> params = buildParamsFromSaleReq(vo);

        // K 不参与签名串，由 signatureKey 计算得到
        params.remove("K");
        String signature = calculateSignature(params, signatureKey);
        params.put("K", signature);

        return toFormUrlEncoded(params);
    }

    /**
     * 将 SaleReqVo 转为参数 Map（不做签名计算）
     */
    private static Map<String, String> buildParamsFromSaleReq(Shift4ReqVo vo) {
        Map<String, String> params = new HashMap<>();
        if (vo == null) {
            return params;
        }

        Field[] fields = Shift4ReqVo.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(vo);
            } catch (IllegalAccessException e) {
                log.warn("读取 SaleReqVo 字段失败: {}", field.getName(), e);
                continue;
            }
            if (value == null) {
                continue;
            }

            String paramName;
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null && StrUtil.isNotBlank(jsonProperty.value())) {
                paramName = jsonProperty.value();
            } else {
                paramName = field.getName();
            }

            params.put(paramName, String.valueOf(value));
        }

        return params;
    }

    /**
     * 将 Map 响应转换为 Shift4ResVo 对象
     * 使用反射根据 @JsonProperty 注解映射字段
     *
     * @param responseMap API响应的Map
     * @return Shift4ResVo 对象
     */
    public static Shift4ResVo toResVo(Map<String, String> responseMap) {
        if (responseMap == null || responseMap.isEmpty()) {
            return new Shift4ResVo();
        }

        Shift4ResVo resVo = new Shift4ResVo();
        Field[] fields = Shift4ResVo.class.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // 获取 @JsonProperty 注解的值作为参数名
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            String paramName = (jsonProperty != null) ? jsonProperty.value() : field.getName();

            String value = responseMap.get(paramName);
            if (StrUtil.isNotBlank(value)) {
                try {
                    field.setAccessible(true);
                    field.set(resVo, value);
                } catch (IllegalAccessException e) {
                    log.error("设置响应字段失败: field={}, value={}", paramName, value, e);
                }
            }
        }

        return resVo;
    }
}
