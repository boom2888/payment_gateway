package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.games.common.annotation.Excel;
import com.games.common.core.domain.entity.SysDictData;
import com.games.nuvei.vo.CryptoCurrencyVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 货币对象 currency
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("currency")
public class Currency  {

    private static final long serialVersionUID=1L;

    public SysDictData toDict() {
        SysDictData data = new SysDictData();
        data.setDictLabel(name);
        data.setDictValue(id.toString());
        data.setRemark(code);
        return data;
    }

    public CryptoCurrencyVo toVo() {
        CryptoCurrencyVo data = new CryptoCurrencyVo();
        data.setName(name);
        data.setSymbol(name);
        data.setIcon(icon);
        return data;
    }


    /** SaaS用户ID */
    @TableId(value = "id")
    private Long id;

    /** 货币代码 */
    @Excel(name = "货币代码")
    private String code;

    /** 货币符号 */
    @Excel(name = "货币符号")
    private String symbol;

    /** 货币名称 */
    @Excel(name = "货币名称")
    private String name;

    /** 货币名称 */
    @Excel(name = "货币图标")
    private String icon;

    private String state;
}
