package com.games.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具类
 * 
 * @author lor
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYY_MM_DD2 = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String DD_HH_MM_SS = "dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    public static Long getNowDateTime() {
        // Long todayZeroTime = LocalDate.now()
        // .atStartOfDay(ZoneOffset.ofHours(8))
        // .toInstant()
        // .toEpochMilli();
        Long todayZeroTime = Long.parseLong(dateTimeNow(YYYY_MM_DD2));
        return todayZeroTime;
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd（使用北京时间UTC+8）
     * 
     * @return String
     */
    public static String getDate() {
        // 使用北京时间获取当前日期
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        return today.format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        // 使用北京时间获取当前时间
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return now.format(DateTimeFormatter.ofPattern(format));
    }

    public static Long getDaySpaceTime(Date thisDayDate, int days) {
        Date yesterday = DateUtils.getDateAfter(thisDayDate, days, Calendar.DAY_OF_MONTH);
        String date = parseDateToStr("yyyyMMdd", yesterday);
        Long todayZeroTime = Long.parseLong(date);
        return todayZeroTime;
    }

    public static final String dateTime(final Date date) {
        // 使用北京时间格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(date);
    }

    public static final Date day2Date(Long ymdTime) {
        return dateTime(YYYY_MM_DD2, ymdTime.toString());
    }

    public static final String parseDateToStr(final String format, final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + MessageUtils.message("date.format.days.hours.minutes").split(" ")[0] + hour
                + MessageUtils.message("date.format.days.hours.minutes").split(" ")[1] + min
                + MessageUtils.message("date.format.days.hours.minutes").split(" ")[2];
    }

    public static long[] getCurrentWeekTimeFrame() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // start of the week
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        calendar.add(Calendar.DAY_OF_WEEK, -(calendar.get(Calendar.DAY_OF_WEEK) - 2));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startTime = calendar.getTimeInMillis();
        // end of the week
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();
        return new long[] { startTime, endTime };
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getDateAfter(Date date, int num, int flag) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(flag, num);
        return instance.getTime();
    }

    public static Date getTodayDate(boolean start) {
        // 获取今日日期的开始时间（使用北京时间UTC+8）
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        LocalDateTime startOfDay = LocalDateTime.of(today, start ? LocalTime.MIDNIGHT : LocalTime.MAX);
        return Date.from(startOfDay.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    public static Date getYesterdayDate(boolean start) {
        // 获取昨日日期的开始时间（使用北京时间UTC+8）
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Shanghai")).minusDays(1);
        LocalDateTime startOfYesterday = LocalDateTime.of(yesterday, start ? LocalTime.MIDNIGHT : LocalTime.MAX);
        return Date.from(startOfYesterday.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    /**
     * 获取本月开始时间（使用北京时间UTC+8）
     */
    public static Date getStartOfMonth() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        LocalDateTime startOfMonth = LocalDateTime.of(today.withDayOfMonth(1), LocalTime.MIDNIGHT);
        return Date.from(startOfMonth.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    /**
     * 获取本月结束时间（使用北京时间UTC+8）
     */
    public static Date getEndOfMonth() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());
        LocalDateTime endOfMonth = LocalDateTime.of(lastDay, LocalTime.MAX);
        return Date.from(endOfMonth.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    /**
     * 获取上月开始时间（使用北京时间UTC+8）
     */
    public static Date getStartOfLastMonth() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDateTime startOfLastMonth = LocalDateTime.of(firstDayOfLastMonth, LocalTime.MIDNIGHT);
        return Date.from(startOfLastMonth.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    /**
     * 获取上月结束时间（使用北京时间UTC+8）
     */
    public static Date getEndOfLastMonth() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth());
        LocalDateTime endOfLastMonth = LocalDateTime.of(lastDayOfLastMonth, LocalTime.MAX);
        return Date.from(endOfLastMonth.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    /**
     * 获取本周开始时间
     */
    public static Date getStartOfWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDateTime startOfWeekTime = LocalDateTime.of(startOfWeek, LocalTime.MIDNIGHT);
        return Date.from(startOfWeekTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取本周结束时间
     */
    public static Date getEndOfWeek() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        LocalDateTime endOfWeekTime = LocalDateTime.of(endOfWeek, LocalTime.MAX);
        return Date.from(endOfWeekTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取上周开始时间
     */
    public static Date getStartOfLastWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfLastWeek = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDateTime startOfLastWeekTime = LocalDateTime.of(startOfLastWeek, LocalTime.MIDNIGHT);
        return Date.from(startOfLastWeekTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取上周结束时间
     */
    public static Date getEndOfLastWeek() {
        LocalDate today = LocalDate.now();
        LocalDate endOfLastWeek = today.minusWeeks(1).with(DayOfWeek.SUNDAY);
        LocalDateTime endOfLastWeekTime = LocalDateTime.of(endOfLastWeek, LocalTime.MAX);
        return Date.from(endOfLastWeekTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getNowDateWithISO8601Format() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.of("+00:00"));
        // 格式化成 ISO 8601
        return now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static Timestamp iso8601ToTimestamp(String isoTime) {
        try {
            Instant instant = Instant.parse(isoTime); // 自动解析 Z 或 +08:00
            return Timestamp.from(instant);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid ISO-8601 time format: " + isoTime, e);
        }
    }

    /**
     * Timestamp -> ISO8601 字符串（UTC，结尾带 Z）
     */
    public static String timestampToIso8601(Timestamp ts) {
        Instant instant = ts.toInstant();
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }

    public static Date fromJava8LocalDateToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        return Date.from(zdt.toInstant());
    }

    /**
     * 计算两个时间之间的分钟差
     * 
     * @param start 开始时间
     * @param end   结束时间
     * @return 相差的分钟数（可能为负数）
     */
    public static long getMinutesBetween(Date start, Date end) {
        long diffMillis = end.getTime() - start.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffMillis);
    }

    /**
     * UTC时间转为UTC+8（北京时间）
     * 
     * @param utcDate 输入UTC Date
     * @return 转换后的UTC+8 Date
     */
    public static Date convertUTCToUTC8(Date utcDate) {
        if (utcDate == null)
            return null;

        // 将 Date 转换成 Instant（时间戳）
        Instant instant = utcDate.toInstant();

        // 使用 UTC+8 时区
        ZonedDateTime utc8DateTime = instant.atZone(ZoneId.of("UTC+8"));

        // 再转换回 Date
        return Date.from(utc8DateTime.toInstant());
    }

    /**
     * 将时区字符串转换为分钟数格式
     * 用于 Shift4 3DS API 的 3ds_browsertz 参数
     * 
     * 支持的输入格式：
     * - "+08", "-05" → 转换为分钟数
     * - "GMT+8", "UTC-5" → 转换为分钟数
     * - "480", "-300" → 已经是分钟数，直接返回
     * 
     * @param timezone     时区字符串
     * @param defaultValue 解析失败时的默认值
     * @return 分钟数字符串
     */
    public static String convertTimezoneToMinutes(String timezone, String defaultValue) {
        if (timezone == null || timezone.trim().isEmpty()) {
            return defaultValue;
        }

        String tz = timezone.trim().toUpperCase();

        // 移除 GMT/UTC 前缀
        tz = tz.replace("GMT", "").replace("UTC", "").trim();

        // 如果已经是纯数字（可能带负号），直接返回
        if (tz.matches("^-?\\d+$")) {
            return tz;
        }

        // 解析 +08 或 -05 格式
        try {
            int sign = 1;
            if (tz.startsWith("+")) {
                tz = tz.substring(1);
            } else if (tz.startsWith("-")) {
                sign = -1;
                tz = tz.substring(1);
            }

            int hours = Integer.parseInt(tz);
            // 转换为分钟数
            int minutes = sign * hours * 60;
            return String.valueOf(minutes);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
