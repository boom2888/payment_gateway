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
 * 风险等级配置对象 config_risk_level
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("config_risk_level")
public class ConfigRiskLevel  {

private static final long serialVersionUID=1L;


    /** 风险等级配置ID */
    @TableId(value = "id")
    private Long id;

    /** 风险等级名称："未设置" | "低" | "中" | "高" */
    @Excel(name = "风险等级名称：未设置 | 低 | 中 | 高")
    private String name;

}
