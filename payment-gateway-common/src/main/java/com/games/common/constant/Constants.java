package com.games.common.constant;

import java.math.BigDecimal;

/**
 * 通用常量信息
 *
 * @author lor
 */
public class Constants {
    public static final String DEFAULT_RATE = "DEFAULT_RATE";
    public static final String PLANT_NAME = "QBSG*";
    public static final String MASTERCARD_DESCRIPTOR = "-M";
    /** IP修正开关 **/
    public static final String IP_SWITCH = "IP_SWITCH";
    /** 卡ID使用开关 **/
    public static final String USER_PAYMENT_OPTION = "USER_PAYMENT_OPTION";

    /** Token最大复用次数 **/
    public static final String MAX_TOKEN_REUSE = "MAX_TOKEN_REUSE";

    /** 卡复用TokenMapping存储的数据 **/
    public static final String MAX_MAPPING_COUNT = "MAX_MAPPING_COUNT";

    /** 7天累计支付金额限制 **/
    public static final String LIMIT_AMOUNT = "LIMIT_AMOUNT";

    /** 商户每日限额开关 **/
    public static final String SHOP_LIMIT_SWITCH = "SHOP_LIMIT_SWITCH";
    public static final String LINE = "\r\n";

    // 平台商户
    public static Long PLANT_SHOP_ID = 1L;
    public static final int ZERO = 0;

    /**
     * 3DS开关状态
     */
    public static final Long TDS_ON = 1L;
    public static final Long TDS_OFF = 0L;

    // 当前游戏期数
    public static final String NOW_TERM = "NOW_TERM";
    public static final String LAST_TOUR = "LAST_TOUR";

    public static final Long ROLE_HEHUOREN = 4L;
    public static final Long ROLE_ADMIN = 3L;

    public static final String ROLE_SHOP = "SHOP";
    public static final String EMI_ROLE_SHOP = "emiRole";

    /**
     * 商户交易记录最大回调次数
     */
    public static final String TRADE_MAX_CALLBACK_TIMES = "TRADE_MAX_CALLBACK_TIMES";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    public static final String RANDOM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    public static final String CHAIN_ID_KEY = "CHAIN_";
    // 充值到账等待时间
    public static final String CHARGE_WAIT_TIME = "CHARGE_WAIT_TIME";

    /**
     * 用户理财项目最大购买数量
     */
    public static final String FINANCE_BUY_MAX_NUM = "FINANCE_BUY_MAX_NUM";

    /**
     * 货币列表
     */
    public static final String EUR = "EUR";
    public static final String EURS = "€";
    public static final String USD = "USD";
    public static final String USDS = "$";

    /**
     * BPS基点
     */
    public static final BigDecimal BPS_BASE = BigDecimal.valueOf(10000);

    public static final String USD_EMPTY = "$    -";
    public static final String EUR_EMPTY = "€    -";

    public static final String DEFAULT_SHOP_PASSWORD = "wynpay@2025";
}
