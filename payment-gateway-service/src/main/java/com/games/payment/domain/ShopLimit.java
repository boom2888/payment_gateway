package com.games.payment.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import com.games.common.enums.SysYesNo;
import com.games.common.exception.BusinessException;
import com.games.common.utils.BigUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

/**
 * 商户限额对象 shop_limit
 *
 * @author Ticker
 * @date 2025-09-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("shop_limit")
public class ShopLimit  {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id")
    private Long id;

    /** 商户编号 */
    @Excel(name = "商户编号", langKey = "payment.limit.excel.shopId")
    private Long shopId;

    /** 周一限额 */
    @Excel(name = "周一限额", langKey = "payment.limit.excel.mondayLimit")
    private BigDecimal monday;

    /** 周二限额 */
    @Excel(name = "周二限额", langKey = "payment.limit.excel.tuesdayLimit")
    private BigDecimal tuesday;

    /** 周三限额 */
    @Excel(name = "周三限额", langKey = "payment.limit.excel.wednesdayLimit")
    private BigDecimal wednesday;

    /** 周四限额 */
    @Excel(name = "周四限额", langKey = "payment.limit.excel.thursdayLimit")
    private BigDecimal thursday;

    /** 周五限额 */
    @Excel(name = "周五限额", langKey = "payment.limit.excel.fridayLimit")
    private BigDecimal friday;

    /** 周六限额 */
    @Excel(name = "周六限额", langKey = "payment.limit.excel.saturdayLimit")
    private BigDecimal saturday;

    /** 周日限额 */
    @Excel(name = "周日限额", langKey = "payment.limit.excel.sundayLimit")
    private BigDecimal sunday;

    /** 当前值 */
    @Excel(name = "当前值", langKey = "payment.limit.excel.currentValue")
    private BigDecimal value;

    /** 状态 */
    @Excel(name = "状态", langKey = "payment.limit.excel.status", dictType="sys_yes_no")
    private String status;

    /** 创建时间 */
    @Excel(name = "创建时间", langKey = "payment.limit.excel.createTime", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /**
     * 获取当天的限额
     */
    public BigDecimal getTodayLimit() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY:    return monday;
            case TUESDAY:   return tuesday;
            case WEDNESDAY: return wednesday;
            case THURSDAY:  return thursday;
            case FRIDAY:    return friday;
            case SATURDAY:  return saturday;
            case SUNDAY:    return sunday;
            default:        return BigDecimal.ZERO;
        }
    }

    // 也可以写一个通用的方法，传入任意日期
    public BigDecimal getLimitByDate(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY:    return monday;
            case TUESDAY:   return tuesday;
            case WEDNESDAY: return wednesday;
            case THURSDAY:  return thursday;
            case FRIDAY:    return friday;
            case SATURDAY:  return saturday;
            case SUNDAY:    return sunday;
            default:        return BigDecimal.ZERO;
        }
    }

    public void checkAmount(){
        BigDecimal limit = BigUtils.safeDecimal(getTodayLimit());
        boolean isOpen = StrUtil.equals(SysYesNo.Y.name(), status);
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        if (!BigUtils.isZero(limit) && isOpen){
            if(value.compareTo(limit) > 0){
                throw new BusinessException("The merchant’s daily collection has exceeded the limit", 503);
            }
        }
    }

}
