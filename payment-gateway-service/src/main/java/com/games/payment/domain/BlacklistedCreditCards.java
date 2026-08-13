package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.util.Date;

/**
 * 黑名单信用卡对象 blacklisted_credit_cards
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("blacklisted_credit_cards")
public class BlacklistedCreditCards  {

private static final long serialVersionUID=1L;


    /** ID */
    @TableId(value = "id")
    private Long id;

    /** 卡完整号码 */
    @Excel(name = "卡完整号码")
    private String fullNumber;

    @TableField("card_finger")
    private String cardFinger;

    /** 卡BIN号码 */
    @Excel(name = "卡BIN号码")
    private String binNumber;

    /** 卡的最后四位数字 */
    @Excel(name = "卡的最后四位数字")
    private String lastFourNumber;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
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
    @TableLogic
    private Integer deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

    /** 记录更新时间 */
    @Excel(name = "记录更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

}
