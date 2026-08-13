package com.games.common.utils;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 国家信息工具类
 * 提供完整的国家列表，包括代码、名称、国旗等信息
 *
 * @author System
 * @date 2026-02-05
 */
public class CountryUtils {

    /**
     * 国家信息DTO
     */
    @Data
    public static class CountryInfo {
        /** ISO 3166-1 alpha-3 国家代码（3位字母，Thunes API要求） */
        private String code;
        /** ISO 3166-1 alpha-2 国家代码（2位字母） */
        private String code2;
        /** 中文名称 */
        private String nameCn;
        /** 英文名称 */
        private String nameEn;
        /** 国旗emoji（Unicode） */
        private String flag;
    }

    /**
     * 获取所有国家列表
     * 按英文名称排序
     *
     * @return 国家信息列表
     */
    public static List<CountryInfo> getAllCountries() {
        List<CountryInfo> countries = new ArrayList<>();

        // 获取所有ISO国家代码
        String[] countryCodes = Locale.getISOCountries();

        for (String alpha2Code : countryCodes) {
            Locale locale = new Locale("", alpha2Code);
            CountryInfo country = new CountryInfo();

            // 转换为alpha-3格式（Thunes API要求）
            country.setCode(locale.getISO3Country());
            country.setCode2(alpha2Code);
            country.setNameCn(locale.getDisplayCountry(Locale.SIMPLIFIED_CHINESE));
            country.setNameEn(locale.getDisplayCountry(Locale.ENGLISH));
            country.setFlag(getFlagEmoji(alpha2Code));

            countries.add(country);
        }

        // 按英文名称排序
        countries.sort(Comparator.comparing(CountryInfo::getNameEn));

        return countries;
    }

    /**
     * 根据国家代码获取国旗emoji
     * 原理：将ISO 3166-1 alpha-2代码转换为区域指示符号（Regional Indicator Symbols）
     *
     * @param countryCode ISO 3166-1 alpha-2 国家代码
     * @return 国旗emoji字符串
     */
    public static String getFlagEmoji(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) {
            return "";
        }

        countryCode = countryCode.toUpperCase();

        // 将字母转换为区域指示符号
        // A-Z 对应 Unicode 0x1F1E6-0x1F1FF
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;

        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

    /**
     * 根据国家代码获取国家信息
     *
     * @param countryCode ISO 3166-1 alpha-3 国家代码（3位字母）
     * @return 国家信息，如果不存在返回null
     */
    public static CountryInfo getCountryByCode(String countryCode) {
        if (countryCode == null || countryCode.length() != 3) {
            return null;
        }

        return getAllCountries().stream()
                .filter(c -> c.getCode().equalsIgnoreCase(countryCode))
                .findFirst()
                .orElse(null);
    }

    /**
     * 搜索国家（支持代码、中文名、英文名）
     *
     * @param keyword 搜索关键词
     * @return 匹配的国家列表
     */
    public static List<CountryInfo> searchCountries(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCountries();
        }

        String lowerKeyword = keyword.toLowerCase();

        return getAllCountries().stream()
                .filter(c -> c.getCode().toLowerCase().contains(lowerKeyword) ||
                        c.getNameCn().contains(keyword) ||
                        c.getNameEn().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * 私有构造函数，防止实例化
     */
    private CountryUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
