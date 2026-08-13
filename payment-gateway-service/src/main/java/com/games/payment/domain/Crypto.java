package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.games.common.annotation.Excel;
import com.games.common.core.domain.entity.SysDictData;
import com.games.nuvei.vo.CryptoCurrencyVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 加密货币对象 crypto_currency
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("crypto_currency")
public class Crypto {

    public SysDictData toDict() {
        SysDictData data = new SysDictData();
        data.setDictLabel(name);
        data.setDictValue(id.toString());
        return data;
    }

    public CryptoCurrencyVo toVo() {
        CryptoCurrencyVo data = new CryptoCurrencyVo();
        data.setName(name);
        data.setSymbol(shortName);
        data.setIcon(icon);
        return data;
    }

    /** 加密货币ID */
    @TableId(value = "id")
    private Long id;

    /** 加密货币简称 */
    @Excel(name = "加密货币简称", langKey = "payment.crypto.excel.shortName")
    private String shortName;

    /** 加密货币名称 */
    @Excel(name = "加密货币名称", langKey = "payment.crypto.excel.name")
    private String name;

    /** 加密货币链 */
    @Excel(name = "加密货币链", langKey = "payment.crypto.excel.chain")
    private String chain;

    /** B2C2传统代码系统限制为3个字符 */
    @Excel(name = "B2C2传统代码系统限制为3个字符", langKey = "payment.crypto.excel.b2c2Ticker")
    private String b2c2Ticker;

    /** 区块链中加密货币表示的小数精度：ETH 18，BTC 8 */
    @Excel(name = "区块链中加密货币表示的小数精度：ETH 18，BTC 8", langKey = "payment.crypto.excel.decimalPrecision")
    private Long decimalPrecision;

    /** 是否基于UTXO */
    @Excel(name = "是否基于UTXO", langKey = "payment.crypto.excel.isUtxoBased")
    private BigDecimal isUtxoBased;

    /** 报价来源 */
    private String quoteSource;

    /** Fireblocks资产ID */
    @Excel(name = "Fireblocks资产ID", langKey = "payment.crypto.excel.fireblocksAssetId")
    private String fireblocksAssetId;

    /** 货币图标 */
    @Excel(name = "货币图标", langKey = "payment.crypto.excel.icon")
    private String icon;

    private String state;

    public boolean isB2c2Quote(){return false;}
}
