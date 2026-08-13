package com.games.payment.mapper;

import com.games.payment.domain.ConfigCountry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 国家配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigCountryMapper extends BaseMapper<ConfigCountry> {
    /**
     * 查询国家配置列表
     *
     * @param configCountry 国家配置
     * @return 国家配置集合
     */
    List<ConfigCountry> selectAllList(ConfigCountry configCountry);

}
