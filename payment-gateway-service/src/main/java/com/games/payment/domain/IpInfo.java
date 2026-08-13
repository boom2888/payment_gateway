package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * IP地址对象 ip_info
 *
 * @author Ticker
 * @date 2025-09-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ip_info")
public class IpInfo  {

    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** IP地址 */
    @Excel(name = "IP地址", langKey = "payment.ipInfo.excel.ip")
    private String ip;

    /** 新值 */
    @Excel(name = "新值", langKey = "payment.ipInfo.excel.value")
    private String value;

    /** 状态 */
    @Excel(name = "状态", langKey = "payment.ipInfo.excel.status")
    private String status;

    /** 创建时间 */
    @Excel(name = "创建时间", langKey = "payment.ipInfo.excel.createTime", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @Excel(name = "更新时间", langKey = "payment.ipInfo.excel.updateTime", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
