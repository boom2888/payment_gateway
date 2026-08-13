package com.games.system.domain;

import com.games.common.annotation.Excel;
import com.games.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 参数配置表 sys_config
 * 
 * @author lor
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    @Excel(name = "参数主键", cellType = Excel.ColumnType.NUMERIC)
    private Long configId;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String configName;

    /** 参数键名 */
    @Excel(name = "参数键名")
    private String configKey;

    /** 参数键值 */
    @Excel(name = "参数键值")
    private String configValue;

    /** 系统内置（Y是 N否） */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    private String configType;

    /** 值类型（INPUT-输入框,SELECT-下拉选择） */
    @Excel(name = "值类型", readConverterExp = "INPUT=输入框,SELECT=下拉选择")
    private String valueType;

    /** 下拉选项配置（JSON格式） */
    private String selectOptions;

    @NotBlank(message = "{config.name.not.empty}")
    @Size(min = 0, max = 100, message = "{config.name.length.limit}")
    public String getConfigName()
    {
        return configName;
    }

    @NotBlank(message = "{config.key.not.empty}")
    @Size(min = 0, max = 100, message = "{config.key.length.limit}")
    public String getConfigKey()
    {
        return configKey;
    }

    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    public String getConfigValue()
    {
        return configValue;
    }
}
