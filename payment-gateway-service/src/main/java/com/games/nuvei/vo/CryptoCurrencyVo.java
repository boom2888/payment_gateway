package com.games.nuvei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyVo {

    /** 货币符号 */
    @ApiModelProperty(value = "货币符号", example = "USDT", required = true)
    private String symbol;

    /** 货币名称 */
    @ApiModelProperty(value = "货币名称")
    private String name;

    /** 货币名称 */
    @ApiModelProperty(value = "货币图标")
    private String icon;

    /** 货币名称 */
    @ApiModelProperty(value = "链")
    private Set<String> chain;

}
