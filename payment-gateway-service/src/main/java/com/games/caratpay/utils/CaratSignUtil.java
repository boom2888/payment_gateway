package com.games.caratpay.utils;

import cn.hutool.core.util.StrUtil;
import com.games.caratpay.CaratPayConfig;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
public class CaratSignUtil {

    /**
     * 签名，
     */
    public static String getSignResult(Map<String, Object> rawMap, CaratPayConfig caratPayConfig) {
        try {
            Map<String, String> signMap = toSignMap(rawMap, Collections.singleton("sign"));
            return buildAndSign(signMap, caratPayConfig.getMerchantKey());
        } catch (Exception e) {
            log.error("验签失败: {}", e.getMessage());
            return null;
        }
    }


    private static String buildAndSign(Map<String, String> params, String key) throws Exception {
        String stringA = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey().replaceAll("\\s+", "") + "=" + entry.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        String stringSignTemp = stringA + "&key=" + key;
        return md5(stringSignTemp).toUpperCase();
    }


    /**
     * MD5加密工具方法
     */
    private static String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static Map<String, String> toSignMap(Map<String, Object> rawMap, Set<String> excludeKeys) {
        Map<String, String> signMap = new LinkedHashMap<>();
        if (rawMap == null) return signMap;
        for (Map.Entry<String, Object> e : rawMap.entrySet()) {
            String key = e.getKey();
            if (key == null) continue;
            if (excludeKeys != null && excludeKeys.stream().anyMatch(ex -> ex.equalsIgnoreCase(key))) continue;
            String val = getNonBlankString(e.getValue());
            if (val == null) continue;
            signMap.put(key, val);
        }
        return signMap;
    }

    private static String getNonBlankString(Object o) {
        if (o == null) return null;
        String s = String.valueOf(o);
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    public static Map<String, String> parseQueryParams(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isBlank(queryString)) {
            return params;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                // URL 解码
                try {
                    value = java.net.URLDecoder.decode(value, "UTF-8");
                } catch (Exception e) {
                    log.warn("URL解码失败: {}", value, e);
                }
                params.put(key, value);
            }
        }
        return params;
    }

    /**
     * 验证回调签名
     */
    public static boolean verifyCallbackSign(Map<String, String> params, String receivedSign, CaratPayConfig caratPayConfig) {
        try {
            // 排除 sign 参数，按 key 字典序排序
            Map<String, String> signParams = new TreeMap<>(params);
            signParams.remove("sign");

            // 构建签名字符串
            StringBuilder signStr = new StringBuilder();
            for (Map.Entry<String, String> entry : signParams.entrySet()) {
                if (StrUtil.isNotBlank(entry.getValue())) { // 只包含非空值
                    if (signStr.length() > 0) {
                        signStr.append("&");
                    }
                    signStr.append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            signStr.append("&key=").append(caratPayConfig.getMerchantKey());

            // 计算 MD5 签名
            String calculatedSign = md5(signStr.toString()).toUpperCase();

            log.debug("CaratPay回调验签 - 原字符串: {}", signStr);
            log.debug("CaratPay回调验签 - 计算签名: {}, 接收签名: {}", calculatedSign, receivedSign);

            return calculatedSign.equals(receivedSign);
        } catch (Exception e) {
            log.error("CaratPay回调验签异常", e);
            return false;
        }
    }

}
