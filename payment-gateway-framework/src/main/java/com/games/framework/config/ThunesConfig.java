package com.games.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Thunes API 配置
 *
 * @author System
 * @date 2025-01-08
 */
@Data
@Component
@ConfigurationProperties(prefix = "thunes.api")
public class ThunesConfig {

    /** API 基础URL */
    private String baseUrl;

    /** API 密钥 */
    private String apiKey;

    /** API 密钥 */
    private String apiSecret;

    /** 超时时间（毫秒） */
    private Integer timeout = 30000;

    /** 重试次数 */
    private Integer retryCount = 3;

    /** API 版本 */
    private String version = "v2";

    /** Thunes 回调地址（域名） */
    private String callbackUrl;

    /** Thunes 回调路径 */
    private String callbackPath = "/api/thunes/callback";

    /**
     * 获取完整的 API 基础路径
     *
     * @return API 基础路径
     */
    public String getApiBasePath() {
        return baseUrl + "/" + version + "/money-transfer";
    }

    /**
     * 获取 Basic Auth 认证字符串
     *
     * @return Base64 编码的认证字符串
     */
    public String getBasicAuthString() {
        String credentials = apiKey + ":" + apiSecret;
        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    /**
     * 获取完整的 Thunes 回调地址
     *
     * @return 回调完整地址
     */
    public String getCallbackFullUrl() {
        if (callbackUrl == null || callbackUrl.trim().isEmpty()) {
            return null;
        }
        String normalizedBase = callbackUrl.trim();
        if (normalizedBase.endsWith("/")) {
            normalizedBase = normalizedBase.substring(0, normalizedBase.length() - 1);
        }
        String path = callbackPath == null ? "" : callbackPath.trim();
        if (path.isEmpty()) {
            return normalizedBase;
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return normalizedBase + path;
    }
}
