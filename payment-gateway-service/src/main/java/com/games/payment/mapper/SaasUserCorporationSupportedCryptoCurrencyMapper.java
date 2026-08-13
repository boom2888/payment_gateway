package com.games.payment.mapper;

import com.games.payment.domain.SaasUserCorporationSupportedCryptoCurrency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 【请填写功能名称】Mapper接口
 *
 * @author Ticker
 * @date 2025-07-06
 */
public interface SaasUserCorporationSupportedCryptoCurrencyMapper extends BaseMapper<SaasUserCorporationSupportedCryptoCurrency> {
    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCryptoCurrency 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<SaasUserCorporationSupportedCryptoCurrency> selectAllList(SaasUserCorporationSupportedCryptoCurrency saasUserCorporationSupportedCryptoCurrency);

}
