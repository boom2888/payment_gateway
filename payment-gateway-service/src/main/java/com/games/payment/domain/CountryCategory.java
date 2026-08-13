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
 * 欧洲国家的地区对象 country_category
 *
 * @author Ticker
 * @date 2025-07-22
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("country_category")
public class CountryCategory  {

private static final long serialVersionUID=1L;


    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 地区 */
    @Excel(name = "地区")
    private String category;

}
