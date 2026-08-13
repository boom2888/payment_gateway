package com.games.payment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * MID配置对象 config_mid
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("config_mid")
public class ConfigMid  {


    /** MID ID */
    private Long id;

    /** 收单机构ID外键 */
    @Excel(name = "收单机构ID外键", langKey = "payment.configMid.excel.acquirerId")
    private Long acquirerId;

    /** 收单机构MID */
    @Excel(name = "收单机构MID", langKey = "payment.configMid.excel.midCode")
    private String midCode;

    /** MID描述 */
    @Excel(name = "MID描述", langKey = "payment.configMid.excel.description")
    private String description;

    /** 货币表或加密货币表主键 */
    @Excel(name = "货币表或加密货币表主键", langKey = "payment.configMid.excel.settlementCurrencyId")
    private Long settlementCurrencyId;

    /** 货币类型(0-法币，1-加密货币) */
    @Excel(name = "货币类型(0-法币，1-加密货币)", langKey = "payment.configMid.excel.settlementCurrencyType")
    private Long settlementCurrencyType;

    /** 类型(0-激活，1-停用) */
    @Excel(name = "类型(0-激活，1-停用)", langKey = "payment.configMid.excel.status")
    private Long status;

    /** Nuvei站点ID */
    @Excel(name = "Nuvei站点ID", langKey = "payment.configMid.excel.siteId")
    private String siteId;

    /** Nuvei站点名称 */
    @Excel(name = "Nuvei站点名称", langKey = "payment.configMid.excel.siteName")
    private String siteName;

    /** Nuvei客户端ID */
    @Excel(name = "Nuvei客户端ID", langKey = "payment.configMid.excel.clientId")
    private String clientId;

    /** Nuvei客户端名称 */
    @Excel(name = "Nuvei客户端名称", langKey = "payment.configMid.excel.clientName")
    private String clientName;

    /** 收单机构分配的商户ID */
    @Excel(name = "收单机构分配的商户ID", langKey = "payment.configMid.excel.acquirerMerchantId")
    private String acquirerMerchantId;

    /** 类型(0-NFT，1-加密货币) */
    @Excel(name = "类型(0-NFT，1-加密货币)", langKey = "payment.configMid.excel.type")
    private Long type;

}
