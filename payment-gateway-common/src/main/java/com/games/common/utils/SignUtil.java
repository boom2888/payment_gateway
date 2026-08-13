package com.games.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import com.games.common.exception.CustomException;
import com.games.common.utils.spring.SpringUtils;
import org.springframework.core.env.Environment;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUtil {

    private static final Pattern PAN_NORMALIZE_PATTERN = Pattern.compile("[\\s-]");

    public static String sign(String sharedSecretKey, String sso_customer_id) {
        try {
            String payload = sso_customer_id;
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(sharedSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] hash = hmacSHA256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
//            System.out.println(bytesToHex(hash));
            return bytesToHex(hash);
        }catch (Exception e){
            throw new CustomException(MessageUtils.message("SIGIN_ERR"));
        }
    }

    /**
     * 生成卡号指纹（card_fingerprint），用于在不存储完整 PAN 的情况下做等值匹配。
     * 算法：HMAC-SHA256(secretKey, normalizedPan)，输出 hex 字符串。
     * secretKey 默认从 card-finger.secretKey 配置或 CardFingerConfig bean 中读取。
     */
    public static String cardFingerprint(String pan) {
        String secretKey = resolveCardFingerSecretKey();
        return cardFingerprint(pan, secretKey);
    }

    /**
     * 使用指定 secretKey 生成卡号指纹。
     */
    public static String cardFingerprint(String pan, String secretKey) {
        String normalizedPan = normalizePan(pan);
        if (normalizedPan == null || normalizedPan.isEmpty()) {
            throw new CustomException("PAN is blank");
        }
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new CustomException("card-finger.secretKey is blank");
        }
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] hash = hmacSHA256.doFinal(normalizedPan.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new CustomException(MessageUtils.message("SIGIN_ERR"));
        }
    }

    /**
     * 对 PAN 做展示脱敏：前 6 后 4，中间固定 "****"。
     * 输入可包含空格/短横线/其它非数字字符，会先去除非数字字符。
     */
    public static String maskPan(String pan) {
        if (pan == null || pan.trim().isEmpty()) {
            return "****";
        }
        String normalized = pan.replaceAll("\\D", "");
        if (normalized.length() <= 10) {
            return "****";
        }
        String prefix = normalized.substring(0, 6);
        String suffix = normalized.substring(normalized.length() - 4);
        return prefix + "****" + suffix;
    }

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static Map<String, Object> getParams(Object data) {
        Map<String, Object> logicParam = BeanUtil.beanToMap(data, false, true);
        // 遍历所有 key-value 进行处理
        logicParam.replaceAll((k, v) -> (v instanceof Date) ? DateUtil.format((Date) v, "yyyy-MM-dd HH:mm:ss") : v);
        return logicParam;
    }

    public static String convertToUrlParams(Map<String, Object> fieldMap, String baseUrl) {
        // 将对象字段转为 Map
//        Map<String, Object> fieldMap = BeanUtil.beanToMap(obj, false, true);
        // 构建 URL
        UrlBuilder urlBuilder = UrlBuilder.ofHttp(baseUrl);
        UrlQuery query = urlBuilder.getQuery();
        // 将 Map 转为 URL 参数
        fieldMap.forEach((key, value) -> {
            if (value != null) {
                query.add(key, value.toString());
            }
        });
        return urlBuilder.toString();
    }

    private static String normalizePan(String pan) {
        if (pan == null) {
            return null;
        }
        String trimmed = pan.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        return PAN_NORMALIZE_PATTERN.matcher(trimmed).replaceAll("");
    }

    private static String resolveCardFingerSecretKey() {
        try {
            Environment environment = SpringUtils.getBean(Environment.class);
            String key = environment.getProperty("card-finger.secretKey");
            if (key != null && !key.trim().isEmpty()) {
                return key.trim();
            }
        } catch (Exception ignored) {
        }
        try {
            Object configBean = SpringUtils.getBean("cardFingerConfig");
            Object key = BeanUtil.getProperty(configBean, "secretKey");
            if (key != null && !key.toString().trim().isEmpty()) {
                return key.toString().trim();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
