package com.games.payment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * EMI 商户信息视图对象（个人+企业合并）
 *
 * @author Ticker
 * @date 2025-10-17
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EmiMerchantInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 名称（个人：姓名，企业：中文名称） */
    private String name;

    /** 商户类型（1：个人，2：企业） */
    private Integer merchantType;

    /** 状态 */
    private Integer status;

    /** 启用状态 */
    private Integer enabledStatus;

    /** 记录创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 账号 */
    private String account;

    /** 用户ID */
    private Long userId;
}

