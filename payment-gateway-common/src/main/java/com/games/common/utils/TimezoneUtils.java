package com.games.common.utils;

import cn.hutool.core.util.StrUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时区工具类
 * 用于获取客户端时区信息
 * 
 * @author system
 */
public class TimezoneUtils
{
    /**
     * 默认时区
     */
    private static final String DEFAULT_TIMEZONE = "Asia/Shanghai";

    /**
     * 获取客户端时区
     * 优先从header中获取，如果没有则使用默认时区
     * 
     * @return 时区字符串，如 'Asia/Shanghai', 'America/New_York' 等
     */
    public static String getClientTimezone()
    {
        String timezone = null;
        try
        {
            HttpServletRequest request = ServletUtils.getRequest();
            if (request != null)
            {
                timezone = request.getHeader("timezone");
            }
        }
        catch (Exception e)
        {
            // 忽略异常，使用默认时区
        }
        
        if (StrUtil.isBlank(timezone))
        {
            timezone = DEFAULT_TIMEZONE;
        }
        
        // 验证时区是否有效
        if (!isValidTimezone(timezone))
        {
            timezone = DEFAULT_TIMEZONE;
        }
        
        return timezone;
    }

    /**
     * 检查时区是否有效
     * 
     * @param timezone 时区字符串
     * @return 是否有效
     */
    public static boolean isValidTimezone(String timezone)
    {
        if (StrUtil.isBlank(timezone))
        {
            return false;
        }
        
        try
        {
            TimeZone tz = TimeZone.getTimeZone(timezone);
            // 检查时区ID是否匹配，避免使用GMT时区作为fallback
            return tz.getID().equals(timezone);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 获取时区对象
     * 
     * @return TimeZone对象
     */
    public static TimeZone getClientTimeZone()
    {
        String timezone = getClientTimezone();
        return TimeZone.getTimeZone(timezone);
    }

    /**
     * 获取默认时区
     * 
     * @return 默认时区字符串
     */
    public static String getDefaultTimezone()
    {
        return DEFAULT_TIMEZONE;
    }

    /**
     * 获取客户端时区偏移量（毫秒）
     * 
     * @return 时区偏移量（毫秒）
     */
    public static int getClientTimezoneOffset()
    {
        TimeZone timeZone = getClientTimeZone();
        return timeZone.getOffset(System.currentTimeMillis());
    }

    /**
     * 获取客户端时区偏移量（小时）
     * 
     * @return 时区偏移量（小时）
     */
    public static double getClientTimezoneOffsetHours()
    {
        int offsetMillis = getClientTimezoneOffset();
        return offsetMillis / (1000.0 * 60 * 60);
    }

    /**
     * 格式化时区偏移量为字符串
     * 
     * @return 时区偏移量字符串，如 '+08:00', '-05:00' 等
     */
    public static String getClientTimezoneOffsetString()
    {
        int offsetMillis = getClientTimezoneOffset();
        int offsetHours = offsetMillis / (1000 * 60 * 60);
        int offsetMinutes = Math.abs(offsetMillis % (1000 * 60 * 60)) / (1000 * 60);
        
        String sign = offsetHours >= 0 ? "+" : "-";
        int absHours = Math.abs(offsetHours);
        
        return String.format("%s%02d:%02d", sign, absHours, offsetMinutes);
    }

    /**
     * 在指定时区中格式化日期
     * 
     * @param date 要格式化的日期
     * @param timezone 时区字符串，如 'Asia/Shanghai', 'America/New_York' 等
     * @param pattern 日期格式模式，如 'yyyy-MM-dd HH:mm:ss'
     * @return 格式化后的日期字符串
     */
    public static String formatDateInTimezone(Date date, String timezone, String pattern)
    {
        if (date == null)
        {
            return "";
        }
        
        if (StrUtil.isBlank(timezone))
        {
            timezone = DEFAULT_TIMEZONE;
        }
        
        if (StrUtil.isBlank(pattern))
        {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));
            return sdf.format(date);
        }
        catch (Exception e)
        {
            // 如果时区无效，使用默认时区
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
            return sdf.format(date);
        }
    }

    /**
     * 获取常用时区列表
     * 
     * @return 常用时区数组
     */
    public static String[] getCommonTimezones()
    {
        return new String[]{
            "Asia/Shanghai",      // 北京时间
            "Asia/Tokyo",         // 东京时间
            "Asia/Seoul",         // 首尔时间
            "Asia/Singapore",     // 新加坡时间
            "Asia/Hong_Kong",     // 香港时间
            "America/New_York",   // 纽约时间
            "America/Los_Angeles", // 洛杉矶时间
            "America/Chicago",    // 芝加哥时间
            "Europe/London",      // 伦敦时间
            "Europe/Paris",       // 巴黎时间
            "Europe/Berlin",      // 柏林时间
            "Australia/Sydney"    // 悉尼时间
        };
    }
}

