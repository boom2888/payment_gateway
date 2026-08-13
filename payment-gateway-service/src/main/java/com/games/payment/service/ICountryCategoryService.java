package com.games.payment.service;

import com.games.payment.domain.CountryCategory;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 欧洲国家的地区Service接口
 *
 * @author Ticker
 * @date 2025-07-22
 */
public interface ICountryCategoryService extends IService<CountryCategory> {

    /**
     * 查询欧洲国家的地区列表
     *
     * @param countryCategory 欧洲国家的地区
     * @return 欧洲国家的地区集合
     */
    List<CountryCategory> selectAllList(CountryCategory countryCategory);

    /**
     * 查询列表
     */
    List<CountryCategory> queryList(CountryCategory countryCategory);

    boolean isEu(String country);
}
