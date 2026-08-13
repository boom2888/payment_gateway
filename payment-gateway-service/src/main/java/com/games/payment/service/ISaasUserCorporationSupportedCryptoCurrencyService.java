package com.games.payment.service;

import com.games.payment.domain.SaasUserCorporationSupportedCryptoCurrency;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author Ticker
 * @date 2025-07-06
 */
public interface ISaasUserCorporationSupportedCryptoCurrencyService extends IService<SaasUserCorporationSupportedCryptoCurrency> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCryptoCurrency 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<SaasUserCorporationSupportedCryptoCurrency> selectAllList(SaasUserCorporationSupportedCryptoCurrency saasUserCorporationSupportedCryptoCurrency);

    /**
     * 查询列表
     */
    List<SaasUserCorporationSupportedCryptoCurrency> queryList(SaasUserCorporationSupportedCryptoCurrency saasUserCorporationSupportedCryptoCurrency);

}
