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
 * 定时任务调度对象 sys_job
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_job")
public class SysJob  {

private static final long serialVersionUID=1L;


    /** 任务ID */
    @TableId(value = "job_id")
    private Long jobId;

    /** 任务名称 */
    @TableField(value = "job_name")
    private String jobName;

    /** 任务组名 */
    @TableField(value = "job_group")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** cron执行表达式 */
    @Excel(name = "cron执行表达式")
    private String cronExpression;

    /** 计划执行错误策略（1立即执行 2执行一次 3放弃执行） */
    @Excel(name = "计划执行错误策略" , readConverterExp = "1=立即执行,2=执行一次,3=放弃执行")
    private String misfirePolicy;

    /** 是否并发执行（0-允许，1-禁止） */
    @Excel(name = "是否并发执行" , readConverterExp = "0=-允许，1-禁止")
    private String concurrent;

    /** 状态（0-正常，1-暂停） */
    @Excel(name = "状态" , readConverterExp = "0=-正常，1-暂停")
    private String status;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String remark;

}
