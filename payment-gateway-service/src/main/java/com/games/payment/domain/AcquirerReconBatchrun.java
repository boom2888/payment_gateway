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
 * 收单机构对账批处理对象 acquirer_recon_batchrun
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("acquirer_recon_batchrun")
public class AcquirerReconBatchrun  {

private static final long serialVersionUID=1L;


    /** 订单对账ID */
    @TableId(value = "id")
    private Long id;

    /** 收单机构ID：config_acquirer表主键 */
    @Excel(name = "收单机构ID：config_acquirer表主键")
    private Long acquirerId;

    /** 支付ID */
    @Excel(name = "支付ID")
    private String acquirerBatchPayoutId;

    /** 对账作业开始时间 */
    @Excel(name = "对账作业开始时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reconStart;

    /** 对账作业结束时间 */
    @Excel(name = "对账作业结束时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reconEnd;

    /** 每次对账作业的总订单数 */
    @Excel(name = "每次对账作业的总订单数")
    private Long totalNumberOfOrder;

    /** 每次对账作业的匹配订单数 */
    @Excel(name = "每次对账作业的匹配订单数")
    private Long matchedNumberOfOrder;

    /** 每次对账作业的未匹配订单数 */
    @Excel(name = "每次对账作业的未匹配订单数")
    private Long unmatchedNumberOfOrder;

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

    /** recon_batch_job_file表主键 */
    @Excel(name = "recon_batch_job_file表主键")
    private Long reconFileId;

    /** 捕获金额 */
    @Excel(name = "捕获金额")
    private BigDecimal totalCapturedAmount;

    /** 拒付金额 */
    @Excel(name = "拒付金额")
    private BigDecimal totalChargebackAmount;

    /** 退款金额 */
    @Excel(name = "退款金额")
    private BigDecimal totalRefundAmount;

    /** 收单机构费用金额 */
    @Excel(name = "收单机构费用金额")
    private BigDecimal totalAcquirerFeeAmount;

    /** 总结算费用金额 */
    @Excel(name = "总结算费用金额")
    private BigDecimal totalSettledAmount;

}
