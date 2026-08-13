package com.games.payment.service;

import com.games.payment.domain.ConfigCountry;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 国家配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigCountryService extends IService<ConfigCountry> {

    /**
     * 查询国家配置列表
     *
     * @param configCountry 国家配置
     * @return 国家配置集合
     */
    List<ConfigCountry> selectAllList(ConfigCountry configCountry);

    /**
     * 查询列表
     */
    List<ConfigCountry> queryList(ConfigCountry configCountry);

}
