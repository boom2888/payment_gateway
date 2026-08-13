package com.games.common.utils;

/**
 * 语言工具类
 * 
 * @author lor
 */
public class LangUtils
{

    /**
     * 判断字符串是否包含中文
     * @param str 字符串
     * @return 是否包含中文
     */
    public static boolean containsChinese(String str) {
        return str.matches(".*[\u4e00-\u9fa5]+.*");
    }

}
