package com.games.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 卡号工具类
 * 提供卡号脱敏、规范化等通用功能
 *
 * @author Auto
 * @date 2025-01-XX
 */
public class CardNumberUtils {

    /**
     * 脱敏卡号格式的正则表达式：前六位 + 中间6-9位（数字或*）+ 后四位
     */
    private static final Pattern MASKED_CARD_PATTERN = Pattern.compile("^(\\d{6})([\\d\\*]{6,9})(\\d{4})$");

    /**
     * 提取卡号中的纯数字
     *
     * @param pan 卡号（可能包含空格、短横线等）
     * @return 纯数字字符串，如果输入为空则返回空字符串
     */
    public static String normalizeDigits(String pan) {
        if (StringUtils.isBlank(pan)) {
            return "";
        }
        return pan.replaceAll("\\D", "");
    }

    /**
     * 判断是否为完整卡号（16-19位数字）
     *
     * @param pan 卡号
     * @return 如果是完整卡号返回true，否则返回false
     */
    public static boolean isFullCardNumber(String pan) {
        if (StringUtils.isBlank(pan)) {
            return false;
        }
        String digits = normalizeDigits(pan);
        return digits.matches("\\d{16,19}");
    }

    /**
     * 对卡号进行脱敏处理，转换为"前六****后四"格式
     * 如果卡号已经是脱敏格式或为空，则直接返回原值
     * 主要用于日志记录等场景，不需要规范化已脱敏的格式
     *
     * @param cardNumber 卡号
     * @return 脱敏后的卡号，如果输入为空或不是完整卡号则返回原值
     */
    public static String maskCardNumber(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return cardNumber;
        }
        // 提取纯数字
        String digits = normalizeDigits(cardNumber);
        // 如果是完整卡号（16-19位数字），进行脱敏
        if (digits.matches("\\d{16,19}")) {
            return SignUtil.maskPan(digits);
        }
        // 如果已经是脱敏格式或其他格式，直接返回原值
        return cardNumber;
    }

    /**
     * 将输入卡号（完整或脱敏）规范化为可落库/可查询的"前六****后四"格式
     * 用于数据库存储，会规范化已脱敏的格式
     *
     * @param pan 卡号（完整卡号或脱敏格式）
     * @return 规范化后的脱敏格式（"前六****后四"），不可识别时返回空字符串
     */
    public static String normalizeMaskedPanForStorage(String pan) {
        if (StringUtils.isBlank(pan)) {
            return "";
        }
        String trimmed = pan.trim();
        String digits = normalizeDigits(trimmed);
        if (digits.matches("\\d{16,19}")) {
            // 完整卡号，转换为脱敏格式
            return SignUtil.maskPan(digits);
        }
        // 尝试匹配已脱敏的格式（前六****后四）
        Matcher matcher = MASKED_CARD_PATTERN.matcher(trimmed.replaceAll("[\\s-]+", ""));
        if (matcher.matches()) {
            // 规范化已脱敏的格式，确保格式统一
            return matcher.group(1) + "****" + matcher.group(3);
        }
        return "";
    }

    /**
     * 确保卡号是脱敏格式（安全措施）
     * 如果发现是完整卡号，则转换为脱敏格式
     *
     * @param cardNumber 卡号
     * @return 脱敏后的卡号，如果输入为空或不是完整卡号则返回原值
     */
    public static String ensureMasked(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return cardNumber;
        }
        String digits = normalizeDigits(cardNumber);
        // 如果发现是完整卡号（16-19位数字），转换为脱敏格式
        if (digits.matches("\\d{16,19}")) {
            return SignUtil.maskPan(digits);
        }
        // 如果已经是脱敏格式或其他格式，直接返回原值
        return cardNumber;
    }
}

