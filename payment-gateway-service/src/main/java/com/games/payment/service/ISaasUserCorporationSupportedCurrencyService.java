package com.games.payment.service;

import com.games.payment.domain.SaasUserCorporationSupportedCurrency;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author Ticker
 * @date 2025-07-06
 */
public interface ISaasUserCorporationSupportedCurrencyService extends IService<SaasUserCorporationSupportedCurrency> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCurrency 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<SaasUserCorporationSupportedCurrency> selectAllList(SaasUserCorporationSupportedCurrency saasUserCorporationSupportedCurrency);

    /**
     * 查询列表
     */
    List<SaasUserCorporationSupportedCurrency> queryList(SaasUserCorporationSupportedCurrency saasUserCorporationSupportedCurrency);

}
