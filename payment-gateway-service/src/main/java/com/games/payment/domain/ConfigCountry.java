package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 国家配置对象 config_country
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("config_country")
public class ConfigCountry  {

private static final long serialVersionUID=1L;


    /** 两字母国家代码（ISO 3166-1 alpha-2） */
    @TableId(value = "code")
    private String code;

    /** 英文国家名称 */
    @Excel(name = "英文国家名称")
    private String name;

    /** 完整英文国家名称 */
    @Excel(name = "完整英文国家名称")
    private String fullName;

    /** 三字母国家代码（ISO 3166-1 alpha-3） */
    @Excel(name = "三字母国家代码" , readConverterExp = "I=SO,3=166-1,a=lpha-3")
    private String iso3;

    /** 三位数字国家编号（ISO 3166-1 数字） */
    @Excel(name = "三位数字国家编号" , readConverterExp = "I=SO,3=166-1,数=字")
    private String number;

    /** 大洲代码 */
    @Excel(name = "大洲代码")
    private String continentCode;

}
