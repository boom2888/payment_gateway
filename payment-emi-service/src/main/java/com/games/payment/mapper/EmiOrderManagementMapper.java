package com.games.payment.mapper;

import com.games.payment.domain.EmiOrderManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 订单管理Mapper接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface EmiOrderManagementMapper extends BaseMapper<EmiOrderManagement> {
    /**
     * 查询订单管理列表
     *
     * @param emiOrderManagement 订单管理
     * @return 订单管理集合
     */
    List<EmiOrderManagement> selectAllList(EmiOrderManagement emiOrderManagement);

}
