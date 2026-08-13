package com.games.payment.service;

import com.games.payment.domain.ConfigContinent;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 大洲配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigContinentService extends IService<ConfigContinent> {

    /**
     * 查询大洲配置列表
     *
     * @param configContinent 大洲配置
     * @return 大洲配置集合
     */
    List<ConfigContinent> selectAllList(ConfigContinent configContinent);

    /**
     * 查询列表
     */
    List<ConfigContinent> queryList(ConfigContinent configContinent);

}
