package com.games.payment.mapper;

import com.games.payment.domain.SaasUserCorporationSupportedCountry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 【请填写功能名称】Mapper接口
 *
 * @author Ticker
 * @date 2025-07-06
 */
public interface SaasUserCorporationSupportedCountryMapper extends BaseMapper<SaasUserCorporationSupportedCountry> {
    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCountry 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<SaasUserCorporationSupportedCountry> selectAllList(SaasUserCorporationSupportedCountry saasUserCorporationSupportedCountry);

}
