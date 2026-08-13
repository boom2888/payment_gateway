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
 * 证件类型配置对象 config_document_type
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("config_document_type")
public class ConfigDocumentType  {

private static final long serialVersionUID=1L;


    /** 证件类型代码 */
    @TableId(value = "code")
    private String code;

    /** 证件描述：地址证明 */
    @Excel(name = "证件描述：地址证明")
    private String description;

    /** 类型（0-激活，1-停用） */
    @Excel(name = "类型" , readConverterExp = "0=-激活，1-停用")
    private Long status;

}
