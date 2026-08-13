package com.games.wynpay.swap.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.games.common.annotation.Excel;
import com.games.wynpay.swap.BuySell;
import com.games.wynpay.swap.SwapState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Swap订单对象 swap_order
 *
 * @author Minter
 * @date 2025-02-17
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("swap_order")
public class SwapOrder implements Serializable {

private static final long serialVersionUID=1L;

    public SwapOrder(Integer walletIndex, String address, BuySell buySell, String contact, BigDecimal amount, String hash) {
        this.index = walletIndex;
        this.address = address;
        this.buySell = buySell.name();
        this.contact = contact;
        this.amount = amount;
        this.hash = hash;
        this.state = SwapState.SENDING.name();
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    /** 编号 */
    @TableId(value = "id")
    private Long id;

    /** 商户编号 */
    @Excel(name = "商户编号")
    private Long shopId;

    /** 地址顺序 */
    @Excel(name = "地址顺序")
    private Integer index;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 方向 */
    @Excel(name = "方向")
    private String buySell;

    /** 代币 */
    @Excel(name = "代币")
    private String contact;

    /** 法币 */
    @Excel(name = "法币")
    private String currency;

    private String crypto;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal amount;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 状态 */
    @Excel(name = "状态")
    private String state;

    private String chain;

    /** HASH */
    @Excel(name = "HASH")
    private String hash;

    /** 创建人 */
    private Long createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private Long updateBy;

    /** 更新时间 */
    private Date updateTime;

}
