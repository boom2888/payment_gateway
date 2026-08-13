package com.games.payment.mapper;

import com.games.payment.domain.CountryCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 欧洲国家的地区Mapper接口
 *
 * @author Ticker
 * @date 2025-07-22
 */
public interface CountryCategoryMapper extends BaseMapper<CountryCategory> {
    /**
     * 查询欧洲国家的地区列表
     *
     * @param countryCategory 欧洲国家的地区
     * @return 欧洲国家的地区集合
     */
    List<CountryCategory> selectAllList(CountryCategory countryCategory);

}
