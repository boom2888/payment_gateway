package com.games.common.constant;

/**
 * Shift4 3DS相关常量
 * 用于3DS认证流程中的标准值
 */
public class Shift4TdsConstants {

    /**
     * 3DS发起标识值
     * 01 - 商户发起的3DS认证
     */
    public static final String TDS_INITIATE_MERCHANT = "01";

    /**
     * 3DS渠道值
     * 03 - 应用程序渠道（App-based）
     */
    public static final String TDS_CHANNEL_APP = "03";

    /**
     * 3DS交易类型 - 商品/服务购买
     * 01 - Goods / Service purchase
     */
    public static final String TDS_TRANSTYPE_GOODS_SERVICE = "01";

    /**
     * 3DS挑战窗口大小默认值
     * 04 - 600x400
     */
    public static final String TDS_CHALLENGE_WINDOW_SIZE_DEFAULT = "04";

    /**
     * 浏览器Accept头默认值
     */
    public static final String TDS_BROWSER_ACCEPT_HEADER_DEFAULT = "text/html,application/json";

    /**
     * 浏览器时区默认值（UTC）
     */
    public static final String TDS_BROWSER_TZ_DEFAULT = "0";

    /**
     * 浏览器颜色深度默认值
     */
    public static final String TDS_BROWSER_COLOR_DEPTH_DEFAULT = "24";

    /**
     * 浏览器屏幕宽度默认值
     */
    public static final String TDS_BROWSER_SCREEN_WIDTH_DEFAULT = "1920";

    /**
     * 浏览器屏幕高度默认值
     */
    public static final String TDS_BROWSER_SCREEN_HEIGHT_DEFAULT = "1080";

    /**
     * 浏览器Java启用默认值
     */
    public static final String TDS_BROWSER_JAVA_ENABLED_DEFAULT = "false";

    /**
     * 浏览器User-Agent默认值
     */
    public static final String TDS_BROWSER_USER_AGENT_DEFAULT = "Mozilla/5.0";

    /**
     * 浏览器语言默认值
     */
    public static final String TDS_BROWSER_LANGUAGE_DEFAULT = "en-US";

    /**
     * 3DS完成指示器 - 设备指纹收集成功（10秒内完成）
     */
    public static final String TDS_COMPIND_YES = "Y";

    /**
     * 3DS完成指示器 - 设备指纹收集超时或失败
     */
    public static final String TDS_COMPIND_NO = "N";

    /**
     * 私有构造函数，防止实例化
     */
    private Shift4TdsConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
}
