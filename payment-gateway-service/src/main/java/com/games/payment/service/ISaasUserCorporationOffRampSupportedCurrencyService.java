package com.games.payment.service;

import com.games.payment.domain.SaasUserCorporationOffRampSupportedCurrency;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author Ticker
 * @date 2025-07-06
 */
public interface ISaasUserCorporationOffRampSupportedCurrencyService extends IService<SaasUserCorporationOffRampSupportedCurrency> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationOffRampSupportedCurrency 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<SaasUserCorporationOffRampSupportedCurrency> selectAllList(SaasUserCorporationOffRampSupportedCurrency saasUserCorporationOffRampSupportedCurrency);

    /**
     * 查询列表
     */
    List<SaasUserCorporationOffRampSupportedCurrency> queryList(SaasUserCorporationOffRampSupportedCurrency saasUserCorporationOffRampSupportedCurrency);

}
