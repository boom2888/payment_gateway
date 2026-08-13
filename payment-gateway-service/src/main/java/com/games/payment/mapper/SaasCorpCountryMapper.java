package com.games.payment.mapper;

import com.games.payment.domain.SaasCorpCountry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * SaaS用户公司支持国家Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SaasCorpCountryMapper extends BaseMapper<SaasCorpCountry> {
    /**
     * 查询SaaS用户公司支持国家列表
     *
     * @param saasCorpCountry SaaS用户公司支持国家
     * @return SaaS用户公司支持国家集合
     */
    List<SaasCorpCountry> selectAllList(SaasCorpCountry saasCorpCountry);

}
