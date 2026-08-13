package com.games.payment.service;

import com.games.payment.domain.ConfigAcquirer;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 收单机构配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigAcquirerService extends IService<ConfigAcquirer> {

    /**
     * 查询收单机构配置列表
     *
     * @param configAcquirer 收单机构配置
     * @return 收单机构配置集合
     */
    List<ConfigAcquirer> selectAllList(ConfigAcquirer configAcquirer);

    /**
     * 查询列表
     */
    List<ConfigAcquirer> queryList(ConfigAcquirer configAcquirer);

}
