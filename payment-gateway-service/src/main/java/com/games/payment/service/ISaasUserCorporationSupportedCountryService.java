package com.games.payment.service;

import com.games.payment.domain.SaasUserCorporationSupportedCountry;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author Ticker
 * @date 2025-07-06
 */
public interface ISaasUserCorporationSupportedCountryService extends IService<SaasUserCorporationSupportedCountry> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCountry 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<SaasUserCorporationSupportedCountry> selectAllList(SaasUserCorporationSupportedCountry saasUserCorporationSupportedCountry);

    /**
     * 查询列表
     */
    List<SaasUserCorporationSupportedCountry> queryList(SaasUserCorporationSupportedCountry saasUserCorporationSupportedCountry);

}
