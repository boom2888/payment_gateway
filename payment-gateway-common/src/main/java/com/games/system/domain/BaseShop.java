package com.games.system.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseShop {
    /**
     * 用户名
     */
    @TableField(exist = false)
//    @Excel(name = "用户名")
    private String memberName;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();

    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createName;

    /**
     * 更新人名称
     */
    @TableField(exist = false)
    private String updateName;
}
