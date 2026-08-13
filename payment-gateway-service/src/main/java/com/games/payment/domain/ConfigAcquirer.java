package com.games.payment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 收单机构配置对象 config_acquirer
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("config_acquirer")
public class ConfigAcquirer  {

    /** 收单机构配置ID */
    @TableId(value = "id")
    private Long id;

    /** 收单机构名称 */
    @Excel(name = "收单机构名称", langKey = "payment.configAcquirer.excel.acquirerName")
    private String acquirerName;

    /** 类型(0-激活，1-停用) */
    @Excel(name = "类型(0-激活，1-停用)", langKey = "payment.configAcquirer.excel.status")
    private Long status;

}
