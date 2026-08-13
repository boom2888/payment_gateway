package com.games.payment.service;

import com.games.payment.domain.EmiOrderManagement;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 订单管理Service接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface IEmiOrderManagementService extends IService<EmiOrderManagement> {

    /**
     * 查询订单管理列表
     *
     * @param emiOrderManagement 订单管理
     * @return 订单管理集合
     */
    List<EmiOrderManagement> selectAllList(EmiOrderManagement emiOrderManagement);

    /**
     * 查询列表
     */
    List<EmiOrderManagement> queryList(EmiOrderManagement emiOrderManagement);

}
