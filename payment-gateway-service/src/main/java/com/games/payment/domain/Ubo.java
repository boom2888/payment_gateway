package com.games.payment.domain;

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
 * 商户认证受益人信息
 *
 * @author zebord
 * @date 2025-02-05
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("credit_ubo")
public class Ubo implements Serializable {

private static final long serialVersionUID=1L;

    /** 编号 */
    @TableId(value = "id")
    private Long id;

    @Excel(name = "商户ID")
    private Long shopId;

    /** 身份证明文件类型 */
    @Excel(name = "身份证明文件类型")
    private String authType;

    /** 身份证明文件正面 */
    @Excel(name = "身份证明文件正面")
    private String idDocFrontUrl;

    /** 身份证明文件反面 */
    @Excel(name = "身份证明文件反面")
    private String idDocBackUrl;

    /** 地址证明类型 */
    @Excel(name = "地址证明类型")
    private String addressType;

    /** 地址证明文件地址 */
    @Excel(name = "地址证明文件地址")
    private String addressDocUrl;

    /** 最终受益人邮箱地址 */
    @Excel(name = "最终受益人邮箱地址")
    private String uboEmail;

    @Excel(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
