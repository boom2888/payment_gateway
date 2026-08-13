package com.games.common.constant;

/**
 * Thunes 相关常量
 *
 * @author System
 * @date 2025-01-08
 */
public class ThunesConstants {

    /** API 版本 */
    public static final String API_VERSION = "v2";

    /** API 基础路径 */
    public static final String API_BASE_PATH = "/" + API_VERSION + "/money-transfer";

    /** 报价模式 */
    public static final String MODE_SOURCE_AMOUNT = "SOURCE_AMOUNT";
    public static final String MODE_DESTINATION_AMOUNT = "DESTINATION_AMOUNT";

    /** 交易类型 */
    public static final String TRANSACTION_TYPE_C2C = "C2C";
    public static final String TRANSACTION_TYPE_B2C = "B2C";
    public static final String TRANSACTION_TYPE_C2B = "C2B";
    public static final String TRANSACTION_TYPE_B2B = "B2B";

    /** API 端点 */
    public static final String ENDPOINT_PAYERS = "/payers";
    public static final String ENDPOINT_QUOTATIONS = "/quotations";
    public static final String ENDPOINT_TRANSACTIONS = "/transactions";
    public static final String ENDPOINT_CREDIT_PARTY_VERIFICATION = "/credit-party-verification";
    public static final String ENDPOINT_CREDIT_PARTY_INFORMATION = "/credit-party-information";

    /** HTTP 头部 */
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE_JSON = "application/json";

    /** 认证类型 */
    public static final String AUTH_TYPE_BASIC = "Basic";

    /** 默认超时时间（毫秒） */
    public static final int DEFAULT_TIMEOUT = 30000;

    /** 默认重试次数 */
    public static final int DEFAULT_RETRY_COUNT = 3;

    /** 固定源国家 - 马耳他（根据公司实体注册地） */
    public static final String FIXED_SOURCE_COUNTRY = "MLT";
    
    /** 固定源货币 - 欧元 */
    public static final String FIXED_SOURCE_CURRENCY = "EUR";
}