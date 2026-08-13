package com.games.payment.service;

import com.games.payment.domain.ConfigMid;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * MID配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigMidService extends IService<ConfigMid> {

    /**
     * 查询MID配置列表
     *
     * @param configMid MID配置
     * @return MID配置集合
     */
    List<ConfigMid> selectAllList(ConfigMid configMid);

    /**
     * 查询列表
     */
    List<ConfigMid> queryList(ConfigMid configMid);

}
