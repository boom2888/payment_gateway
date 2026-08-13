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
 * 访问令牌对象 access_token
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("access_token")
public class AccessToken  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private String id;

    /** 访问令牌 */
    @Excel(name = "访问令牌")
    private String accessToken;

    /** 过期时间 */
    @Excel(name = "过期时间")
    private String expiresIn;

    /** 数据版本号，每次更新时增加1 */
    @Excel(name = "数据版本号，每次更新时增加1")
    private String version;

    /** 令牌类型 */
    @Excel(name = "令牌类型")
    private String tokenType;

}
