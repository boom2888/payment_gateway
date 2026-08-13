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
 * 订阅模型对象 subscription_model
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("subscription_model")
public class SubscriptionModel  {

private static final long serialVersionUID=1L;


    /** 订阅模型 */
    @TableId(value = "id")
    private Long id;

    /** SaaS用户ID */
    @Excel(name = "SaaS用户ID")
    private Long saasUserCorporationId;

    /** 订阅模型名称 */
    @Excel(name = "订阅模型名称")
    private String modelName;

    /** 订阅模型备注 */
    @Excel(name = "订阅模型备注")
    private String memo;

    /** 客户类型(0-个人，1-企业) */
    @Excel(name = "客户类型(0-个人，1-企业)")
    private Long customerType;

    /** 订阅级别(默认，高级) */
    @Excel(name = "订阅级别(默认，高级)")
    private Long subscriptionLevelId;

    /** 货币ID */
    @Excel(name = "货币ID")
    private Long currencyId;

    /** 默认类型(0-默认，1-可选) */
    @TableField("default")
    private Long default1;

    public Long getDefault() {
        return default1;
    }

    public void setDefault(Long defaultValue) {
        this.default1 = defaultValue;
    }
    /** 订阅模型状态(0-提议，1-激活) */
    @Excel(name = "订阅模型状态(0-提议，1-激活)")
    private Long status;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

}
