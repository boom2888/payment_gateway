package com.games.common.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * 国家代码转换工具类
 * 提供 ISO 3166-1 alpha-2 到 alpha-3 格式的转换
 * 
 * @author system
 */
@Slf4j
public class CountryCodeUtils {

    /**
     * 将国家代码转换为 ISO 3166-1 alpha-3 格式
     * 
     * @param countryCode 输入的国家代码（可能是2位或3位）
     * @return ISO 3166-1 alpha-3 格式的国家代码
     */
    public static String convertToIso3CountryCode(String countryCode) {
        if (StrUtil.isBlank(countryCode)) {
            return countryCode;
        }

        String upperCode = countryCode.toUpperCase().trim();

        // 如果已经是3位，直接返回
        if (upperCode.length() == 3) {
            return upperCode;
        }

        // 使用 Java 标准库进行转换
        try {
            Locale locale = new Locale("", upperCode);
            String iso3 = locale.getISO3Country();
            if (!iso3.trim().isEmpty()) {
                log.debug("Country code conversion: {} -> {}", upperCode, iso3);
                return iso3;
            }
        } catch (Exception e) {
            log.warn("Failed to convert country code using Locale: {}, error: {}", upperCode, e.getMessage());
        }

        // 如果标准库转换失败，返回原值
        log.warn("Country code conversion failed, returning original: {}", upperCode);
        return upperCode;
    }

    /**
     * 判断是否为美国或加拿大
     * 
     * @param countryCode 国家代码（支持2位或3位）
     * @return true 如果是美国或加拿大
     */
    public static boolean isUsOrCanada(String countryCode) {
        String iso3Code = convertToIso3CountryCode(countryCode);
        return "USA".equals(iso3Code) || "CAN".equals(iso3Code);
    }

    /**
     * 判断是否需要收款人地址信息
     * 规则：涉及 CA/US/COL/NIC 的 AFT 时强制要求
     * 
     * @param countryCode 国家代码（支持2位或3位）
     * @return true 如果需要收款人地址信息
     */
    public static boolean isRecipientAddressRequired(String countryCode) {
        String iso3Code = convertToIso3CountryCode(countryCode);
        return "USA".equals(iso3Code) ||
                "CAN".equals(iso3Code) ||
                "COL".equals(iso3Code) ||
                "NIC".equals(iso3Code);
    }

    /**
     * 判断是否为南非
     * 
     * @param countryCode 国家代码（支持2位或3位）
     * @return true 如果是南非
     */
    public static boolean isSouthAfrica(String countryCode) {
        String iso3Code = convertToIso3CountryCode(countryCode);
        return "ZAF".equals(iso3Code);
    }

    /**
     * 判断是否为美国
     * 
     * @param countryCode 国家代码（支持2位或3位）
     * @return true 如果是美国
     */
    public static boolean isUs(String countryCode) {
        String iso3Code = convertToIso3CountryCode(countryCode);
        return "USA".equals(iso3Code);
    }

    /**
     * 私有构造函数，防止实例化
     */
    private CountryCodeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}