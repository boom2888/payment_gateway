package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 白名单信用卡对象 whitelisted_credit_cards
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("whitelisted_credit_cards")
public class WhitelistedCreditCards implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id")
    private Long id;

    /** 卡号（合规：仅落库“前六****后四”） */
    @Excel(name = "完整卡号", prompt = "选填，可填写16-19位完整卡号或“前六****后四”脱敏格式；系统只会保存“前六****后四”。填写完整卡号后，前6位和后4位可不填；不填时，前6位和后4位必填")
    private String fullNumber;

    @TableField("card_finger")
    private String cardFinger;

    /** 卡BIN号码 */

    @Excel(name = "卡前六位", prompt = "条件必填，6位数字。填写完整卡号时可不填，否则必填")
    private String binNumber;

    /** 卡最后四位号码 */
    @Excel(name = "卡后四位", prompt = "条件必填，4位数字。填写完整卡号时可不填，否则必填")
    private String lastFourNumber;

    private String email;

    @Excel(name = "风险等级", prompt = "必填，纯数字，例如：1-低风险 2-中风险 3-高风险")
    private Integer riskLevel;

    private Long shopId;

    @Excel(name = "关联商户编号", prompt = "必填，商户ID必须在系统中存在，如果商户ID不存在，该条数据会被过滤，不会导入，多个商户用分号分隔，例如：123 或 123;456;789")
    @TableField(exist = false)
    private String shopIds;

    /** 记录创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    private Long createdBy;

    /** 记录描述 */
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    private Long whiteUploadId;

    @TableField(exist = false)
    private String originName;

    @TableField(exist = false)
    private String businessName;

    /**
     * 支持createdAt范围查询
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 用于批量查询的ID数组
     */
    @TableField(exist = false)
    private Long[] ids;
}
